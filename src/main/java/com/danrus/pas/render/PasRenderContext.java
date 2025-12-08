package com.danrus.pas.render;

public interface PasRenderContext {
    <T> void putData(T data, String type);
    <T> T getData(Class<T> clazz, String type);
}
