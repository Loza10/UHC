package me.loza.uhc.listeners;

import me.loza.uhc.UHC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class portalEvent implements Listener {
    private final UHC plugin;
    public portalEvent(UHC plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onPortalEnter(PlayerTeleportEvent event) {
        if ((event.getCause().equals(PlayerTeleportEvent.TeleportCause.NETHER_PORTAL)) && !(plugin.getConfig().getBoolean("features.nether"))) {
            event.setCancelled(true);
        }
    }
}
