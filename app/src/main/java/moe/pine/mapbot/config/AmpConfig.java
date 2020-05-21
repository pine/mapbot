package moe.pine.mapbot.config;

import moe.pine.mapbot.amp.Amp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AmpConfig {
    @Bean
    public Amp amp(WebClient.Builder webClientBuilder) {
        return new Amp(webClientBuilder.build());
    }
}
