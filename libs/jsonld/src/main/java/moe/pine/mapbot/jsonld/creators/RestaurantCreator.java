package moe.pine.mapbot.jsonld.creators;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import moe.pine.mapbot.jsonld.factory.Factory;
import moe.pine.mapbot.jsonld.types.PostalAddress;
import moe.pine.mapbot.jsonld.types.Restaurant;

import java.util.Optional;

@Slf4j
public class RestaurantCreator extends AbstractCreator<Restaurant> {
    @Override
    public String getType() {
        return Restaurant.TYPE;
    }

    @Override
    public Optional<Restaurant> onCreate(JsonNode node, Factory factory) {
        return Optional.of(
                new Restaurant(
                        JsonUtils.getText(node, Restaurant.CONTEXT_ATTR),
                        JsonUtils.getText(node, Restaurant.ID_ATTR),
                        JsonUtils.getText(node, Restaurant.NAME_ATTR),
                        JsonUtils.create(node, Restaurant.ADDRESS_ATTR, PostalAddress.class, factory)));
    }
}
