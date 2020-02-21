package moe.pine.mapbot.config;

import moe.pine.mapbot.structured_data.StructuredDataParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StructuredDataConfig {
    @Bean
    public StructuredDataParser structuredDataParser() {
        return new StructuredDataParser();
    }
}
