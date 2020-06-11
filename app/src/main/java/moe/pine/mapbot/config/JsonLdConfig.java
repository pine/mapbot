package moe.pine.mapbot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import moe.pine.mapbot.jsonld.JsonLdParser;
import moe.pine.mapbot.jsonld.factory.DefaultFactory;
import moe.pine.mapbot.jsonld.factory.Factory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonLdConfig {
    @Bean
    public Factory factory() {
        return new DefaultFactory();
    }

    @Bean
    public JsonLdParser jsonLdParser(
            ObjectMapper jsonLdObjectMapper,
            Factory factory
    ) {
        return new JsonLdParser(jsonLdObjectMapper, factory);
    }
}
