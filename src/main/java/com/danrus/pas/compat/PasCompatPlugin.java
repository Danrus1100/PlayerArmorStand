package com.danrus.pas.compat;

import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public abstract class PasCompatPlugin implements IMixinConfigPlugin {

    private boolean anounced = false;
    private Logger logger = LoggerFactory.getLogger("PAS Compatibility");

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName){
        if (!anounced) {
            anounced = true;
            if (shouldApply()) {
                logger.info("[Player Armor Stands] Compatibility for {} are detected.", getModName());
            }
        }
        return shouldApply();
    }

    @Override
    public String getRefMapperConfig() {
        return "";
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return List.of();
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    protected abstract boolean shouldApply();
    protected abstract String getModName();
}
