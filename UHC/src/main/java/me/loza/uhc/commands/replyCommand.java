package me.loza.uhc.commands;

import me.loza.uhc.handlers.prefixHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.loza.uhc.UHC.userSettings;

public class replyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        prefixHandler pref = new prefixHandler();
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 0) {
                if (userSettings.containsKey(player.getUniqueId()) && userSettings.get(player.getUniqueId()).getLastReply() != null) {
                    Player targetPlayer = userSettings.get(player.getUniqueId()).getLastReply();
                    String message = "";
                    for (int i = 0; i < args.length; i++) {
                        message += args[i] + " ";
                    }
                    targetPlayer.sendMessage(ChatColor.DARK_GRAY + "(" + ChatColor.GOLD + player.getDisplayName() + ChatColor.GRAY + " » " +
                            ChatColor.GOLD + "me" + ChatColor.DARK_GRAY + ")" + ChatColor.GRAY + " " + message);
                    player.sendMessage(ChatColor.DARK_GRAY + "(" + ChatColor.GOLD + "You" + ChatColor.GRAY + " » " +
                            ChatColor.GOLD + targetPlayer.getDisplayName() + ChatColor.DARK_GRAY + ")" + ChatColor.GRAY + " " + message);
                } else {
                    player.sendMessage(pref.getErrorPrefix() + "You have no one to reply to");
                }
            } else {
                player.sendMessage(pref.getErrorPrefix() + "Enter a message to send");
            }
        } else {
            sender.sendMessage(pref.getErrorPrefix() + "Only players can send messages");
        }
        return true;
    }
}
