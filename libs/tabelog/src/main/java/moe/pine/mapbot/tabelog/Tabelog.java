package moe.pine.mapbot.tabelog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import moe.pine.mapbot.place.Place;
import moe.pine.mapbot.tabelog.schema.Restaurant;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;
import java.util.Optional;

@Slf4j
public class Tabelog {
    private static final String URL_PREFIX = "https://tabelog.com/";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private final WebClient webClient;

    public Tabelog(WebClient.Builder webClientBuilder) {
        webClient = webClientBuilder.build();
    }

    public Optional<Place> find(String uri) {
        if (!uri.startsWith(URL_PREFIX)) {
            return Optional.empty();
        }

        String body =
                webClient.get()
                        .uri(uri)
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
                    String.format("Unable to parse JSON-LD data. [uri=%s]", uri), e);
        }
        log.info("A restaurant detected : {}", restaurant);

        Element ogpTitleElement = document.selectFirst("meta[property=\"og:title\"]");
        if (ogpTitleElement == null) {
            throw new RuntimeException();
        }

        String ogpTitle = ogpTitleElement.attr("content");

        if (StringUtils.isEmpty(restaurant.getName())) {
            throw new RuntimeException(
                    String.format("A restaurant name not found. [uri=%s, restaurant=%s]", uri, restaurant));
        }
        if (restaurant.getAddress() == null) {
            throw new RuntimeException(
                    String.format("Address not found. [uri=%s, restaurant=%s]", uri, restaurant));
        }

        String domesticAddress = restaurant.getAddress().getDomesticAddress();
        if (StringUtils.isEmpty(domesticAddress)) {
            throw new RuntimeException(
                    String.format("Address not found. [uri=%s, restaurant=%s]", uri, restaurant));
        }
        log.info("Restaurant address detected : {}", domesticAddress);

        return Optional.of(new Place(restaurant.getName(), ogpTitle, domesticAddress));
    }
}
