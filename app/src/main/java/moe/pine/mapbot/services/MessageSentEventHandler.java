package moe.pine.mapbot.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.mapbot.properties.SlackProperties;
import moe.pine.mapbot.slack.MessageEvent;
import moe.pine.mapbot.slack.SlackClient;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageSentEventHandler {
    private final SlackProperties slackProperties;
    private final SlackClient slackClient;

    void execute(MessageEvent messageEvent) throws InterruptedException {
    }
}
