package com.danrus.pas.render.bb.core;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class BbModelContainer {
    private String name;
    private Map<String, Object> meta;
    private BbResolution resolution;
    private List<BbElement> elements;
    private List<BbOutliner> outliner;
    private List<BbTexture> textures;

    @SerializedName("model_identifier")
    private String modelIdentifier;

    public static BbModelContainer parse(File file) throws IOException {
        String json = new String(java.nio.file.Files.readAllBytes(file.toPath()), java.nio.charset.StandardCharsets.UTF_8);
        return parse(json);
    }

    public static BbModelContainer parse(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, BbModelContainer.class);
    }

    // Getters
    public String getName() { return name; }
    public Map<String, Object> getMeta() { return meta; }
    public BbResolution getResolution() { return resolution; }
    public List<BbElement> getElements() { return elements; }
    public List<BbOutliner> getOutliner() { return outliner; }
    public List<BbTexture> getTextures() { return textures; }
    public String getModelIdentifier() { return modelIdentifier; }

    public class BbResolution {
        private int width;
        private int height;

        public int getWidth() { return width; }
        public int getHeight() { return height; }
    }
}