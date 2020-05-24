package moe.pine.mapbot;

import lombok.SneakyThrows;
import moe.pine.heroku.addons.HerokuRedis;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(HerokuRedis.class)
public class AppInitializerTest {
    @Test
    @SneakyThrows
    public void runTest() {
        HerokuRedis redis = mock(HerokuRedis.class);
        when(redis.getHost()).thenReturn("host");
        when(redis.getPassword()).thenReturn("password");
        when(redis.getPort()).thenReturn(12345);

        mockStatic(HerokuRedis.class);
        when(HerokuRedis.get()).thenReturn(redis);

        System.clearProperty("spring.redis.host");
        System.clearProperty("spring.redis.password");
        System.clearProperty("spring.redis.port");

        AppInitializer.run();

        assertEquals("host", System.getProperty("spring.redis.host"));
        assertEquals("password", System.getProperty("spring.redis.password"));
        assertEquals("12345", System.getProperty("spring.redis.port"));
    }
}
