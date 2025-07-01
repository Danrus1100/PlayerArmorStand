package com.danrus.api;

import com.danrus.utils.RestHelper;

import java.util.concurrent.CompletableFuture;

public class NamemcApi {
    public static CompletableFuture<String> getSkinUrl(String id) {
//        String url = "https://s.namemc.com/i/" + id + ".png";
//        return RestHelper.get(url);
        return CompletableFuture.completedFuture("https://s.namemc.com/i/" + id + ".png");
    }
}
