package com.luka2.comms.http;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpHeaders;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.net.ssl.SSLSession;

public class SimpleClient {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public SimpleClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public <T> HttpResponse<T> get(String url, Class<T> responseType) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> mapResponse(response, responseType))
                .join();
    }

    public <T> HttpResponse<T> post(String url, String requestBody, Class<T> responseType) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(requestBody))
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> mapResponse(response, responseType))
                .join();
    }

    public <T> HttpResponse<T> put(String url, String requestBody, Class<T> responseType) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .PUT(BodyPublishers.ofString(requestBody))
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> mapResponse(response, responseType))
                .join();
    }

    private <T> HttpResponse<T> mapResponse(HttpResponse<String> response, Class<T> responseType) {
        int statusCode = response.statusCode();
        HttpHeaders headers = response.headers();
        T body = null;
        if (response.body() != null && !response.body().isEmpty()) {
            body = deserialize(response.body(), responseType);
        }
        return new CustomHttpResponse<>(statusCode, headers, body);
    }

    private <T> T deserialize(String responseBody, Class<T> responseType) {
        try {
            return objectMapper.readValue(responseBody, responseType);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize response body", e);
        }
    }

    private record CustomHttpResponse<T>(int statusCode, HttpHeaders headers, T body) implements HttpResponse<T> {
        @Override
        public HttpRequest request() {
            throw new UnsupportedOperationException("Not implemented");
        }

        @Override
        public Optional<HttpResponse<T>> previousResponse() {
            throw new UnsupportedOperationException("Not implemented");
        }

        @Override
        public Optional<SSLSession> sslSession() {
            throw new UnsupportedOperationException("Not implemented");
        }

        @Override
        public URI uri() {
            throw new UnsupportedOperationException("Not implemented");
        }

        @Override
        public HttpClient.Version version() {
            throw new UnsupportedOperationException("Not implemented");
        }

        @Override
        public boolean equals(Object obj) {
            throw new UnsupportedOperationException("Not implemented");
        }

        @Override
        public int hashCode() {
            throw new UnsupportedOperationException("Not implemented");
        }

        @Override
        public String toString() {
            throw new UnsupportedOperationException("Not implemented");
        }
    }


}
