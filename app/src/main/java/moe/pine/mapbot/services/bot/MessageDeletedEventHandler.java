package moe.pine.mapbot.services.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.mapbot.log.SentLog;
import moe.pine.mapbot.log.SentLogId;
import moe.pine.mapbot.log.SentLogRepository;
import moe.pine.mapbot.slack.DeleteMessageRequest;
import moe.pine.mapbot.slack.MessageEvent;
import moe.pine.mapbot.slack.SlackClient;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
class MessageDeletedEventHandler {
    private final SentLogRepository sentLogRepository;
    private final SlackClient slackClient;

    void execute(MessageEvent messageEvent) {
        SentLogId sentLogId =
                new SentLogId(
                        messageEvent.getChannel(),
                        messageEvent.getPreviousMessage().getTs());
        Optional<SentLog> sentLogOpt = sentLogRepository.get(sentLogId);
        if (sentLogOpt.isEmpty()) {
            return;
        }

        SentLog sentLog = sentLogOpt.get();
        DeleteMessageRequest deleteMessageRequest =
                DeleteMessageRequest.builder()
                        .channel(sentLog.getChannel())
                        .ts(sentLog.getDestinationTs())
                        .build();
        slackClient.deleteMessage(deleteMessageRequest);
    }
}
