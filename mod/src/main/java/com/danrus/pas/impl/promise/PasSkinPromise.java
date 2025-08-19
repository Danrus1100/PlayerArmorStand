package com.danrus.pas.impl.promise;

import com.danrus.pas.api.data.PasSkin;
import com.danrus.pas.api.request.result.Promise;

public class PasSkinPromise implements Promise<PasSkin> {

    private final PasSkin defaultSkin;
    private PasSkin skin;
    private boolean isDone = false;

    public PasSkinPromise() {
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
}
