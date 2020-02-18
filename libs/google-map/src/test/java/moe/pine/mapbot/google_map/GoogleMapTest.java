package moe.pine.mapbot.google_map;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertEquals;

public class GoogleMapTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @InjectMocks
    private GoogleMap googleMap;

    @Test
    public void generateSearchUrlTest() {
        assertEquals("https://www.google.com/maps/search/?api=1&query=%E3%81%82%E3%81%84%E3%81%86%E3%81%88%E3%81%8A",
                googleMap.generateSearchUrl("あいうえお"));
    }
}
