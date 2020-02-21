package moe.pine.mapbot.tabelog;

import lombok.extern.slf4j.Slf4j;
import moe.pine.mapbot.medium.Medium;
import moe.pine.mapbot.medium.Place;
import moe.pine.mapbot.structured_data.StructuredDataParser;
import moe.pine.mapbot.structured_data.types.Restaurant;
import moe.pine.mapbot.structured_data.types.Thing;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class Tabelog implements Medium {
    private static final String BASE_URL = "https://tabelog.com/";

    private final WebClient webClient;
    private final PathResolver pathResolver;
    private final StructuredDataParser structuredDataParser;

    public Tabelog(
            WebClient.Builder webClientBuilder,
            StructuredDataParser structuredDataParser
    ) {
        this(webClientBuilder, structuredDataParser, new PathResolver(webClientBuilder));
    }

    Tabelog(
            WebClient.Builder webClientBuilder,
            StructuredDataParser structuredDataParser,
            PathResolver pathResolver
    ) {
        webClient = webClientBuilder.baseUrl(BASE_URL).build();
        this.structuredDataParser = Objects.requireNonNull(structuredDataParser);
        this.pathResolver = Objects.requireNonNull(pathResolver);
    }

    @Override
    public Optional<Place> find(String absoluteUrl) {
        Optional<String> pathOpt = pathResolver.resolve(absoluteUrl);
        if (pathOpt.isEmpty()) {
            return Optional.empty();
        }

        String path = pathOpt.get();
        String body =
                webClient.get()
                        .uri(path)
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
        Objects.requireNonNull(body);

        Document document = Jsoup.parse(body);
        String structuredData =
                Optional.ofNullable(document.selectFirst("script[type=\"application/ld+json\"]"))
                        .map(Element::html)
                        .orElse(StringUtils.EMPTY);
        if (StringUtils.isEmpty(structuredData)) {
            log.warn("Structured data not found. [absolute-url={}. path={}]", absoluteUrl, path);
            return Optional.empty();
        }

        List<Thing> things = structuredDataParser.parse(structuredData);
        Optional<Restaurant> restaurantOpt =
                things.stream()
                        .filter(v -> v instanceof Restaurant)
                        .map(v -> (Restaurant) v)
                        .findAny();
        if (restaurantOpt.isEmpty()) {
            log.warn("Unable to find any restaurant in structured data. [absolute-url={}, path={}, structured-data={}]",
                    absoluteUrl, path, structuredData);
            return Optional.empty();
        }

        Restaurant restaurant = restaurantOpt.get();
        log.info("A restaurant detected : {}", restaurant);

        if (StringUtils.isEmpty(restaurant.getName())) {
            log.warn("A restaurant name not found. [absolute-url={}, path={}, restaurant={}]", absoluteUrl, path, restaurant);
            return Optional.empty();
        }
        if (restaurant.getAddress() == null) {
            log.warn("Address not found. [absolute-url={}, path={}, restaurant={}]", absoluteUrl, path, restaurant);
            return Optional.empty();
        }

        String domesticAddress = restaurant.getAddress().getDomesticAddress();
        if (StringUtils.isEmpty(domesticAddress)) {
            log.warn("Address not found. [absolute-url={}, restaurant={}]", absoluteUrl, restaurant);
            return Optional.empty();
        }
        log.info("Restaurant address detected : {}", domesticAddress);

        String ogpTitle = Optional.ofNullable(document.selectFirst("meta[property=\"og:title\"]"))
                .map(v -> v.attr("content"))
                .orElse(StringUtils.EMPTY);
        if (StringUtils.isEmpty(ogpTitle)) {
            log.warn("OGP title not found. [absolute-url={}, path={}]", absoluteUrl, path);
            return Optional.empty();
        }

        return Optional.of(new Place(restaurant.getName(), ogpTitle, domesticAddress));
    }
}
