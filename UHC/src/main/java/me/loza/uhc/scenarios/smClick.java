package me.loza.uhc.scenarios;

import me.loza.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class smClick implements Listener {
    private final UHC plugin;

    public smClick(UHC plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD + "scenarios")) {
            String itemName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
            toggleStatus(toSnakeCase(itemName));
            event.getWhoClicked().closeInventory();
            plugin.getServer().dispatchCommand(event.getWhoClicked(), "sm");
            event.setCancelled(true);
        } else if (event.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD + "enabled scenarios")) {
            event.setCancelled(true);
        }
    }
    public static String toSnakeCase(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        String trimmedInput = input.trim();
        return trimmedInput.replaceAll(" +", "_").toLowerCase();
    }
    public void toggleStatus(String input) {
        String path = "scenarios." + input;
        if (plugin.getConfig().getBoolean(path)) {
            plugin.getConfig().set(path, false);
        } else {
            plugin.getConfig().set(path, true);
        }
        plugin.saveConfig();
    }
}
