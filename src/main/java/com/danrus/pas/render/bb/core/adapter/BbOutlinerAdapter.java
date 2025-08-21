package com.danrus.pas.render.bb.core.adapter;

import com.danrus.pas.render.bb.core.BbOutliner;
import com.google.gson.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BbOutlinerAdapter implements JsonDeserializer<BbOutliner> {
    @Override
    public BbOutliner deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        BbOutliner outliner = new BbOutliner();

        if (obj.has("name")) outliner.name = obj.get("name").getAsString();
        if (obj.has("uuid")) outliner.uuid = obj.get("uuid").getAsString();
        if (obj.has("origin")) {
            outliner.origin = ctx.deserialize(obj.get("origin"), List.class);
        }
        if (obj.has("color")) outliner.color = obj.get("color").getAsInt();
        if (obj.has("export")) outliner.export = obj.get("export").getAsBoolean();
        if (obj.has("mirror_uv")) outliner.mirrorUv = obj.get("mirror_uv").getAsBoolean();
        if (obj.has("isOpen")) outliner.isOpen = obj.get("isOpen").getAsBoolean();
        if (obj.has("locked")) outliner.locked = obj.get("locked").getAsBoolean();
        if (obj.has("visibility")) outliner.visibility = obj.get("visibility").getAsBoolean();
        if (obj.has("autouv")) outliner.autouv = obj.get("autouv").getAsInt();
        if (obj.has("selected")) outliner.selected = obj.get("selected").getAsBoolean();

        // --- children ---
        if (obj.has("children")) {
            JsonArray arr = obj.getAsJsonArray("children");
            List<Object> children = new ArrayList<>();
            for (JsonElement el : arr) {
                if (el.isJsonPrimitive() && el.getAsJsonPrimitive().isString()) {
                    children.add(el.getAsString()); // UUID
                } else if (el.isJsonObject()) {
                    children.add(ctx.deserialize(el, BbOutliner.class)); // nested outliner
                }
            }
            outliner.children = children;
        }

        return outliner;
    }
}
