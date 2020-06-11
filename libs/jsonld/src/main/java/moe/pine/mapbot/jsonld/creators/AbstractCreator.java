package moe.pine.mapbot.jsonld.creators;

import com.fasterxml.jackson.databind.JsonNode;
import moe.pine.mapbot.jsonld.factory.Factory;
import moe.pine.mapbot.jsonld.types.Thing;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

import java.util.Optional;

abstract class AbstractCreator<T extends Thing> implements Creator<T> {
    abstract protected String getType();

    abstract protected Optional<T> onCreate(JsonNode node, Factory factory);

    @Override
    public Optional<T> create(@Nullable JsonNode node, Factory factory) {
        if (node == null) {
            return Optional.empty();
        }
        if (node.isEmpty()) {
            return Optional.empty();
        }

        String type = JsonUtils.getText(node, Thing.TYPE_ATTR);
        if (StringUtils.equals(getType(), type)) {
            return onCreate(node, factory);
        }

        return Optional.empty();
    }
}
