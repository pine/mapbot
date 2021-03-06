package moe.pine.mapbot.services.bot;

import moe.pine.mapbot.slack.Event;
import moe.pine.mapbot.slack.EventListener;
import moe.pine.mapbot.slack.MessageEvent;
import moe.pine.mapbot.slack.MessageEvent.Subtypes;
import moe.pine.mapbot.slack.SlackClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.time.Clock;
import java.time.Instant;

@Service
public class BotService {
    private final SlackClient slackClient;

    private final MessageChangedEventHandler messageChangedEventHandler;
    private final MessageDeletedEventHandler messageDeletedEventHandler;
    private final MessageSentEventHandler messageSentEventHandler;
    private final Instant startupTime;

    private final EventListener eventConsumer = this::onEvent;

    public BotService(
            SlackClient slackClient,
            MessageChangedEventHandler messageChangedEventHandler,
            MessageDeletedEventHandler messageDeletedEventHandler,
            MessageSentEventHandler messageSentEventHandler,
            Clock clock
    ) {
        this.slackClient = slackClient;
        this.messageChangedEventHandler = messageChangedEventHandler;
        this.messageDeletedEventHandler = messageDeletedEventHandler;
        this.messageSentEventHandler = messageSentEventHandler;

        startupTime = clock.instant();
        slackClient.addEventListener(eventConsumer);
    }

    @PreDestroy
    public void destroy() {
        slackClient.removeEventListener(eventConsumer);
    }

    private void onEvent(Event event) throws InterruptedException {
        if (event instanceof MessageEvent) {
            onMessageEvent((MessageEvent) event);
        }
    }

    private void onMessageEvent(MessageEvent messageEvent) throws InterruptedException {
        String ts = messageEvent.getTs();
        if (StringUtils.isEmpty(ts)) {
            return;
        }

        double parsedTs = Double.parseDouble(ts);
        if (parsedTs < startupTime.getEpochSecond()) {
            return;
        }
        if (StringUtils.isNotEmpty(messageEvent.getBotId())) {
            return;
        }

        final boolean isSentEvent =
                StringUtils.isEmpty(messageEvent.getSubtype()) ||
                        Subtypes.THREAD_BROADCAST.equals(messageEvent.getSubtype());
        final boolean isChangedEvent =
                Subtypes.MESSAGE_CHANGED.equals(messageEvent.getSubtype()) &&
                        !Subtypes.TOMBSTONE.equals(messageEvent.getMessage().getSubtype());
        final boolean isDeletedEvent =
                Subtypes.MESSAGE_DELETED.equals(messageEvent.getSubtype()) ||
                        Subtypes.MESSAGE_CHANGED.equals(messageEvent.getSubtype()) &&
                                Subtypes.TOMBSTONE.equals(messageEvent.getMessage().getSubtype());

        if (isSentEvent) {
            messageSentEventHandler.execute(messageEvent);
        } else if (isChangedEvent) {
            messageChangedEventHandler.execute(messageEvent);
        } else if (isDeletedEvent) {
            messageDeletedEventHandler.execute(messageEvent);
        }
    }
}
