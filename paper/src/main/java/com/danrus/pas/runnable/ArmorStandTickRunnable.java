package com.danrus.pas.runnable;

import com.danrus.pas.api.PasApi;
import com.danrus.pas.api.event.common.PasTickEvent;
import com.danrus.pas.api.types.McSide;
import com.danrus.pas.impl.ArmorStandAdapterPaperImpl;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;

public class ArmorStandTickRunnable extends BukkitRunnable {
    @Override
    public void run() {
        for (World world : Bukkit.getWorlds()) {
            for (ArmorStand armorStand : world.getEntitiesByClass(ArmorStand.class)) {
                if (!armorStand.getName().isEmpty()) {
                    PasApi.postEvent(new PasTickEvent(new ArmorStandAdapterPaperImpl(armorStand), McSide.SERVER));
                }
            }
        }
    }
}
