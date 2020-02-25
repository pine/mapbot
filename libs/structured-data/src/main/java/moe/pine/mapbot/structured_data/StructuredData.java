package moe.pine.mapbot.structured_data;

import lombok.RequiredArgsConstructor;
import moe.pine.mapbot.structured_data.types.Restaurant;
import moe.pine.mapbot.structured_data.types.Thing;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class StructuredData {
    public static final StructuredData EMPTY = new StructuredData(List.of());

    private final List<Thing> things;

    public Optional<Restaurant> findRestaurant() {
        return things.stream()
                .filter(v -> v instanceof Restaurant)
                .map(v -> (Restaurant) v)
                .findAny();
    }
}
