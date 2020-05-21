package moe.pine.mapbot.medium;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import moe.pine.mapbot.jsonld.JsonLd;
import moe.pine.mapbot.jsonld.JsonLdParser;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class Browser {
    private final JsonLdParser jsonLdParser;
    private final WebClient webClient;

    @Value
    @Getter(AccessLevel.PRIVATE)
    static class Context {
        Document document;
    }

    Optional<Context> browse(String absoluteUrl) {
        String content = webClient.get()
                .uri(absoluteUrl)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        if (StringUtils.isBlank(content)) {
            return Optional.empty();
        }

        Document document = Jsoup.parse(content);
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
                        .collect(Collectors.toUnmodifiableList());

        return jsonLdParser.parse(contents);
    }
}
