package moe.pine.mapbot.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.mapbot.log.SentLog;
import moe.pine.mapbot.log.SentLogId;
import moe.pine.mapbot.log.SentLogRepository;
import moe.pine.mapbot.properties.BotProperties;
import moe.pine.mapbot.slack.MessageEvent;
import moe.pine.mapbot.slack.SlackClient;
import moe.pine.mapbot.slack.TextField;
import moe.pine.mapbot.slack.UpdateMessageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageChangedEventHandler {
    private final BotProperties botProperties;
    private final SentLogRepository sentLogRepository;
    private final SlackClient slackClient;
    private final OutgoingTextService outgoingTextService;

    void execute(MessageEvent messageEvent) throws InterruptedException {
        if (messageEvent.getMessage().getEdited() == null) {
            return;
        }

        SentLogId sentLogId =
                new SentLogId(
                        messageEvent.getChannel(),
                        messageEvent.getMessage().getTs());
        Optional<SentLog> sentLogOpt = sentLogRepository.get(sentLogId);
        if (sentLogOpt.isEmpty()) {
            return;
        }
        SentLog sentLog = sentLogOpt.get();

        String messageText = messageEvent.getMessage().getText();
        List<TextField> textFields = outgoingTextService.generate(messageText);
        if (CollectionUtils.isEmpty(textFields)) {
            UpdateMessageRequest updateMessageRequest =
                    UpdateMessageRequest.builder()
                            .channel(sentLog.getChannel())
                            .text(botProperties.getEditedMessage())
                            .ts(sentLog.getDestinationTs())
                            .build();
            slackClient.updateMessage(updateMessageRequest);
            return;
        }

        UpdateMessageRequest updateMessageRequest =
                UpdateMessageRequest.builder()
                        .channel(sentLog.getChannel())
                        .textFields(textFields)
                        .ts(sentLog.getDestinationTs())
                        .build();
        slackClient.updateMessage(updateMessageRequest);
    }
}
