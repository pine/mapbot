package moe.pine.mapbot.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.mapbot.properties.SlackProperties;
import moe.pine.mapbot.slack.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageSentEventHandler {
    private final SlackProperties slackProperties;
    private final SlackClient slackClient;
    private final OutgoingTextService outgoingTextService;

    void execute(MessageEvent messageEvent) throws InterruptedException {
        List<TextField> textFields = outgoingTextService.generate(messageEvent.getText());
        if (CollectionUtils.isEmpty(textFields)) {
            return;
        }

        PostMessageRequest postMessageRequest =
                PostMessageRequest.builder()
                        .username(slackProperties.getUsername())
                        .channel(messageEvent.getChannel())
                        .textFields(textFields)
                        .iconUrl(slackProperties.getIconUrl())
                        .build();

        PostMessageResponse postMessageResponse =
                slackClient.postMessage(postMessageRequest);
    }
}
