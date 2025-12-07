package com.danrus.pas.render.tooltip;

import com.danrus.pas.api.info.NameInfo;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public class PasTooltip implements TooltipComponent {
    private NameInfo info;

    public PasTooltip(ItemStack stack) {
        this.info = NameInfo.parse(stack.getCustomName());
    }

    public NameInfo getNameInfo() {
        return info;
    }
}
