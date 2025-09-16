package com.danrus.pas.api.adapter;

public interface PasTexture {
    void pas$setPixel(int x, int y, int color);
    int pas$getPixel(int x, int y);
    void pas$copyRect(int xFrom, int yFrom, int xToDelta, int yToDelta, int width, int height, boolean mirrorX, boolean mirrorY);
    void pas$copyRect(PasTexture source, int xFrom, int yFrom, int xTo, int yTo, int width, int height, boolean mirrorX, boolean mirrorY);
}
