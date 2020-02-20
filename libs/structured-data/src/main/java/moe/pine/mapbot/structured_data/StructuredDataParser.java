package moe.pine.mapbot.structured_data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import moe.pine.mapbot.structured_data.types.Thing;
import org.apache.commons.lang3.StringUtils;

import java.io.UncheckedIOException;
import java.util.List;

public class StructuredDataParser {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public List<Thing> parse(String structuredData) {
        if (StringUtils.isBlank(structuredData)) {
            return List.of();
        }

        try {
            Thing thing = OBJECT_MAPPER.readValue(structuredData, Thing.class);
            return List.of(thing);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }
}
