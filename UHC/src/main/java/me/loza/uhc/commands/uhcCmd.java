package me.loza.uhc.commands;

import me.loza.uhc.UHC;
import me.loza.uhc.handlers.prefixHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;

public class uhcCmd implements CommandExecutor {
    private final UHC plugin;

    public uhcCmd(UHC plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        prefixHandler pref = new prefixHandler();
        if (!(sender instanceof Player)) {
            sender.sendMessage(pref.getErrorPrefix() + "Only players can use this command!");
            return true;
        }
        Player player = (Player) sender;
        if (label.equalsIgnoreCase("uhc") || label.equalsIgnoreCase("config")) {
            openConfig(player);
        }
        return true;
    }
    private ItemStack createItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            if (lore != null) {
                meta.setLore(Arrays.asList(lore));
            } else {
                meta.setLore(Collections.emptyList());
            }
            item.setItemMeta(meta);
        }
        return item;
    }
    public void openConfig(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, ChatColor.GOLD + "UHC Config");
        inventory.setItem(11, createItem(Material.NETHER_WART, ChatColor.GOLD + "Nether Config",
                ChatColor.DARK_GRAY + "» " + ChatColor.GRAY + "Nether: " + formatStatus(toSnakeCase("Nether")),
                ChatColor.DARK_GRAY + "» " + ChatColor.GRAY + "Potions: " + formatStatus(toSnakeCase("Potions")),
                ChatColor.DARK_GRAY + "» " + ChatColor.GRAY + "Strength Potions: " + formatStatus(toSnakeCase("Strength Potions")),
                ChatColor.DARK_GRAY + "» " + ChatColor.GRAY + "Splash Potions: " + formatStatus(toSnakeCase("Splash Potions")),
                ChatColor.DARK_GRAY + "» " + ChatColor.GRAY + "Tier II: " + formatStatus(toSnakeCase("Tier II"))
        ));

        inventory.setItem(12, createItem(Material.GOLDEN_APPLE, ChatColor.GOLD + "Healing",
                ChatColor.DARK_GRAY + "» " + ChatColor.GRAY + "Golden Heads: " + formatStatus(toSnakeCase("Golden Heads")),
                ChatColor.DARK_GRAY + "» " + ChatColor.GRAY + "Absorption: " + formatStatus(toSnakeCase("Absorption")),
                ChatColor.DARK_GRAY + "» "  + ChatColor.GRAY + "God Apples: " + formatStatus(toSnakeCase("God Apples"))
        ));

        inventory.setItem(13, createItem(Material.IRON_SWORD, ChatColor.GOLD + "Combat",
                ChatColor.DARK_GRAY + "» " + ChatColor.GRAY + "Bow Nerf: " + formatStatus(toSnakeCase("Bow Nerf")),
                ChatColor.DARK_GRAY + "» " + ChatColor.GRAY + "Axe Nerf: " + formatStatus(toSnakeCase("Axe Nerf")),
                ChatColor.DARK_GRAY + "» " + ChatColor.GRAY + "Shields: " + formatStatus(toSnakeCase("Shields")),
                ChatColor.DARK_GRAY + "» "  + ChatColor.GRAY + "Original Melee: " + formatStatus(toSnakeCase("Original Melee")),
                ChatColor.DARK_GRAY + "» "  + ChatColor.GRAY + "Trident Craft: " + formatStatus(toSnakeCase("Trident Craft"))
        ));

        inventory.setItem(14, createItem(Material.BOOK, ChatColor.GOLD + "Enchanting",
                ChatColor.DARK_GRAY + "» " + ChatColor.GRAY + "Villager Trading: " + formatStatus(toSnakeCase("Villager Trading")),
                ChatColor.DARK_GRAY + "» " + ChatColor.GRAY + "Fire Aspect: " + formatStatus(toSnakeCase("Fire Aspect")),
                ChatColor.DARK_GRAY + "» " + ChatColor.GRAY + "Bookshelves: " + formatStatus(toSnakeCase("Bookshelves"))
        ));

        inventory.setItem(15, createItem(Material.ENDER_PEARL, ChatColor.GOLD + "Misc",
                ChatColor.DARK_GRAY + "» " + ChatColor.GRAY + "Apple Rates: " + ChatColor.GREEN + plugin.getConfig().getInt("features.apple_rates") + "%",
                ChatColor.DARK_GRAY + "» " + ChatColor.GRAY + "Flint Rates: " + ChatColor.GREEN + plugin.getConfig().getInt("features.flint_rates") + "%",
                ChatColor.DARK_GRAY + "» " + ChatColor.GRAY + "Pearl Damage: " + formatStatus(toSnakeCase("Pearl Damage")),
                ChatColor.DARK_GRAY + "» " + ChatColor.GRAY + "Horses: " + formatStatus(toSnakeCase("Horses")),
                ChatColor.DARK_GRAY + "» " + ChatColor.GRAY + "Shear String: " + formatStatus(toSnakeCase("Shear String"))
        ));
        player.openInventory(inventory);
    }

    public static String toSnakeCase(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        String trimmedInput = input.trim();
        return trimmedInput.replaceAll(" +", "_").toLowerCase();
    }
    public String formatStatus(String path) {
        if (plugin.getConfig().getBoolean("features." + path)) {
            return ChatColor.GREEN + "Enabled";
        } else {
            return ChatColor.RED + "Disabled";
        }
    }
}
