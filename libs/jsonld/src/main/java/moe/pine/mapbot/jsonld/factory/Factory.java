package moe.pine.mapbot.jsonld.factory;

import moe.pine.mapbot.jsonld.creators.Creator;
import moe.pine.mapbot.jsonld.types.Thing;

import java.util.Collection;
import java.util.Optional;

public interface Factory {
    Collection<Creator<? extends Thing>> getCreators();

    <T extends Thing> Optional<Creator<T>> getCreator(Class<T> clazz);
}
