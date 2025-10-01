package com.danrus.pas.compat.armorposer.mixin.accessor;

//? if armorposer {

import com.mrbysco.armorposer.client.gui.ArmorStandScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ArmorStandScreen.class)
public interface ArmorStandScreenAccessor {
    @Accessor("nameField")
    //? if <=1.21.5 {
    com.mrbysco.armorposer.client.gui.widgets.NameBox pas$getNameInput();
    //?} else {
    /*EditBox pas$getNameInput();
    *///?}

    @Accessor("renameButton")
    Button pas$getRenameButton();
}
//?}
