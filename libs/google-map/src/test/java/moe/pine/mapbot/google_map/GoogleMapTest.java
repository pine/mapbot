package moe.pine.mapbot.google_map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class GoogleMapTest {
    @InjectMocks
    private GoogleMap googleMap;

    @Test
    void generateSearchUrlTest() {
        assertEquals("https://www.google.com/maps/search/?api=1&query=%E3%81%82%E3%81%84%E3%81%86%E3%81%88%E3%81%8A",
                googleMap.generateSearchUrl("あいうえお"));
        assertEquals("https://www.google.com/maps/search/?api=1&query=%28abc%29",
                googleMap.generateSearchUrl("(abc)"));
    }
}
