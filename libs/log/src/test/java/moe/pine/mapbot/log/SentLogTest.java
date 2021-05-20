package moe.pine.mapbot.log;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SentLogTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Test
    void serializeTest() throws Exception {
        SentLog sentLog = new SentLog("channel", "sourceTs", "destinationTs");
        SentLog deserializedSentLog =
                OBJECT_MAPPER.readValue(OBJECT_MAPPER.writeValueAsString(sentLog), SentLog.class);

        assertEquals(sentLog, deserializedSentLog);
    }
}
