package me.loza.uhc.listeners;

import me.loza.uhc.UHC;
import me.loza.uhc.handlers.prefixHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class craftItem implements Listener {
    private final UHC plugin;
    prefixHandler pref = new prefixHandler();
    public craftItem(UHC plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onPrepare(PrepareItemCraftEvent event) {
        CraftingInventory inventory = event.getInventory();

        ItemStack result = inventory.getResult();

        if (result != null && result.getType() == Material.ARROW && result.getAmount() == 4) {
            result.setAmount(8);
            inventory.setResult(result);
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if ((event.getRecipe().getResult().getType() == Material.BREWING_STAND) && !(plugin.getConfig().getBoolean("features.potions"))) {
            event.setCancelled(true);
            if (event.getWhoClicked() instanceof Player) {
                event.getWhoClicked().sendMessage(pref.getErrorPrefix() + "Potions are currently disabled!");
            }
        } else if ((event.getRecipe().getResult().getType() == Material.BLAZE_POWDER) && !(plugin.getConfig().getBoolean("features.strength_potions"))) {
            event.setCancelled(true);
            if (event.getWhoClicked() instanceof Player) {
                event.getWhoClicked().sendMessage(pref.getErrorPrefix() + "Strength potions are currently disabled!");
            }
        } else if (event.getRecipe().getResult().getType() == Material.CRAFTER) {
            event.setCancelled(true);
        } else if ((event.getRecipe().getResult().getType() == Material.SHIELD) && !(plugin.getConfig().getBoolean("features.shields"))) {
            event.setCancelled(true);
            if (event.getWhoClicked() instanceof Player) {
                event.getWhoClicked().sendMessage(pref.getErrorPrefix() + "Shields are currently disabled!");
            }
        }
    }
}
