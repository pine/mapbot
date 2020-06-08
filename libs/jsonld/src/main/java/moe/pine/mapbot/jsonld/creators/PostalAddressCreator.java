package moe.pine.mapbot.jsonld.creators;

import com.fasterxml.jackson.databind.JsonNode;
import moe.pine.mapbot.jsonld.factory.Factory;
import moe.pine.mapbot.jsonld.types.PostalAddress;

import java.util.Optional;

public class PostalAddressCreator implements Creator<PostalAddress> {
    @Override
    public Optional<PostalAddress> create(JsonNode node, Factory factory) {
        return Optional.empty();
    }
}
