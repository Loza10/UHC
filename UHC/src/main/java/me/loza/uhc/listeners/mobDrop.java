package me.loza.uhc.listeners;

import me.loza.uhc.UHC;
import me.loza.uhc.handlers.prefixHandler;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class mobDrop implements Listener {
    private final UHC plugin;
    prefixHandler pref = new prefixHandler();
    public mobDrop(UHC plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (!(plugin.getConfig().getBoolean("features.tier_ii"))) {
            event.getDrops().removeIf(item -> item.getType() == Material.GLOWSTONE_DUST);
        }
    }
}