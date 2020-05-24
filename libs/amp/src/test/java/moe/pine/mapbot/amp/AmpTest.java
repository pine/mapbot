package moe.pine.mapbot.amp;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AmpTest {
    private Amp amp;
    private WebClient webClient;

    private MockWebServer mockWebServer;
    private String baseUrl;

    @Before
    public void setUp() {
        webClient = spy(WebClient.create());
        amp = spy(new Amp(webClient));

        mockWebServer = new MockWebServer();
        baseUrl = mockWebServer.url("").toString();
    }

    @After
    public void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    public void resolveOriginalUrlTest_amp() {
        String absoluteUrl = "https://www.google.co.jp/amp/s/s.tabelog.com/tokyo/A1304/A130401/13000809/top_amp/";
        doReturn(Optional.of("redirectedUrl")).when(amp).getRedirectedUrl(absoluteUrl);

        assertEquals(Optional.of("redirectedUrl"), amp.resolveOriginalUrl(absoluteUrl));
    }

    @Test
    public void resolveOriginalUrlTest_nonAmp() {
        assertEquals(Optional.empty(), amp.resolveOriginalUrl("https://www.amazon.co.jp/"));
    }

    @Test
    public void getRedirectedUrlTest() throws Exception {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setResponseCode(HttpStatus.FOUND.value());
        mockResponse.addHeader(HttpHeaders.LOCATION, "redirectedUrl");
        mockWebServer.enqueue(mockResponse);

        assertEquals(Optional.of("redirectedUrl"), amp.getRedirectedUrl(baseUrl));

        RecordedRequest recordedRequest = mockWebServer.takeRequest(10L, TimeUnit.SECONDS);
        assertNotNull(recordedRequest);
    }

    @Test
    public void getRedirectedUrlTest_emptyHeader() throws Exception {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setResponseCode(HttpStatus.FOUND.value());
        mockWebServer.enqueue(mockResponse);

        assertEquals(Optional.empty(), amp.getRedirectedUrl(baseUrl));

        RecordedRequest recordedRequest = mockWebServer.takeRequest(10L, TimeUnit.SECONDS);
        assertNotNull(recordedRequest);
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void getRedirectedUrlTest_nullResponse() {
        RequestHeadersUriSpec requestHeadersUriSpec = mock(RequestHeadersUriSpec.class);
        RequestHeadersSpec requestHeadersSpec = mock(RequestHeadersSpec.class);

        doReturn(requestHeadersUriSpec).when(webClient).get();
        when(requestHeadersUriSpec.uri("absoluteUrl")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.exchange()).thenReturn(Mono.empty());

        assertEquals(Optional.empty(), amp.getRedirectedUrl("absoluteUrl"));
    }
}
