package moe.pine.mapbot.amp;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

public class Amp {
    private static final String AMP_PREFIX = "https://www.google.co.jp/amp/";
    private static final Duration BLOCK_TIMEOUT = Duration.ofSeconds(10L);

    private WebClient webClient;

    public Amp(WebClient.Builder webClientBuilder) {
        webClient = webClientBuilder.build();
    }

    public Optional<String> resolveRedirect(String absoluteUrl) {
        if (absoluteUrl.startsWith(AMP_PREFIX)) {
            return getRedirectUrl(absoluteUrl);
        }

        return Optional.empty();
    }

    protected Optional<String> getRedirectUrl(String absoluteUrl) {
        ClientResponse clientResponse =
                webClient.get()
                        .uri(absoluteUrl)
                        .exchange()
                        .block(BLOCK_TIMEOUT);
        if (clientResponse == null) {
            return Optional.empty();
        }
        clientResponse.bodyToMono(String.class).block(BLOCK_TIMEOUT);

        List<String> redirectHeaders =
                clientResponse.headers().header(HttpHeaders.LOCATION);
        if (CollectionUtils.isEmpty(redirectHeaders)) {
            return Optional.empty();
        }

        return Optional.ofNullable(StringUtils.firstNonEmpty(redirectHeaders.get(0)));
    }
}
