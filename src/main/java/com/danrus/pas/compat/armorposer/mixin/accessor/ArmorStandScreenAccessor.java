package com.danrus.pas.compat.armorposer.mixin.accessor;

//? if armorposer {

import com.mrbysco.armorposer.client.gui.ArmorStandScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

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

    @Accessor("oldName")
    String pas$getOldName();

    @Accessor("oldName")
    void pas$setOldName(String name);

    @Accessor("changedName")
    String pas$getChangedName();

    @Accessor("changedName")
    void pas$setChangedName(String name);

    @Accessor("entityArmorStand")
    ArmorStand pas$getEntityArmorStand();

    @Invoker("hasLevels")
    boolean pas$invokeHasLevels();

    @Invoker("updateRenameButton")
    void pas$invokeUpdateRenameButton();
}
//?}
