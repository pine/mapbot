package moe.pine.mapbot.medium;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import moe.pine.mapbot.jsonld.JsonLd;
import moe.pine.mapbot.jsonld.JsonLdParser;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.UncheckedIOException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class Browser {
    private static final Duration BLOCK_TIMEOUT = Duration.ofSeconds(30L);
    
    private final JsonLdParser jsonLdParser;
    private final WebClient webClient;

    @Value
    @Getter(AccessLevel.PRIVATE)
    static class Context {
        Document document;
    }

    Optional<Context> browse(String absoluteUrl) {
        ClientResponse clientResponse =
                webClient.get()
                        .uri(absoluteUrl)
                        .exchange()
                        .block(BLOCK_TIMEOUT);
        if (clientResponse == null) {
            return Optional.empty();
        }

        Optional<MediaType> mediaTypeOpt = clientResponse.headers().contentType();
        if (mediaTypeOpt.isEmpty()) {
            return Optional.empty();
        }

        MediaType mediaType = mediaTypeOpt.get();
        CensoredMediaType censoredMediaType = new CensoredMediaType(mediaType);
        if (!censoredMediaType.isSupported()) {
            log.debug("Unsupported media type [absoluteUrl={}, media-type={}]", absoluteUrl, censoredMediaType);
            return Optional.empty();
        }

        String content = clientResponse.bodyToMono(String.class).block();
        if (StringUtils.isBlank(content)) {
            return Optional.empty();
        }

        Document document;
        try {
            document = Jsoup.parse(content);
        } catch (UncheckedIOException e) {
            log.debug("Unable to parse HTML [absolute-url={}]", absoluteUrl, e);
            return Optional.empty();
        }

        Context context = new Context(document);
        return Optional.of(context);
    }

    OgpMetadata getOgpMetadata(Context context) {
        String title =
                Optional.ofNullable(context.getDocument().select("meta[property=\"og:title\"]"))
                        .map(v -> v.attr("content"))
                        .orElse(StringUtils.EMPTY);
        return new OgpMetadata(title);
    }

    JsonLd getEmbeddedJsonLd(Context context) {
        List<String> contents =
                context.getDocument()
                        .select("script[type=\"application/ld+json\"]")
                        .stream()
                        .map(Element::html)
                        .filter(StringUtils::isNoneBlank)
                        .collect(Collectors.toUnmodifiableList());

        return jsonLdParser.parse(contents);
    }
}
