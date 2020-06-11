package moe.pine.mapbot.jsonld.types;

import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@NonFinal
public class Thing {
    public static final String CONTEXT_ATTR = "@context";
    public static final String ID_ATTR = "@id";
    public static final String TYPE_ATTR = "@type";

    String context;
    String id;
    String type;
}
