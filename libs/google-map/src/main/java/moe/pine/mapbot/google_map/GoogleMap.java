package moe.pine.mapbot.google_map;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class GoogleMap {
    private static final String SEARCH_URL = "https://www.google.com/maps/search/?api=1&query=";

    public String generateSearchUrl(String query) {
        return SEARCH_URL + URLEncoder.encode(query, StandardCharsets.UTF_8);
    }
}
