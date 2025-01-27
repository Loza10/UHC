package me.loza.uhc.listeners;

import me.loza.uhc.UHC;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class playerMove implements Listener {
    private final UHC plugin;

    public playerMove(UHC plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (plugin.getConfig().getBoolean("frozen") && (event.getPlayer().getGameMode() == GameMode.SURVIVAL || event.getPlayer().getGameMode() == GameMode.ADVENTURE)) {
            Vector from = event.getFrom().toVector();
            Vector to = event.getTo().toVector();

            if (!from.equals(to)) {
                double deltaX = to.getX() - from.getX();
                double deltaY = to.getY() - from.getY();
                double deltaZ = to.getZ() - from.getZ();

                if (Math.abs(deltaX) > 0 || Math.abs(deltaZ) > 0 || deltaY > 0) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
