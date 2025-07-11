package com.danrus.utils;

import java.net.URI;
import java.net.http.*;
import java.util.concurrent.CompletableFuture;

public class RestHelper {
    private static final HttpClient client = HttpClient.newHttpClient();

    public static CompletableFuture<String> get(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .exceptionally(e -> {
                    e.printStackTrace();
                    return null;
                });
    }
}
