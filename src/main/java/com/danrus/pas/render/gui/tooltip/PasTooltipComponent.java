package com.danrus.pas.render.gui.tooltip;

import com.danrus.pas.utils.StringUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class PasTooltipComponent implements TooltipComponent {
    private final boolean noName;
    private final String name;
    private final String params;
    private final Component displayName;
    private final Component fullName;

    public PasTooltipComponent(ItemStack stack) {
        Component customName = stack.getCustomName();
        if (customName != null) {
            List<String> matches = StringUtils.matchASName(customName.getString());
            name = matches.get(0);
            params = matches.get(1);
            noName = false;
            displayName = Component.literal(name);
            fullName = customName;
        } else {
            name =
            params = "";
            noName = true;
            displayName = Component.translatable("item.minecraft.armor_stand");
            fullName = displayName;
        }
    }

    public PasTooltipComponent(String string, boolean noName) {
        List<String> matches = StringUtils.matchASName(string);
        name = matches.get(0);
        params = matches.get(1);
        displayName = Component.literal(name);
        fullName = Component.literal(string);
        this.noName = noName;
    }

    public String name() {
        return name;
    }

    public String params() {
        return params;
    }

    public boolean noName() {
        return noName;
    }

    public Component displayName() {
        return displayName;
    }

    public Component fullName() {
        return fullName;
    }
}
