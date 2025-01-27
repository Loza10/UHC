package me.loza.uhc.commands;

import me.loza.uhc.handlers.prefixHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class clearCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        prefixHandler pref = new prefixHandler();
        if (args.length > 0) {
            if (!args[0].equalsIgnoreCase("*")) {
                Player targetPlayer = Bukkit.getPlayerExact(args[0]);
                targetPlayer.getInventory().clear();
                sender.sendMessage(pref.getUHCPrefix() + targetPlayer.getDisplayName() + "'s inventory has been cleared!");
            } else {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.getInventory().clear();
                }
                sender.sendMessage(pref.getUHCPrefix() + "Everyone's inventory has been cleared!");
            }
        } else {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.getInventory().clear();
                player.sendMessage(pref.getUHCPrefix() + "Your inventory has been cleared!");
            } else {
                sender.sendMessage(pref.getErrorPrefix() + "You can not clear your inventory!");
            }
        }
        return true;
    }
}
