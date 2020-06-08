package moe.pine.mapbot.jsonld.creators;

import com.fasterxml.jackson.databind.JsonNode;
import moe.pine.mapbot.jsonld.factory.Factory;
import moe.pine.mapbot.jsonld.types.PostalAddress;
import moe.pine.mapbot.jsonld.types.Restaurant;

import java.util.Optional;

public class RestaurantCreator implements Creator<Restaurant> {
    @Override
    public String getType() {
        return Restaurant.TYPE;
    }

    @Override
    public Optional<Restaurant> onCreate(JsonNode node, Factory factory) {
        if (node.isEmpty()) {
            return Optional.empty();
        }

        new Restaurant(
                "",
                "",
                node.get(Restaurant.NAME_ATTR).asText(),
                factory.getCreator(PostalAddress.class)
                        .flatMap(v -> v.create(node.get(Restaurant.ADDRESS_ATTR), factory))
                        .orElse(null));
        return Optional.empty();
    }
}
