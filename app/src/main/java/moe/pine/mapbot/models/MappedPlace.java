package moe.pine.mapbot.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class MappedPlace {
    String name;
    String label;
    String address;
    String mapUrl;
}
