package moe.pine.mapbot.jsonld.creators;

import com.fasterxml.jackson.databind.JsonNode;
import moe.pine.mapbot.jsonld.factory.Factory;
import moe.pine.mapbot.jsonld.types.Thing;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public interface Creator<T> {
    String getType();

    Optional<T> onCreate(JsonNode node, Factory factory);

    default Optional<T> create(JsonNode node, Factory factory) {
        if (node.isEmpty()) {
            return Optional.empty();
        }

        String type = node.get(Thing.TYPE_ATTR).asText();
        if (StringUtils.equals(getType(), type)) {
            return onCreate(node, factory);
        }

        return Optional.empty();
    }
}
