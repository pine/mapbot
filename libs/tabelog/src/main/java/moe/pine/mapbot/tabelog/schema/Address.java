package moe.pine.mapbot.tabelog.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class Address {
    @JsonProperty("@type")
    String type;
    String streetAddress;
    String addressLocality;
    String addressRegion;
    String postalCode;
    String addressCountry;
}
