package moe.pine.mapbot.config;

import moe.pine.mapbot.properties.SlackProperties;
import moe.pine.mapbot.slack.SlackClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SlackProperties.class)
public class SlackConfig {
    @Bean(destroyMethod = "close")
    public SlackClient slackClient(SlackProperties slackProperties) {
        return new SlackClient(slackProperties.getToken());
    }
}
