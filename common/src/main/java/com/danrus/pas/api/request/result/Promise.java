package com.danrus.pas.api.request.result;

public interface Promise<T> {

    T get();

    void complete(T value);

    boolean isDone();
}
