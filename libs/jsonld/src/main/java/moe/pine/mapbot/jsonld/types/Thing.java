package moe.pine.mapbot.jsonld.types;

import java.util.List;

public class Thing {
    public static final String TYPE_ATTR = "@type";

    public static List<Class<? extends Thing>> types() {
        return List.of();
    }
}
