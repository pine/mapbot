package moe.pine.mapbot.tabelog;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

class PathResolver {
    private static final Duration BLOCK_TIMEOUT = Duration.ofSeconds(10L);
    private static final String OFFICIAL_URL_PREFIX = "https://tabelog.com/";
    private static final String AMP_URL_HOST = "https://www.google.co.jp/amp/s/s.tabelog.com/";

    private WebClient webClient;

    PathResolver(WebClient.Builder webClientBuilder) {
        webClient = webClientBuilder.build();
    }

    Optional<String> resolve(String absoluteUrl) {
        Objects.requireNonNull(absoluteUrl);

        if (absoluteUrl.startsWith(OFFICIAL_URL_PREFIX)) {
            return Optional.ofNullable(StringUtils.firstNonEmpty(getPath(absoluteUrl)));
        }

        if (absoluteUrl.startsWith(AMP_URL_HOST)) {
            ClientResponse clientResponse =
                    webClient.get()
                            .uri(absoluteUrl)
                            .exchange()
                            .block(BLOCK_TIMEOUT);
            if (clientResponse == null) {
                return Optional.empty();
            }
            clientResponse.bodyToMono(String.class).block(BLOCK_TIMEOUT);

            List<String> locationHeaders =
                    clientResponse.headers().header(HttpHeaders.LOCATION);
            if (CollectionUtils.isEmpty(locationHeaders)) {
                return Optional.empty();
            }

            String locationHeader = locationHeaders.get(0);
            if (locationHeader.startsWith(OFFICIAL_URL_PREFIX)) {
                return Optional.ofNullable(StringUtils.firstNonEmpty(getPath(locationHeader)));
            }
        }

        return Optional.empty();
    }

    private String getPath(String absoluteUrl) {
        try {
            return new URL(absoluteUrl).getPath();
        } catch (MalformedURLException e) {
            throw new UncheckedIOException(e);
        }
    }
}
