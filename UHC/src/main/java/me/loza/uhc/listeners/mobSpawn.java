package me.loza.uhc.listeners;

import me.loza.uhc.UHC;
import me.loza.uhc.handlers.prefixHandler;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class mobSpawn implements Listener {
    private final UHC plugin;
    public mobSpawn(UHC plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onSpawn(EntitySpawnEvent event) {
        EntityType entityType = event.getEntityType();
        if ((entityType == EntityType.DONKEY || entityType == EntityType.HORSE || entityType == EntityType.MULE || entityType == EntityType.SKELETON_HORSE || entityType == EntityType.CAMEL) && !(plugin.getConfig().getBoolean("features.horses"))) {
            event.setCancelled(true);
            event.getLocation().getWorld().spawnEntity(event.getLocation(), EntityType.COW);
        }
    }
}
