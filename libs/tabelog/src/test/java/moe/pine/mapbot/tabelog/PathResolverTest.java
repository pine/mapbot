package moe.pine.mapbot.tabelog;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PathResolverTest {
    private PathResolver pathResolver;

    @Before
    public void setUp() {
        WebClient.Builder webClientBuilder = mock(WebClient.Builder.class);
        when(webClientBuilder.build()).thenReturn(mock(WebClient.class));

        pathResolver = new PathResolver(webClientBuilder);
    }

    @Test
    public void resolveTest_officialUrl() {
        assertEquals(Optional.of("/tokyo/A1304/A130401/13000809/"),
                pathResolver.resolve("https://tabelog.com/tokyo/A1304/A130401/13000809/"));
    }
}
