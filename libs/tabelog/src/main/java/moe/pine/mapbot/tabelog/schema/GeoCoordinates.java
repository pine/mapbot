package moe.pine.mapbot.tabelog.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class GeoCoordinates {
    @JsonProperty("@type")
    String type;
    Double latitude;
    Double longitude;
}
