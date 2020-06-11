package moe.pine.mapbot.jsonld.creators;

import com.fasterxml.jackson.databind.JsonNode;
import moe.pine.mapbot.jsonld.factory.Factory;
import moe.pine.mapbot.jsonld.types.Thing;
import org.springframework.lang.Nullable;

import java.util.Optional;

public interface Creator<T extends Thing> {
    Optional<T> create(@Nullable JsonNode node, Factory factory);
}
