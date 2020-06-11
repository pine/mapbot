package moe.pine.mapbot.jsonld.creators;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.experimental.UtilityClass;
import moe.pine.mapbot.jsonld.factory.Factory;
import moe.pine.mapbot.jsonld.types.Thing;
import org.springframework.lang.Nullable;

import java.util.Optional;

@UtilityClass
class JsonUtils {
    @Nullable
    public String getText(JsonNode node, String attr) {
        return Optional.ofNullable(node.get(attr))
                .map(JsonNode::asText)
                .orElse(null);
    }

    @Nullable
    public <T extends Thing> T create(JsonNode node, String attr, Class<T> clazz, Factory factory) {
        return factory.getCreator(clazz)
                .flatMap(v -> v.create(node.get(attr), factory))
                .orElse(null);
    }
}
