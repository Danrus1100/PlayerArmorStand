package com.danrus.pas.compat.armorposer.mixin.accessor;

import com.mrbysco.armorposer.client.gui.ArmorStandScreen;
import net.minecraft.client.gui.components.Button;
import org.spongepowered.asm.mixin.Mixin;
import com.mrbysco.armorposer.client.gui.widgets.NameBox;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ArmorStandScreen.class)
public interface ArmorStandScreenAccessor {
    @Accessor("nameField")
    NameBox pas$getNameInput();

    @Accessor("renameButton")
    Button pas$getRenameButton();
}
