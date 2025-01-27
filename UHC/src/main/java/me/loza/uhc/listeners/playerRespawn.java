package me.loza.uhc.listeners;

import me.loza.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class playerRespawn implements Listener {
    private final UHC plugin;
    public playerRespawn(UHC plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            plugin.updateHealth(event.getPlayer());
        });
    }
}
