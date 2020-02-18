package moe.pine.mapbot.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.mapbot.properties.SlackProperties;
import moe.pine.mapbot.slack.MessageEvent;
import moe.pine.mapbot.slack.PostMessageRequest;
import moe.pine.mapbot.slack.SlackClient;
import moe.pine.mapbot.slack.TextField;
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
        Optional<String> outgoingTextOpt = outgoingTextService.generate(messageEvent.getText());
        if (outgoingTextOpt.isEmpty()) {
            return;
        }

        log.info("{}", outgoingTextOpt);

        String outgoingText = outgoingTextOpt.get();
        PostMessageRequest postMessageRequest =
                PostMessageRequest.builder()
                        .username("foo")
                        .channel(messageEvent.getChannel())
                        .textFields(List.of(new TextField("aa", outgoingText)))
                        .build();

        log.info("postMessage: {}", postMessageRequest);
        slackClient.postMessage(postMessageRequest);
    }
}
