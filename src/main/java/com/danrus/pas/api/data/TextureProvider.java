package com.danrus.pas.api.data;

import com.danrus.pas.api.info.NameInfo;

import java.util.concurrent.CompletableFuture;

public interface TextureProvider {
    CompletableFuture<Void> load(NameInfo info);
    String getLiteral();
}
