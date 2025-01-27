package me.loza.uhc.commands;

import me.loza.uhc.UHC;
import me.loza.uhc.handlers.prefixHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.UUID;

public class whitelistCommand implements CommandExecutor {
    private final UHC plugin;

    public whitelistCommand(UHC plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        prefixHandler pref = new prefixHandler();
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;
        if (args.length == 0) {
            sendUsage(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "add":
                if (args.length > 1) {
                    addToWhitelist(player, pref, args[1]);
                } else {
                    player.sendMessage(pref.getErrorPrefix() + "Please specify a player name.");
                }
                break;
            case "remove":
                if (args.length > 1) {
                    removeFromWhitelist(player, pref, args[1]);
                } else {
                    player.sendMessage(pref.getErrorPrefix() + "Please specify a player name.");
                }
                break;
            case "list":
                sendWhitelist(player, pref);
                break;
            case "all":
                addAllOnlinePlayers(player, pref);
                break;
            case "clear":
                resetWhitelist(player, pref);
                break;
            case "on":
                setWhitelistEnabled(player, pref, true);
                break;
            case "off":
                setWhitelistEnabled(player, pref, false);
                break;
            default:
                sendUsage(player);
        }

        return true;
    }

    private void addToWhitelist(Player player, prefixHandler pref, String name) {
        UUID uuid = Bukkit.getOfflinePlayer(name).getUniqueId();
        if (uuid == null) {
            player.sendMessage(pref.getErrorPrefix() + "Player not found.");
            return;
        }

        String path = "whitelist." + name.toLowerCase();
        if (plugin.getConfig().contains(path)) {
            player.sendMessage(pref.getErrorPrefix() + "That player is already whitelisted!");
            return;
        }

        plugin.getConfig().set(path, uuid.toString());
        plugin.saveConfig();
        player.sendMessage(pref.getUHCPrefix() + "Added " + ChatColor.GOLD + name + ChatColor.GRAY + " to the whitelist.");
    }

    private void removeFromWhitelist(Player player, prefixHandler pref, String name) {
        String path = "whitelist." + name.toLowerCase();
        if (!plugin.getConfig().contains(path)) {
            player.sendMessage(pref.getErrorPrefix() + "That player is not whitelisted!");
            return;
        }

        plugin.getConfig().set(path, null);
        plugin.saveConfig();
        player.sendMessage(pref.getUHCPrefix() + "Removed " + ChatColor.GOLD + name + ChatColor.GRAY + " from the whitelist.");
    }

    private void sendWhitelist(Player player, prefixHandler pref) {
        ConfigurationSection whitelistSection = plugin.getConfig().getConfigurationSection("whitelist");

        if (whitelistSection == null || whitelistSection.getKeys(false).isEmpty()) {
            player.sendMessage(pref.getErrorPrefix() + "There is no one whitelisted.");
            return;
        }

        StringBuilder whitelistList = new StringBuilder(pref.getUHCPrefix() + "Whitelist: ");
        for (String name : whitelistSection.getKeys(false)) {
            if (name.equalsIgnoreCase("enabled")) {
                continue;
            }
            whitelistList.append(ChatColor.GOLD).append(name).append(ChatColor.GRAY).append(", ");
        }

        if (whitelistList.length() > pref.getUHCPrefix().length() + "Whitelist: ".length()) {
            whitelistList.setLength(whitelistList.length() - 2);
        }

        player.sendMessage(whitelistList.toString());
    }

    private void addAllOnlinePlayers(Player player, prefixHandler pref) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            addToWhitelist(player, pref, onlinePlayer.getName());
        }
        player.sendMessage(pref.getUHCPrefix() + "Added all online players to the whitelist.");
    }

    private void resetWhitelist(Player player, prefixHandler pref) {
        ConfigurationSection whitelistSection = plugin.getConfig().getConfigurationSection("whitelist");
        if (whitelistSection != null) {
            for (String name : whitelistSection.getKeys(false)) {
                if (name.equalsIgnoreCase("enabled")) {
                    continue;
                }
                plugin.getConfig().set("whitelist." + name.toLowerCase(), null);
            }
            plugin.saveConfig();
        }
        player.sendMessage(pref.getUHCPrefix() + "Cleared the whitelist.");
    }

    private void setWhitelistEnabled(Player player, prefixHandler pref, boolean enabled) {
        plugin.getConfig().set("whitelist.enabled", enabled);
        plugin.saveConfig();
        player.sendMessage(pref.getUHCPrefix() + "Whitelist is now " + (enabled ? "enabled!" : "disabled!"));
    }

    private void sendUsage(Player player) {
        player.sendMessage(ChatColor.GOLD + "Correct Usage: ");
        player.sendMessage(ChatColor.DARK_GRAY + "» " + ChatColor.GOLD + "/wl [add/remove] <player>");
        player.sendMessage(ChatColor.DARK_GRAY + "» " + ChatColor.GOLD + "/wl list");
        player.sendMessage(ChatColor.DARK_GRAY + "» " + ChatColor.GOLD + "/wl all");
        player.sendMessage(ChatColor.DARK_GRAY + "» " + ChatColor.GOLD + "/wl clear");
        player.sendMessage(ChatColor.DARK_GRAY + "» " + ChatColor.GOLD + "/wl on");
        player.sendMessage(ChatColor.DARK_GRAY + "» " + ChatColor.GOLD + "/wl off");
    }
}
