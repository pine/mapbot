package moe.pine.mapbot.config;

import moe.pine.mapbot.amp.Amp;
import moe.pine.mapbot.jsonld.JsonLdParser;
import moe.pine.mapbot.medium.Browser;
import moe.pine.mapbot.medium.DenyHostFilter;
import moe.pine.mapbot.medium.Medium;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties(MediumProperties.class)
public class MediumConfig {
    @Bean
    public Medium medium(
            Amp amp,
            Browser browser,
            DenyHostFilter denyHostFilter
    ) {
        return new Medium(amp, browser, denyHostFilter);
    }

    @Bean
    public Browser browser(
            JsonLdParser jsonLdParser,
            WebClient.Builder webClientBuilder
    ) {
        return new Browser(jsonLdParser, webClientBuilder.build());
    }

    @Bean
    public DenyHostFilter denyHostFilter(
            MediumProperties mediumProperties
    ) {
        return new DenyHostFilter(mediumProperties.getDenyHosts());
    }
}
