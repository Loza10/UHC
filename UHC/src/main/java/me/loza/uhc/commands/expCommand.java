package me.loza.uhc.commands;

import me.loza.uhc.handlers.prefixHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class expCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        prefixHandler pref = new prefixHandler();
        if (args.length > 0) {
            try {
                int level = Integer.parseInt(args[0]);
                if (level < 0) {
                    sender.sendMessage(pref.getErrorPrefix() + "Xp level must be at least 0!");
                    return true;
                }
                if (args.length < 2) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.setLevel(level);
                        p.setExp(0);
                    }
                    sender.sendMessage(pref.getUHCPrefix()+ "Set everyone's xp level to: " + ChatColor.GOLD + level);
                } else {
                    Player targetPlayer = Bukkit.getPlayerExact(args[1]);
                    if (targetPlayer == null) {
                        sender.sendMessage(pref.getErrorPrefix() + "No player is online with that name");
                        return true;
                    } else {
                        targetPlayer.setLevel(level);
                        targetPlayer.setExp(0);
                        sender.sendMessage(pref.getUHCPrefix() + "Set " + targetPlayer.getDisplayName() + " xp level to: " + ChatColor.GOLD + level);
                    }
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(pref.getErrorPrefix() + "Enter a valid number!");
            }
        } else {
            sender.sendMessage(pref.getUHCPrefix() + "Correct Usage:");
            sender.sendMessage(ChatColor.GOLD + "/xp <number> <player>");
            sender.sendMessage(ChatColor.GRAY + "NOTE: Leave '<player>' blank to change everyone's xp");
        }
        return true;
    }
}
