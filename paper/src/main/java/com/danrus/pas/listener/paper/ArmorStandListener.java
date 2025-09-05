package com.danrus.pas.listener.paper;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ArmorStandListener implements Listener {
    @EventHandler
    public void onArmorStandInteract(org.bukkit.event.player.PlayerArmorStandManipulateEvent event) {
        // Handle armor stand interaction events here
        // For example, you can log the interaction or modify the armor stand properties
        System.out.println("Armor Stand interacted with by: " + event.getPlayer().getName());
    }
}
