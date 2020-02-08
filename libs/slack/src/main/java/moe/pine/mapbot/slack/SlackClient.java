package moe.pine.mapbot.slack;

import lombok.extern.slf4j.Slf4j;
import moe.pine.mapbot.retry_support.RetryTemplateFactory;
import org.springframework.retry.support.RetryTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class SlackClient {
    class StateManagerImpl implements StateManager {
        @Override
        public boolean isClosed() {
            return closed.get();
        }
    }

    private final SlackRtmClient slackRtmClient;
    private final SlackWebClient slackWebClient;

    private final AtomicBoolean closed = new AtomicBoolean();
    private final StateManager stateManager = new StateManagerImpl();

    public SlackClient(String token) {
        this(token,
            RetryTemplateFactory.create(
                5, 500, 2.0, SlackClientException.class),
            RetryTemplateFactory.createUnlimited(
                500, 60 * 1000, 2.0, SlackClientException.class));
    }

    SlackClient(
        String token,
        RetryTemplate retryTemplate,
        RetryTemplate unlimitedRetryTemplate
    ) {
        Objects.requireNonNull(retryTemplate);
        Objects.requireNonNull(unlimitedRetryTemplate);

        this.slackRtmClient = new SlackRtmClient(
            token,
            stateManager,
            retryTemplate,
            unlimitedRetryTemplate);
        this.slackWebClient = new SlackWebClient(
            token,
            stateManager,
            retryTemplate);
    }

    public void addEventListener(EventListener listener) {

        slackRtmClient.addEventListener(listener);
    }

    public void removeEventListener(EventListener listener) {
        slackRtmClient.removeEventListener(listener);
    }

    public PostMessageResponse postMessage(PostMessageRequest postMessageRequest) {
        return slackWebClient.postMessage(postMessageRequest);
    }

    public void updateMessage(UpdateMessageRequest updateMessageRequest) {
        slackWebClient.updateMessage(updateMessageRequest);
    }

    public void deleteMessage(DeleteMessageRequest deleteMessageRequest) {
        slackWebClient.deleteMessage(deleteMessageRequest);
    }

    public List<User> getUsers() {
        return slackWebClient.getUsers();
    }

    public void close() throws IOException {
        if (closed.compareAndSet(false, true)) {
            log.info("Closing the socket.");
            slackRtmClient.close();
        }
    }
}
