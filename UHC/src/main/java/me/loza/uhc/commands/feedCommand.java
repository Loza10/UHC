package me.loza.uhc.commands;

import me.loza.uhc.handlers.prefixHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class feedCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        prefixHandler pref = new prefixHandler();
        if (args.length > 0) {
            if (!args[0].equalsIgnoreCase("*")) {
                Player targetPlayer = Bukkit.getPlayerExact(args[0]);
                targetPlayer.setFoodLevel(20);
                sender.sendMessage(pref.getUHCPrefix() + targetPlayer.getDisplayName() + " has been fed!");
            } else {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.setFoodLevel(20);
                }
                sender.sendMessage(pref.getUHCPrefix() + "Everyone has been fed!");
            }
        } else {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.setFoodLevel(20);
                player.sendMessage(pref.getUHCPrefix() + "Fed yourself!");
            } else {
                sender.sendMessage(pref.getErrorPrefix() + "You can not feed yourself!");
            }
        }
        return true;
    }
}
