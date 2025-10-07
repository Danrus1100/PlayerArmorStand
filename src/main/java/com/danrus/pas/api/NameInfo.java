package com.danrus.pas.api;

import com.danrus.pas.managers.PasManager;
import com.danrus.pas.utils.StringUtils;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NameInfo {
    private String base;
    private String params;
    private String overlay;
    private int blend;

    private boolean cape;
    private String capeProvider;
    private String capeId;

    private static final String FLAG_SLIM = "S";

    public NameInfo() { this("", ""); }
    public NameInfo(String base) { this(base, ""); }
    public NameInfo(String base, String params) { this(base, params, "", 100); }
    public NameInfo(String base, String params, String overlay, int blend) {this(base, params, overlay, blend, new CapeInfo());}
    private NameInfo(String base, String params, String overlay, int blend, CapeInfo cape) {
        this.base = base;
        this.params = normalizeParams(params);
        this.overlay = overlay;
        this.blend = clamp(blend, 0, 100);

        this.cape = cape.enabled;
        this.capeProvider = cape.provider;
        this.capeId = cape.id;
    }

    public static NameInfo parse(Component input) {
        if (input != null) {
            return parse(input.getString());
        }
        return new NameInfo();
    }

    public static NameInfo parse(String input) {
        if (input == null) return new NameInfo();
        String[] divided = input.split("\\|", -1);
        if (divided.length == 0 || divided[0].isEmpty()) return new NameInfo();

        String name = divided[0].trim();
        if (name.matches(".*[<>:\"/\\?*].*")) return new NameInfo();

        if (divided.length < 2) return new NameInfo(name);

        String rawParams = divided[1].trim();
        String overlay = "";
        int blend = 100;

        List<String> textureMatch = StringUtils.matchTexture(rawParams);
        if (!textureMatch.get(0).isEmpty()) {
            overlay = textureMatch.get(0);
            blend = safeParseInt(textureMatch.get(1), 100);
            rawParams = textureMatch.get(2).trim();
        }

        List<String> capeMatch = StringUtils.matchCape(rawParams);
        CapeInfo info = new CapeInfo();
        if (capeMatch.get(0).contains("C")) {
            info = new CapeInfo(true, capeMatch.get(1), capeMatch.get(2));
        }
        rawParams = capeMatch.get(3).trim();

        return new NameInfo(name, rawParams, overlay, blend, info);
    }

    public String compile() {
        StringBuilder out = new StringBuilder();
        out.append(base == null ? "" : base);
        if (params != null && !params.isEmpty()) {
            out.append("|").append(params);
        }
        if (overlay != null && !overlay.isEmpty()) {
            if (params == null || params.isEmpty()) out.append("|");
            out.append("T:").append(overlay).append("%").append(blend);
        }
        if (cape) {
            if ((params == null || params.isEmpty()) && overlay.isEmpty()) out.append("|");
            out.append("C");
            if (!capeProvider.isEmpty() && !capeId.isEmpty()) out.append(":").append(capeProvider).append("%").append(capeId);
        }
        return out.toString();
    }


    private static String normalizeParams(String raw) {
        if (raw == null || raw.isEmpty()) return "";
        String p = raw.replaceAll("\\s+", "").toUpperCase();
        List<String> textureMatch = StringUtils.matchTexture(p);
        if (!textureMatch.get(0).isEmpty()) {
            p = textureMatch.get(2).trim().toUpperCase();
        }
        StringBuilder sb = new StringBuilder();
        boolean[] seen = new boolean[256];
        for (int i = 0; i < p.length(); i++) {
            char ch = p.charAt(i);
            if (ch < 256 && !seen[ch]) {
                seen[ch] = true;
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    private static int clamp(int v, int lo, int hi) {
        return Math.max(lo, Math.min(hi, v));
    }
    private static int safeParseInt(String s, int def) {
        try { return Integer.parseInt(s); } catch (Exception e) { return def; }
    }

    // --- API ---

    public boolean isEmpty() { return base == null || base.isEmpty(); }
    public String getDesiredProvider() {
        List<String> providers = PasManager.getInstance().getExistingProviders();
        for (String prov : providers) {
            if (params.contains(prov)) return prov;
        }
        return "M";
    }

    public boolean wantBeSlim() { return params.contains(FLAG_SLIM); }
    public boolean wantCape() { return this.cape; }

    public void setSlim(boolean value) { toggleFlag(FLAG_SLIM, value); }
    public void setCape(boolean value) { this.cape = value; }

    public void setProvider(String literal) {
        List<String> providers = PasManager.getInstance().getExistingProviders();
        String p = params;
        for (String prov : providers) p = p.replace(prov, "");
        p = p + literal;
        params = normalizeParams(p);
    }

    private void toggleFlag(String literal, boolean on) {
        String p = params.replace(literal, "");
        if (on) p = p + literal;
        params = normalizeParams(p);
    }
    
    public void setName(String newName) { this.base = newName == null ? "" : newName; }
    public void setOverlay(String textureName) { this.overlay = textureName == null ? "" : textureName; }
    public void setBlend(int blend) { this.blend = clamp(blend, 0, 100); }
    public void setCapeId(String capeId) {this.capeId = capeId;}
    public void setCapeProvider(String capeProvider) {this.capeProvider = capeProvider;}

    public String base() { return base; }
    public String params() { return params; }
    public String overlay() { return overlay; }
    public int blend() { return blend; }
    public String capeId() {return capeId;}
    public String capeProvider() {return capeProvider;}

    @Override public @NotNull String toString() {
        return "NameInfo[" + this.base + ", " + this.params + "]";
    }

    private record CapeInfo(boolean enabled, String provider, String id) {
        private CapeInfo() {this(false, "", "");}
    }
}

