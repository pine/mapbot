package moe.pine.mapbot.config;

import moe.pine.mapbot.amp.Amp;
import moe.pine.mapbot.jsonld.JsonLd;
import moe.pine.mapbot.medium.Medium;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MediumConfig {
    @Bean
    public Medium medium(Amp amp, JsonLd jsonLd) {
        return new Medium(amp, jsonLd);
    }
}
