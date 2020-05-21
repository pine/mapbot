package moe.pine.mapbot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import moe.pine.mapbot.jsonld.JsonLdParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonLdConfig {
    @Bean
    public JsonLdParser jsonLdParser(
            ObjectMapper jsonLdObjectMapper
    ) {
        return new JsonLdParser(jsonLdObjectMapper);
    }
}
