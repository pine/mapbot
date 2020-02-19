package moe.pine.mapbot.tabelog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import moe.pine.mapbot.medium.Medium;
import moe.pine.mapbot.medium.Place;
import moe.pine.mapbot.tabelog.schema.Restaurant;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;
import java.util.Optional;

@Slf4j
public class Tabelog implements Medium {
    private static final String BASE_URL = "https://tabelog.com/";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private final WebClient webClient;
    private final PathResolver pathResolver;

    public Tabelog(WebClient.Builder webClientBuilder) {
        this(webClientBuilder, new PathResolver(webClientBuilder));
    }

    Tabelog(
            WebClient.Builder webClientBuilder,
            PathResolver pathResolver
    ) {
        webClient = webClientBuilder.baseUrl(BASE_URL).build();
        this.pathResolver = pathResolver;
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
        Element jsonLdElement = document.selectFirst("script[type=\"application/ld+json\"]");
        String jsonLdData = jsonLdElement.html();

        Restaurant restaurant;
        try {
            restaurant = OBJECT_MAPPER.readValue(jsonLdData, Restaurant.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(
                    String.format("Unable to parse JSON-LD data. [uri=%s]", absoluteUrl), e);
        }
        log.info("A restaurant detected : {}", restaurant);

        Element ogpTitleElement = document.selectFirst("meta[property=\"og:title\"]");
        if (ogpTitleElement == null) {
            throw new RuntimeException();
        }

        String ogpTitle = ogpTitleElement.attr("content");

        if (StringUtils.isEmpty(restaurant.getName())) {
            throw new RuntimeException(
                    String.format("A restaurant name not found. [uri=%s, restaurant=%s]", absoluteUrl, restaurant));
        }
        if (restaurant.getAddress() == null) {
            throw new RuntimeException(
                    String.format("Address not found. [uri=%s, restaurant=%s]", absoluteUrl, restaurant));
        }

        String domesticAddress = restaurant.getAddress().getDomesticAddress();
        if (StringUtils.isEmpty(domesticAddress)) {
            throw new RuntimeException(
                    String.format("Address not found. [uri=%s, restaurant=%s]", absoluteUrl, restaurant));
        }
        log.info("Restaurant address detected : {}", domesticAddress);

        return Optional.of(new Place(restaurant.getName(), ogpTitle, domesticAddress));
    }
}
