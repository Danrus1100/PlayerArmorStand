package com.danrus.pas.api.reg;

import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.api.data.DataHolder;
import com.danrus.pas.api.info.InfoTranslator;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.utils.Rl;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InfoTranslators {
    private static final InfoTranslators INSTANCE = new InfoTranslators();
    private final HashMap<Class<? extends DataHolder>, List<InfoTranslator>> registry = new HashMap();

    public <T extends InfoTranslator> void register(Class<? extends DataHolder> clazz, T resolver) {
        registry.computeIfAbsent(clazz, k -> new ArrayList<>()).add(resolver);
    }

    public ResourceLocation toResourceLocation(Class<? extends DataHolder> type, NameInfo info) {
        List<InfoTranslator> list = this.registry.get(type);

        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("No translators registered for type: " + type);
        }

        for (InfoTranslator translator : list) {
            try {
                if (translator.isApplicable(info)) {
                    ResourceLocation location = translator.toResourceLocation(info);
                    if (location != null) {
                        return location;
                    }
                }
            } catch (Exception e) {
                PlayerArmorStandsClient.LOGGER.warn("Translator {} failed for {}",
                        translator.getClass().getSimpleName(), info, e);
            }
        }

        return Rl.vanilla("textures/entity/player/wide/steve.png");
    }


    public String toFileName(Class<? extends DataHolder> type, NameInfo info) {
        List<InfoTranslator> list = this.registry.get(type);

        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("No translators registered for type: " + type);
        }

        for (InfoTranslator translator : list) {
            try {
                if (translator.isApplicable(info)) {
                    String fileName = translator.toFileName(info);
                    if (fileName != null) {
                        return fileName;
                    }
                }
            } catch (Exception e) {
                PlayerArmorStandsClient.LOGGER.warn("Translator {} failed for {}",
                        translator.getClass().getSimpleName(), info, e);
            }
        }

        if (!list.isEmpty()) {
            PlayerArmorStandsClient.LOGGER.warn("No applicable translator for {}, using fallback", info);
            return list.get(0).toFileName(info);
        }

        throw new IllegalArgumentException("No applicable InfoTransformer found for NameInfo: " + info);
    }


    private InfoTranslators() {
    }
    public static InfoTranslators getInstance() {
        return INSTANCE;
    }
}
