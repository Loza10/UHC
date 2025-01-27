package me.loza.uhc.listeners;

import me.loza.uhc.UHC;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class chestEvent implements Listener {
    private final UHC plugin;
    public chestEvent(UHC plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void chestOpen(InventoryOpenEvent event) {
        if ((event.getInventory().getHolder() instanceof org.bukkit.block.Chest) && !(plugin.getConfig().getBoolean("features.god_apples"))) {
            Inventory chestInventory = event.getInventory();
            for (int i = 0; i < chestInventory.getSize(); i++) {
                ItemStack item = chestInventory.getItem(i);
                if (item != null && item.getType() == Material.ENCHANTED_GOLDEN_APPLE) {
                    chestInventory.setItem(i, new ItemStack(Material.GOLDEN_APPLE, item.getAmount()));
                }
            }
        }
    }
}
