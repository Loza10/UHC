package me.loza.uhc.listeners;

import me.loza.uhc.UHC;
import me.loza.uhc.handlers.prefixHandler;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class blockPlace implements Listener {
    private final UHC plugin;
    prefixHandler pref = new prefixHandler();
    public blockPlace(UHC plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if ((event.getBlock().getType() == Material.ENCHANTING_TABLE) && !(plugin.getConfig().getBoolean("features.bookshelves"))) {
            Block placedBlock = event.getBlockPlaced();
            for (int x = -5; x <= 5; x++) {
                for (int y = -5; y <= 5; y++) {
                    for (int z = -5; z <= 5; z++) {
                        Block nearbyBlock = placedBlock.getRelative(x, y, z);
                        if (nearbyBlock.getType() == Material.BOOKSHELF) {
                            event.setCancelled(true);
                            event.getPlayer().sendMessage(pref.getErrorPrefix() + "Bookshelves are currently disabled!");
                            return;
                        }
                    }
                }
            }
        } else if ((event.getBlock().getType() == Material.BOOKSHELF) && !(plugin.getConfig().getBoolean("features.bookshelves"))) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(pref.getErrorPrefix() + "Bookshelves are currently disabled!");
        }
    }
}