package me.loza.uhc.commands;

import me.loza.uhc.UHC;
import me.loza.uhc.handlers.prefixHandler;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class healCommand implements CommandExecutor {
    private final UHC plugin;

    public healCommand(UHC plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        prefixHandler pref = new prefixHandler();
        if (args.length > 0) {
            if (!args[0].equalsIgnoreCase("*")) {
                Player targetPlayer = Bukkit.getPlayerExact(args[0]);
                targetPlayer.setHealth(targetPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
                sender.sendMessage(pref.getUHCPrefix() + targetPlayer.getDisplayName() + " has been healed to full!");
                plugin.updateHealth(targetPlayer);
            } else {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
                    plugin.updateHealth(p);
                }
                sender.sendMessage(pref.getUHCPrefix() + "Everyone has been healed to full!");
            }
        } else {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
                player.sendMessage(pref.getUHCPrefix() + "Healed yourself!");
                plugin.updateHealth(player);
            } else {
                sender.sendMessage(pref.getErrorPrefix() + "You can not heal yourself!");
            }
        }
        return true;
    }
}
