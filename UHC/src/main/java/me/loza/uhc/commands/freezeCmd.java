package me.loza.uhc.commands;

import me.loza.uhc.UHC;
import me.loza.uhc.handlers.prefixHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class freezeCmd implements CommandExecutor {
    private final UHC plugin;
    private final prefixHandler pref = new prefixHandler();
    public freezeCmd(UHC plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if ((plugin.getConfig().getBoolean("frozen"))) {
            plugin.getConfig().set("frozen", false);
            sender.sendMessage(pref.getUHCPrefix() + "Players are now unfrozen!");
        } else {
            plugin.getConfig().set("frozen", true);
            sender.sendMessage(pref.getUHCPrefix() + "Players are now frozen!");
        }
        plugin.saveConfig();
        return true;
    }
}
