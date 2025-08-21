package com.danrus.pas.render.bb.core;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;

public class BbElement {
    public String name;
    public String type;
    public String uuid;

    @SerializedName("box_uv")
    public boolean boxUv;

    public List<Double> from;
    public List<Double> to;
    public List<Double> origin;
    public Map<String, BbFace> faces;
    public boolean rescale;
    public boolean locked;

    @SerializedName("light_emission")
    public int lightEmission;

    @SerializedName("render_order")
    public String renderOrder;

    @SerializedName("allow_mirror_modeling")
    public boolean allowMirrorModeling;

    public int autouv;
    public int color;
}