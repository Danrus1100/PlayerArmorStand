package com.danrus.pas;

import com.danrus.pas.listener.paper.ArmorStandListener;
import com.danrus.pas.runnable.ArmorStandTickRunnable;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerArmorStandsPlugin extends JavaPlugin {

    private final ArmorStandTickRunnable armorStandTickRunnable;

    public PlayerArmorStandsPlugin() {
        this.armorStandTickRunnable = new ArmorStandTickRunnable();
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Player Armor Stands Plugin has been enabled.");

        Bukkit.getPluginManager().registerEvents(new ArmorStandListener(), this);

        this.armorStandTickRunnable.runTaskTimer(this, 0L, 1L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Player Armor Stands Plugin has been disabled.");
        this.armorStandTickRunnable.cancel();
    }
}
