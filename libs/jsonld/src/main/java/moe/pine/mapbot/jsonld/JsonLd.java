package moe.pine.mapbot.jsonld;

import lombok.RequiredArgsConstructor;
import moe.pine.mapbot.jsonld.types.Thing;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class JsonLd {
    private final List<Thing> things;

    public <T> Optional<T> findAny(Class<T> clazz) {
        return things.stream()
                .filter(v -> clazz.isAssignableFrom(v.getClass()))
                .map(clazz::cast)
                .findAny();
    }
}
