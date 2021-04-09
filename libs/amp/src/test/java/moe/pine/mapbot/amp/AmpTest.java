package moe.pine.mapbot.amp;

import moe.pine.mapbot.retry_support.RetryTemplateFactory;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AmpTest {
    private Amp amp;
    private WebClient webClient;

    private MockWebServer mockWebServer;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        webClient = spy(WebClient.create());
        amp = spy(new Amp(webClient));

        mockWebServer = new MockWebServer();
        baseUrl = mockWebServer.url("").toString();
    }

    @AfterEach
    void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    void constructorTest() {
        Amp amp = new Amp(webClient);
        RetryTemplate retryTemplate = (RetryTemplate) ReflectionTestUtils.getField(amp, "retryTemplate");

        assertNotNull(retryTemplate);
    }

    @Test
    void resolveOriginalUrlTest_amp() {
        String absoluteUrl = "https://www.google.co.jp/amp/s/s.tabelog.com/tokyo/A1304/A130401/13000809/top_amp/";
        doReturn(Optional.of("redirectedUrl")).when(amp).getRedirectedUrl(absoluteUrl);

        assertEquals(Optional.of("redirectedUrl"), amp.resolveOriginalUrl(absoluteUrl));
    }

    @Test
    void resolveOriginalUrlTest_nonAmp() {
        assertEquals(Optional.empty(), amp.resolveOriginalUrl("https://www.amazon.co.jp/"));
    }

    @Test
    void resolveOriginalUrlTest_maxRetry() {
        String absoluteUrl = "https://www.google.co.jp/amp/s/s.tabelog.com/tokyo/A1304/A130401/13000809/top_amp/";
        RetryTemplate retryTemplate = RetryTemplateFactory.create(5, 0L, 1.0, RuntimeException.class);
        Amp amp = spy(new Amp(webClient, retryTemplate));
        Throwable e1 = new RuntimeException();

        doThrow(e1).when(amp).getRedirectedUrl(anyString());

        try {
            amp.resolveOriginalUrl(absoluteUrl);
            fail();
        } catch (RuntimeException e2) {
            assertTrue(e2 instanceof AmpException);
            assertEquals("Unable to get redirected URL. [retry-count=4]", e2.getMessage());
            assertSame(e1, e2.getCause());
        }

        verify(amp, times(5)).getRedirectedUrl(absoluteUrl);
    }

    @Test
    void getRedirectedUrlTest() throws Exception {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setResponseCode(HttpStatus.FOUND.value());
        mockResponse.addHeader(HttpHeaders.LOCATION, "redirectedUrl");
        mockWebServer.enqueue(mockResponse);

        assertEquals(Optional.of("redirectedUrl"), amp.getRedirectedUrl(baseUrl));

        RecordedRequest recordedRequest = mockWebServer.takeRequest(10L, TimeUnit.SECONDS);
        assertNotNull(recordedRequest);
    }

    @Test
    void getRedirectedUrlTest_emptyHeader() throws Exception {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setResponseCode(HttpStatus.FOUND.value());
        mockWebServer.enqueue(mockResponse);

        assertEquals(Optional.empty(), amp.getRedirectedUrl(baseUrl));

        RecordedRequest recordedRequest = mockWebServer.takeRequest(10L, TimeUnit.SECONDS);
        assertNotNull(recordedRequest);
    }

    @Test
    @SuppressWarnings("rawtypes")
    void getRedirectedUrlTest_nullResponse() {
        RequestHeadersUriSpec requestHeadersUriSpec = mock(RequestHeadersUriSpec.class);
        RequestHeadersSpec requestHeadersSpec = mock(RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        doReturn(requestHeadersUriSpec).when(webClient).get();
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toBodilessEntity()).thenReturn(Mono.empty());

        assertEquals(Optional.empty(), amp.getRedirectedUrl("absoluteUrl"));

        verify(requestHeadersUriSpec).uri("absoluteUrl");
    }
}
