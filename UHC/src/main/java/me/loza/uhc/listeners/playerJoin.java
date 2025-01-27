package me.loza.uhc.listeners;

import me.loza.uhc.UHC;
import me.loza.uhc.handlers.prefixHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class playerJoin implements Listener {
    private final UHC plugin;
    prefixHandler pref = new prefixHandler();
    public playerJoin(UHC plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (plugin.getConfig().getBoolean("features.original_melee")) {
            Player player = event.getPlayer();
            player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(100);
        } else {
            Player player = event.getPlayer();
            player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);
        }
        if (event.getPlayer().isOp()) {
            event.setJoinMessage(ChatColor.GOLD + event.getPlayer().getDisplayName() + ChatColor.DARK_GRAY + " (" + ChatColor.GREEN + "»" + ChatColor.DARK_GRAY + ")");
        } else {
            event.setJoinMessage(ChatColor.GRAY + event.getPlayer().getDisplayName() + ChatColor.DARK_GRAY + " (" + ChatColor.GREEN + "»" + ChatColor.DARK_GRAY + ")");
        }
        plugin.updateHealth(event.getPlayer());
    }
    @EventHandler
    public void onPlayerConnect(AsyncPlayerPreLoginEvent event) {
        if (plugin.getConfig().getBoolean("whitelist.enabled", false)) {
            UUID playerUUID = event.getUniqueId();
            String playerName = event.getName();

            if (!isPlayerWhitelisted(playerUUID) && !Bukkit.getOfflinePlayer(playerUUID).isOp()) {
                event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                event.setKickMessage("You are not whitelisted!");
            }
        }
    }
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if (event.getPlayer().isOp()) {
            event.setQuitMessage(ChatColor.GOLD + event.getPlayer().getDisplayName() + ChatColor.DARK_GRAY + " (" + ChatColor.RED + "«" + ChatColor.DARK_GRAY + ")");
        } else {
            event.setQuitMessage(ChatColor.GRAY + event.getPlayer().getDisplayName() + ChatColor.DARK_GRAY + " (" + ChatColor.RED + "«" + ChatColor.DARK_GRAY + ")");
        }
    }
    private boolean isPlayerWhitelisted(UUID uuid) {
        ConfigurationSection whitelistSection = plugin.getConfig().getConfigurationSection("whitelist");

        if (whitelistSection == null) {
            return false;
        }

        for (String playerName : whitelistSection.getKeys(false)) {
            String storedUUID = whitelistSection.getString(playerName);
            if (uuid.toString().equalsIgnoreCase(storedUUID)) {
                return true;
            }
        }

        return false;
    }
}
