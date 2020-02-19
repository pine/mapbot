package moe.pine.mapbot.config;

import moe.pine.mapbot.gnavi.Gnavi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GnaviConfig {
    @Bean
    public Gnavi gnavi() {
        return new Gnavi();
    }
}
