package moe.pine.mapbot.google_map;

import org.springframework.web.util.UriComponentsBuilder;

public class GoogleMap {
    private static final String SEARCH_URL = "https://www.google.com/maps/search/?api=1";

    public String generateSearchUrl(String query) {
        return UriComponentsBuilder.fromUriString(SEARCH_URL)
            .queryParam("query", query)
            .build()
            .encode()
            .toUriString();
    }
}
