package com.danrus.pas.api.request.result;

public interface LazyResult<T> {

    T get();

    void complete(T value);

    boolean isDone();
}
