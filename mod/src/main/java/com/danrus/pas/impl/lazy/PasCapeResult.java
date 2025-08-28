package com.danrus.pas.impl.lazy;

import com.danrus.pas.api.request.result.LazyResult;

public class PasCapeResult implements LazyResult<> {
    @Override
    public Object get() {
        return null;
    }

    @Override
    public void complete(Object value) {

    }

    @Override
    public boolean isDone() {
        return false;
    }
}
