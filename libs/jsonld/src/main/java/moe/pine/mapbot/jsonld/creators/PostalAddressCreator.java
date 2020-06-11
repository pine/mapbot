package moe.pine.mapbot.jsonld.creators;

import com.fasterxml.jackson.databind.JsonNode;
import moe.pine.mapbot.jsonld.factory.Factory;
import moe.pine.mapbot.jsonld.types.PostalAddress;

import java.util.Optional;

public class PostalAddressCreator extends AbstractCreator<PostalAddress> {
    @Override
    protected String getType() {
        return PostalAddress.TYPE;
    }

    @Override
    public Optional<PostalAddress> onCreate(JsonNode node, Factory factory) {
        return Optional.of(
                new PostalAddress(
                        JsonUtils.getText(node, PostalAddress.CONTEXT_ATTR),
                        JsonUtils.getText(node, PostalAddress.ID_ATTR),
                        JsonUtils.getText(node, PostalAddress.ADDRESS_LOCALITY_ATTR),
                        JsonUtils.getText(node, PostalAddress.ADDRESS_REGION_ATTR),
                        JsonUtils.getText(node, PostalAddress.STREET_ADDRESS_ATTR)));
    }
}
