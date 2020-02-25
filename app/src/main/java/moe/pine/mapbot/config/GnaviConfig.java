package moe.pine.mapbot.config;

import moe.pine.mapbot.gnavi.Gnavi;
import moe.pine.mapbot.structured_data.StructuredDataParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GnaviConfig {
    @Bean
    public Gnavi gnavi(
            WebClient.Builder webClientBuilder,
            StructuredDataParser structuredDataParser
    ) {
        return new Gnavi(webClientBuilder, structuredDataParser);
    }
}
