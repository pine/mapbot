package moe.pine.mapbot.amp;

import moe.pine.mapbot.retry_support.RetryTemplateFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Amp {
    private static final String AMP_PREFIX = "https://www.google.co.jp/amp/";
    private static final Duration BLOCK_TIMEOUT = Duration.ofSeconds(10L);

    private final WebClient webClient;
    private final RetryTemplate retryTemplate;

    public Amp(WebClient webClient) {
        this(webClient,
                RetryTemplateFactory.create(
                        5, 500, 2.0, AmpException.class));
    }

    protected Amp(WebClient webClient, RetryTemplate retryTemplate) {
        this.webClient = Objects.requireNonNull(webClient);
        this.retryTemplate = Objects.requireNonNull(retryTemplate);
    }

    public Optional<String> resolveOriginalUrl(String absoluteUrl) {
        if (!absoluteUrl.startsWith(AMP_PREFIX)) {
            return Optional.empty();
        }

        return retryTemplate.execute(ctx -> {
            try {
                return getRedirectedUrl(absoluteUrl);
            } catch (RuntimeException e) {
                throw new AmpException(
                        String.format("Unable to get redirected URL. [retry-count=%d]", ctx.getRetryCount()), e);
            }
        });
    }

    protected Optional<String> getRedirectedUrl(String absoluteUrl) {
        ClientResponse clientResponse =
                webClient.get()
                        .uri(absoluteUrl)
                        .exchange()
                        .block(BLOCK_TIMEOUT);
        if (clientResponse == null) {
            return Optional.empty();
        }

        List<String> redirectHeaders = clientResponse.headers().header(HttpHeaders.LOCATION);
        clientResponse.releaseBody();

        if (CollectionUtils.isEmpty(redirectHeaders)) {
            return Optional.empty();
        }

        return Optional.ofNullable(StringUtils.firstNonEmpty(redirectHeaders.get(0)));
    }
}
