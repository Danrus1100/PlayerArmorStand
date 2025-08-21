package com.danrus.pas.render.bb.core;

import com.google.gson.annotations.SerializedName;

public class BbTexture {
    public String path;
    public String name;
    public String folder;
    public String namespace;
    public String id;
    public String group;
    public int width;
    public int height;

    @SerializedName("uv_width")
    public int uvWidth;

    @SerializedName("uv_height")
    public int uvHeight;

    public boolean particle;

    @SerializedName("use_as_default")
    public boolean useAsDefault;

    public String source;
    public String uuid;

    @SerializedName("relative_path")
    public String relativePath;
}