package me.loza.uhc.listeners;

import me.loza.uhc.UHC;
import me.loza.uhc.handlers.prefixHandler;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class interactEvent implements Listener {
    private final UHC plugin;
    prefixHandler pref = new prefixHandler();
    public interactEvent(UHC plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if ((item != null && item.getType() == Material.SPLASH_POTION) && !(plugin.getConfig().getBoolean("features.splash_potions"))) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(pref.getErrorPrefix() + "Splash potions are currently disabled!");
        }
        if ((item != null && item.getType() == Material.SHIELD) && !(plugin.getConfig().getBoolean("features.shields"))) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(pref.getErrorPrefix() + "Shields are currently disabled!");
        }
    }
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if ((event.getRightClicked().getType() == EntityType.VILLAGER) && !(plugin.getConfig().getBoolean("features.villager_trading"))) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(pref.getErrorPrefix() + "Villager trading is currently disabled!");
        }
        if ((event.getRightClicked().getType() == EntityType.HORSE || event.getRightClicked().getType() == EntityType.DONKEY || event.getRightClicked().getType() == EntityType.MULE) && !(plugin.getConfig().getBoolean("features.horses"))) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(pref.getErrorPrefix() + "Horses are currently disabled!");
        }
    }
    @EventHandler
    public void onPlayerInteractBlock(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block clickedBlock = event.getClickedBlock();
            if ((clickedBlock != null && clickedBlock.getType() == Material.BREWING_STAND) && !(plugin.getConfig().getBoolean("features.potions"))) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(pref.getErrorPrefix() + "Potions are currently disabled!");
            }
        }
    }
}
