package moe.pine.mapbot.structured_data.types;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(BreadcrumbList.class),
        @JsonSubTypes.Type(PostalAddress.class),
        @JsonSubTypes.Type(Restaurant.class)
})
public class Thing {
}
