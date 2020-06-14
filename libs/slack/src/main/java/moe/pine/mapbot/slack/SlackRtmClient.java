package moe.pine.mapbot.slack;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.methods.SlackApiException;
import com.github.seratch.jslack.api.rtm.RTMClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.support.RetryTemplate;

import javax.websocket.CloseReason;
import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
class SlackRtmClient {
    private final StateManager stateManager;
    private final RetryTemplate unlimitedRetryTemplate;
    private final RTMClient rtmClient;

    private final List<EventListener> eventListeners = new CopyOnWriteArrayList<>();

    SlackRtmClient(
            String token,
            StateManager stateManager,
            RetryTemplate retryTemplate,
            RetryTemplate unlimitedRetryTemplate
    ) {
        Objects.requireNonNull(token);
        Objects.requireNonNull(retryTemplate);

        this.stateManager = Objects.requireNonNull(stateManager);
        this.unlimitedRetryTemplate = Objects.requireNonNull(unlimitedRetryTemplate);

        rtmClient = retryTemplate.execute(ctx -> {
            try {
                return new Slack().rtm(token);
            } catch (IOException e) {
                throw new SlackClientException(e);
            }
        });
        rtmClient.addMessageHandler(this::onEvent);
        rtmClient.addErrorHandler(this::onError);
        rtmClient.addCloseHandler(this::onClose);

        retryTemplate.execute(ctx -> {
            try {
                rtmClient.connect();
                return null;
            } catch (IOException | DeploymentException e) {
                throw new SlackClientException(e);
            }
        });
    }

    void addEventListener(EventListener listener) {
        eventListeners.add(listener);
    }

    void removeEventListener(EventListener listener) {
        eventListeners.remove(listener);
    }

    void close() throws IOException {
        rtmClient.close();
    }

    private void onEvent(String content) {
        log.debug("New event received: {}", content);
        stateManager.throwIfAlreadyClosed();

        Optional<Event> eventOpt = Events.parse(content);
        if (eventOpt.isEmpty()) {
            return;
        }

        Event event = eventOpt.get();
        try {
            for (EventListener listener : eventListeners) {
                stateManager.throwIfAlreadyClosed();
                listener.accept(event);
            }
        } catch (InterruptedException e) {
            log.error("Event listener is interrupted", e);
            Thread.currentThread().interrupt();
        }
    }

    private void onError(Throwable error) {
        log.error("An error has occurred.", error);
    }

    private void onClose(CloseReason closeReason) {
        log.info("The socket has been closed. The reason is {}. Trying reconnect.", closeReason);

        unlimitedRetryTemplate.execute(ctx -> {
            stateManager.throwIfAlreadyClosed();
            try {
                rtmClient.reconnect();
                return null;
            } catch (IOException | SlackApiException | URISyntaxException | DeploymentException e) {
                log.info("Connecting failed. Number of retries is {}", ctx.getRetryCount(), e);
                throw new SlackClientException(e);
            }
        });
    }
}
