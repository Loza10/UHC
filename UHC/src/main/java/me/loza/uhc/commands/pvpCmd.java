package me.loza.uhc.commands;

import me.loza.uhc.UHC;
import me.loza.uhc.handlers.prefixHandler;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import java.util.List;

public class pvpCmd implements CommandExecutor {
    private final UHC plugin;

    public pvpCmd(UHC plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        prefixHandler pref = new prefixHandler();

        if (args.length != 1) {
            sender.sendMessage(pref.getUHCPrefix() + "Usage: /pvp on/off");
            return true;
        }
        if (args[0].equalsIgnoreCase("on")) {
            UHC.pvpStatus = true;
            sender.sendMessage(pref.getUHCPrefix() + "PVP has been turned on!");
        } else if (args[0].equalsIgnoreCase("off")) {
            UHC.pvpStatus = false;
            sender.sendMessage(pref.getUHCPrefix() + "PVP has been turned off!");
        } else {
            sender.sendMessage(pref.getErrorPrefix() + "Set PVP to on or off!");
            return true;
        }
        List<World> worldList = Bukkit.getWorlds();
        for (World world : worldList) {
            world.setPVP(UHC.pvpStatus);
        }
        return true;
    }
}
