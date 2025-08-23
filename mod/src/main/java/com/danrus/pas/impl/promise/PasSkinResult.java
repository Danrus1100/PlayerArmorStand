package com.danrus.pas.impl.promise;

import com.danrus.pas.api.data.PasSkin;
import com.danrus.pas.api.request.PasRequest;
import com.danrus.pas.api.request.result.LazyResult;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PasSkinResult implements LazyResult<PasSkin> {

    private final PasSkin defaultSkin;
    private PasSkin skin;
    private boolean isDone = false;

    public PasSkinResult() {
        this.defaultSkin = PasSkin.ARMOR_STAND;
    }

    @Override
    public PasSkin get() {
        return isDone ? skin : defaultSkin;
    }

    @Override
    public void complete(PasSkin value) {

    }

    @Override
    public boolean isDone() {
        return isDone;
    }

    /**
     * Register a task to get skin for the given request.
     *
     * @param request the request to get skin for
     * @param future the future to complete when the skin is retrieved
     * @return UUID of the task in the queue
     */

    public static UUID register(PasRequest request, CompletableFuture<PasSkin> future) {
        UUID taskId = UUID.randomUUID();

        return taskId;
    }
}
