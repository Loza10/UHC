package me.loza.uhc.commands;

import me.loza.uhc.handlers.prefixHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class adminCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        prefixHandler pref = new prefixHandler();
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 0) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.isOp()) {
                        p.sendMessage(pref.getAdminPrefix(player) + String.join(" ", args));
                    }
                }
            } else {
                player.sendMessage(pref.getErrorPrefix() + "Specify a message to send to the admin chat!");
            }
            return true;
        }
        return false;
    }
}
