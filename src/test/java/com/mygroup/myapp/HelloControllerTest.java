package com.mygroup.myapp;

import io.micronaut.context.annotation.Property;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientException;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest
@Property(name = "ratelimit.enabled", value = "True")
@Property(name = "ratelimit.max-allowed", value = "1")
@Property(name = "ratelimit.period", value = "10s")
@Property(name = "ratelimit.reset-interval", value = "15s")
public class HelloControllerTest {

    @Inject
    @Client("/")
    RxHttpClient client;

    @Test
    void testGreet() throws HttpClientException {
        HttpRequest<Object> httpRequest = HttpRequest.GET("/api/hello/test");
        HttpResponse<String> response = client.toBlocking().exchange("/api/hello/test", String.class);
        assertNotNull(response);
        assertEquals("Hello test", response.body());

        HttpClientException httpClientException = Assertions.assertThrows(HttpClientException.class,
                () -> client.toBlocking().exchange("/api/hello/test", String.class));
        HttpStatus errorCode = ((HttpClientResponseException) httpClientException).getResponse().getStatus();
        assertEquals(HttpStatus.TOO_MANY_REQUESTS, errorCode);
    }
}
