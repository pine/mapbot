package moe.pine.mapbot.structured_data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import moe.pine.mapbot.structured_data.types.Thing;
import org.apache.commons.lang3.StringUtils;

import java.io.UncheckedIOException;
import java.util.List;

public class StructuredDataParser {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public StructuredData parse(String structuredData) {
        if (StringUtils.isBlank(structuredData)) {
            return StructuredData.EMPTY;
        }

        try {
            Thing thing = OBJECT_MAPPER.readValue(structuredData, Thing.class);
            return new StructuredData(List.of(thing));
        } catch (JsonProcessingException e1) {
            try {
                List<Thing> things = OBJECT_MAPPER.readValue(structuredData, new TypeReference<>() {
                });
                return new StructuredData(things);
            } catch (JsonProcessingException e2) {
                UncheckedIOException re = new UncheckedIOException(e2);
                re.addSuppressed(e1);
                throw re;
            }
        }
    }
}
