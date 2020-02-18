package moe.pine.mapbot.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.mapbot.properties.SlackProperties;
import moe.pine.mapbot.slack.MessageEvent;
import moe.pine.mapbot.slack.PostMessageRequest;
import moe.pine.mapbot.slack.PostMessageResponse;
import moe.pine.mapbot.slack.SlackClient;
import moe.pine.mapbot.slack.TextField;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageSentEventHandler {
    private final SlackClient slackClient;
    private final SlackProperties slackProperties;
    private final OutgoingTextService outgoingTextService;

    void execute(MessageEvent messageEvent) throws InterruptedException {
        List<TextField> textFields = outgoingTextService.generate(messageEvent.getText());
        if (CollectionUtils.isEmpty(textFields)) {
            return;
        }

        PostMessageRequest postMessageRequest =
                PostMessageRequest.builder()
                        .username(slackProperties.getUsername())
                        .threadTs(messageEvent.getThreadTs())
                        .channel(messageEvent.getChannel())
                        .textFields(textFields)
                        .iconUrl(slackProperties.getIconUrl())
                        .build();

        PostMessageResponse postMessageResponse =
                slackClient.postMessage(postMessageRequest);


    }
}
