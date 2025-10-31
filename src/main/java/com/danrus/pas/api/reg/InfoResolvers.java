package com.danrus.pas.api.reg;

import com.danrus.pas.api.DataHolder;
import com.danrus.pas.api.InfoResolver;
import com.danrus.pas.api.NameInfo;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InfoResolvers {
    private static final InfoResolvers INSTANCE = new InfoResolvers();
    private final HashMap<Class<? extends DataHolder>, List<InfoResolver>> registry = new HashMap();

    public <T extends InfoResolver> void registerTransformer(Class<? extends DataHolder> clazz, T resolver) {
        registry.computeIfAbsent(clazz, k -> new ArrayList<>()).add(resolver);
    }

    public ResourceLocation toResourceLocation(Class<? extends DataHolder> type, NameInfo info) {
        List<InfoResolver> list = this.registry.get(type);
        for (InfoResolver transformer : list) {
            if (transformer.isApplicable(info)) {
                ResourceLocation location = transformer.toResourceLocation(info);
                if (location != null) {
                    return location;
                }
            }
        }
        throw new IllegalArgumentException("No applicable InfoTransformer found for NameInfo: " + info);
    }

    public String toFileName(Class<? extends DataHolder> type, NameInfo info) {
        List<InfoResolver> list = this.registry.get(type);
        for (InfoResolver transformer : list) {
            if (transformer.isApplicable(info)) {
                String fileName = transformer.toFileName(info);
                if (fileName != null) {
                    return fileName;
                }
            }
        }
        throw new IllegalArgumentException("No applicable InfoTransformer found for NameInfo: " + info);
    }

    private InfoResolvers() {
    }
    public static InfoResolvers getInstance() {
        return INSTANCE;
    }
}
