package moe.pine.mapbot;

import moe.pine.heroku.addons.HerokuRedis;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AppInitializerTest {
    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void runTest() {
        HerokuRedis redis = mock(HerokuRedis.class);
        when(redis.getHost()).thenReturn("host");
        when(redis.getPassword()).thenReturn("password");
        when(redis.getPort()).thenReturn(12345);

        try (MockedStatic<HerokuRedis> mockedStatic = Mockito.mockStatic(HerokuRedis.class)) {
            mockedStatic.when(HerokuRedis::get).thenReturn(redis);
            System.clearProperty("spring.redis.host");
            System.clearProperty("spring.redis.password");
            System.clearProperty("spring.redis.port");

            AppInitializer.run();

            assertEquals("host", System.getProperty("spring.redis.host"));
            assertEquals("password", System.getProperty("spring.redis.password"));
            assertEquals("12345", System.getProperty("spring.redis.port"));
        }
    }
}
