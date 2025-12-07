package com.danrus.pas.impl.features;

import com.danrus.pas.api.info.RenameFeature;
import com.danrus.pas.utils.Rl;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SlimFeature implements RenameFeature {

    public static final ResourceLocation WIDE_ARM_LOGO = Rl.pas("wide");
    public static final ResourceLocation SLIM_ARM_LOGO = Rl.pas("slim");

    private static final String FLAG = "S";
    private boolean slim = false;

    @Override
    public boolean parse(@NotNull String input) {
        if (input.contains(FLAG)) {
            this.slim = true;
            return true;
        }
        return false;
    }

    @Override
    public @NotNull String compile() {
        return slim ? FLAG : "";
    }

    @Override
    public @Nullable Component getDisplayText() {
        return Component.translatable("pas.menu.tab.skin.arm_type");
    }

    @Override
    public @Nullable ResourceLocation getIcon() {
        return slim ? SLIM_ARM_LOGO : WIDE_ARM_LOGO;
    }

    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    public void reset() {
        this.slim = false;
    }

    public boolean isSlim() { return slim; }
    public void setSlim(boolean slim) { this.slim = slim; }
}
