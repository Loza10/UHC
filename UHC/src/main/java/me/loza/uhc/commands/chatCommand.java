package me.loza.uhc.commands;

import me.loza.uhc.UHC;
import me.loza.uhc.handlers.prefixHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class chatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        prefixHandler pref = new prefixHandler();
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("enable") || args[0].equalsIgnoreCase("true") || args[0].equalsIgnoreCase("on")) {
                    UHC.chat = true;
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.sendMessage(pref.getUHCPrefix() + "Chat has been turned on!");
                    }
                } else if (args[0].equalsIgnoreCase("disable") || args[0].equalsIgnoreCase("false") || args[0].equalsIgnoreCase("off")) {
                    UHC.chat = false;
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.sendMessage(pref.getUHCPrefix() + "Chat has been turned off!");
                    }
                }
            } else {
                player.sendMessage(ChatColor.GOLD + "Correct Usage: ");
                player.sendMessage(ChatColor.DARK_GRAY + "» " + ChatColor.GOLD + "/chat <on/off>");
            }
            return true;
        }
        return false;
    }
}
