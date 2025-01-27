package me.loza.uhc.scenarios;

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

public class smCmd implements CommandExecutor {
    private final prefixHandler pref = new prefixHandler();
    private final UHC plugin;

    public smCmd(UHC plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(pref.getErrorPrefix() + "Only players can execute this command!");
            return true;
        }
        Player player = (Player) sender;
        Inventory inventory = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Scenarios");
        inventory.setItem(0, createItem(Material.IRON_ORE, ChatColor.GOLD + "Cutclean",
                ChatColor.DARK_GRAY + "» " + formatStatus(toSnakeCase("cutclean"))
        ));
        player.openInventory(inventory);
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
    public static String toSnakeCase(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        String trimmedInput = input.trim();
        return trimmedInput.replaceAll(" +", "_").toLowerCase();
    }
    public String formatStatus(String path) {
        if (plugin.getConfig().getBoolean("scenarios." + path)) {
            return ChatColor.GREEN + "Enabled";
        } else {
            return ChatColor.RED + "Disabled";
        }
    }
}
