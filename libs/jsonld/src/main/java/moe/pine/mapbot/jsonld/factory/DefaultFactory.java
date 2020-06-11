package moe.pine.mapbot.jsonld.factory;

import moe.pine.mapbot.jsonld.creators.Creator;
import moe.pine.mapbot.jsonld.creators.PostalAddressCreator;
import moe.pine.mapbot.jsonld.creators.RestaurantCreator;
import moe.pine.mapbot.jsonld.types.PostalAddress;
import moe.pine.mapbot.jsonld.types.Restaurant;
import moe.pine.mapbot.jsonld.types.Thing;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class DefaultFactory implements Factory {
    private static Map<Class<? extends Thing>, Creator<? extends Thing>> CREATORS =
            Map.of(
                    PostalAddress.class, new PostalAddressCreator(),
                    Restaurant.class, new RestaurantCreator()
            );

    @Override
    public Collection<Creator<? extends Thing>> getCreators() {
        return CREATORS.values();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Thing> Optional<Creator<T>> getCreator(Class<T> clazz) {
        Creator<T> creator = (Creator<T>) CREATORS.get(clazz);
        return Optional.ofNullable(creator);
    }
}

