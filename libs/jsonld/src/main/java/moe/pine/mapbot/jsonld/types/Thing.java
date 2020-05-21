package moe.pine.mapbot.jsonld.types;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(BreadcrumbList.class),
        @JsonSubTypes.Type(DataFeedItem.class),
        @JsonSubTypes.Type(FAQPage.class),
        @JsonSubTypes.Type(ItemList.class),
        @JsonSubTypes.Type(PostalAddress.class),
        @JsonSubTypes.Type(Restaurant.class),
        @JsonSubTypes.Type(ViewAction.class)
})
public class Thing {
}
