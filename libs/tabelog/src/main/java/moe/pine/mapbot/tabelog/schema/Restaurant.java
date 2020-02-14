package moe.pine.mapbot.tabelog.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

/**
 * A restaurant
 *
 * @see <a href="https://schema.org/Restaurant">Restaurant - schema.org Type</a>
 */
@Value
public class Restaurant {
    @JsonProperty("@context")
    String context;
    @JsonProperty("@type")
    String type;
    @JsonProperty("@id")
    String id;
    String name;
    String image;
    PostalAddress address;
    GeoCoordinates geo;
    String telephone;
    String priceRange;
    String servesCuisine;
}
