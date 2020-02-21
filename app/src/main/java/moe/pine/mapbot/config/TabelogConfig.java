package moe.pine.mapbot.config;

import moe.pine.mapbot.structured_data.StructuredDataParser;
import moe.pine.mapbot.tabelog.Tabelog;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class TabelogConfig {
    @Bean
    public Tabelog tabelog(
            WebClient.Builder webClientBuilder,
            StructuredDataParser structuredDataParser
    ) {
        return new Tabelog(webClientBuilder, structuredDataParser);
    }
}
