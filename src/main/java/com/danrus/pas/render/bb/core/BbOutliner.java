package com.danrus.pas.render.bb.core;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;

public class BbOutliner {
    public String name;
    public String uuid;
    public List<Double> origin;
    public int color;
    public boolean export;

    @SerializedName("mirror_uv")
    public boolean mirrorUv;

    @SerializedName("isOpen")
    public boolean isOpen;

    public boolean locked;
    public boolean visibility;
    public int autouv;
    public boolean selected;
    public List<Object> children; // Can be String (uuid) or BbOutliner

    @SuppressWarnings("unchecked")
    public static BbOutliner fromMap(Map<String, Object> map) {
        BbOutliner outliner = new BbOutliner();

        if (map.containsKey("name")) {
            outliner.name = (String) map.get("name");
        }
        if (map.containsKey("uuid")) {
            outliner.uuid = (String) map.get("uuid");
        }
        if (map.containsKey("origin")) {
            outliner.origin = (List<Double>) map.get("origin");
        }
        if (map.containsKey("children")) {
            outliner.children = (List<Object>) map.get("children");
        }
        if (map.containsKey("color")) {
            outliner.color = ((Number) map.get("color")).intValue();
        }
        if (map.containsKey("export")) {
            outliner.export = (Boolean) map.get("export");
        }
        if (map.containsKey("mirror_uv")) {
            outliner.mirrorUv = (Boolean) map.get("mirror_uv");
        }
        if (map.containsKey("isOpen")) {
            outliner.isOpen = (Boolean) map.get("isOpen");
        }
        if (map.containsKey("locked")) {
            outliner.locked = (Boolean) map.get("locked");
        }
        if (map.containsKey("visibility")) {
            outliner.visibility = (Boolean) map.get("visibility");
        }
        if (map.containsKey("autouv")) {
            outliner.autouv = ((Number) map.get("autouv")).intValue();
        }
        if (map.containsKey("selected")) {
            outliner.selected = (Boolean) map.get("selected");
        }

        return outliner;
    }
}