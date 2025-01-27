package me.loza.uhc.commands;

import me.loza.uhc.handlers.playerHandler;
import me.loza.uhc.handlers.prefixHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.loza.uhc.UHC.userSettings;

public class messageCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        prefixHandler pref = new prefixHandler();
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                player.sendMessage(pref.getErrorPrefix() + "Enter a message to send");
            } else if (args.length > 1) {
                Player targetPlayer = Bukkit.getPlayerExact(args[0]);
                if (targetPlayer == null) {
                    player.sendMessage(pref.getErrorPrefix() + "No player is online with that name");
                    return true;
                } else {
                    String message = "";
                    for (int i = 1; i < args.length; i++) {
                        message += args[i] + " ";
                    }
                    targetPlayer.sendMessage(ChatColor.DARK_GRAY + "(" + ChatColor.GOLD + player.getDisplayName() + ChatColor.GRAY + " » " +
                            ChatColor.GOLD + "me" + ChatColor.DARK_GRAY + ")" + ChatColor.GRAY + " " + message);
                    player.sendMessage(ChatColor.DARK_GRAY + "(" + ChatColor.GOLD + "You" + ChatColor.GRAY + " » " +
                            ChatColor.GOLD + targetPlayer.getDisplayName() + ChatColor.DARK_GRAY + ")" + ChatColor.GRAY + " " + message);

                    updateSettings(player, targetPlayer);
                }
            } else {
                player.sendMessage(pref.getErrorPrefix() + "Specify a player to message");
            }
        } else {
            sender.sendMessage(pref.getErrorPrefix() + "Only players can send messages");
        }
        return true;
    }
    void updateSettings(Player player, Player targetPlayer) {
        if (userSettings.containsKey(player.getUniqueId())) {
            userSettings.get(player.getUniqueId()).setLastReply(targetPlayer);
        } else {
            userSettings.put(player.getUniqueId(), new playerHandler(targetPlayer));
        }

        if (userSettings.containsKey(targetPlayer.getUniqueId())) {
            userSettings.get(targetPlayer.getUniqueId()).setLastReply(player);
        } else {
            userSettings.put(targetPlayer.getUniqueId(), new playerHandler(player));
        }
    }
}
