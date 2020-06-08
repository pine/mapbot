package moe.pine.mapbot.jsonld.types;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * A restaurant
 *
 * @see <a href="https://schema.org/Restaurant">Restaurant - schema.org Type</a>
 */
@Value
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("Restaurant")
public class Restaurant extends Thing {
    public static final String TYPE = "Restaurant";
    public static final String ADDRESS_ATTR = "address";
    public static final String NAME_ATTR = "name";

    @JsonProperty("@context")
    String context;
    @JsonProperty("@id")
    String id;
    String name;
    PostalAddress address;
}
