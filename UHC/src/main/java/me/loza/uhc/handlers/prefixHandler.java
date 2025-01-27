package me.loza.uhc.handlers;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class prefixHandler {
    public String getUHCPrefix() {
        return ChatColor.GOLD + "UHC" + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY;
    }
    public String getErrorPrefix() {
        return ChatColor.RED + "Error: ";
    }
    public String getAdminPrefix(Player p) {
        return ChatColor.DARK_GRAY + "[" + ChatColor.RED + "Admin Chat" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD + p.getDisplayName() + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY;
    }
    public String getOPPrefix() {
        return ChatColor.DARK_GRAY + "(" + ChatColor.GOLD + "OP" + ChatColor.DARK_GRAY + ") ";
    }
    public String getTeamPrefix() {
        return ChatColor.GOLD + "Teams" + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY;
    }
}
