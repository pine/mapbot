package moe.pine.mapbot.services.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.mapbot.log.SentLog;
import moe.pine.mapbot.log.SentLogRepository;
import moe.pine.mapbot.properties.SlackProperties;
import moe.pine.mapbot.services.message.OutgoingMessageService;
import moe.pine.mapbot.slack.MessageEvent;
import moe.pine.mapbot.slack.PostMessageRequest;
import moe.pine.mapbot.slack.PostMessageResponse;
import moe.pine.mapbot.slack.SlackClient;
import moe.pine.mapbot.slack.TextField;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
class MessageSentEventHandler {
    private final SentLogRepository sentLogRepository;
    private final SlackClient slackClient;
    private final SlackProperties slackProperties;
    private final OutgoingMessageService outgoingMessageService;

    void execute(MessageEvent messageEvent) throws InterruptedException {
        List<TextField> textFields = outgoingMessageService.generate(messageEvent.getText());
        if (CollectionUtils.isEmpty(textFields)) {
            return;
        }

        String threadTs = StringUtils.firstNonEmpty(messageEvent.getThreadTs(), messageEvent.getTs());
        PostMessageRequest postMessageRequest =
                PostMessageRequest.builder()
                        .username(slackProperties.getUsername())
                        .threadTs(threadTs)
                        .channel(messageEvent.getChannel())
                        .textFields(textFields)
                        .iconUrl(slackProperties.getIconUrl())
                        .build();

        PostMessageResponse postMessageResponse =
                slackClient.postMessage(postMessageRequest);

        SentLog sentLog =
                new SentLog(
                        messageEvent.getChannel(),
                        messageEvent.getTs(),
                        postMessageResponse.getTs()
                );
        sentLogRepository.add(sentLog);
    }
}
