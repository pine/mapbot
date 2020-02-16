package moe.pine.mapbot.config;

import moe.pine.mapbot.google_map.GoogleMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleMapConfig {
    @Bean
    public GoogleMap googleMap() {
        return new GoogleMap();
    }
}
