package me.loza.uhc.commands;

import me.loza.uhc.UHC;
import me.loza.uhc.handlers.prefixHandler;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class killboardCmd implements CommandExecutor {
    private final UHC plugin;
    private Scoreboard scoreboard;

    prefixHandler pref = new prefixHandler();

    public killboardCmd(UHC plugin) {
        this.plugin = plugin;
        this.scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("on")) {

            if (scoreboard.getObjective("Kills") == null) {
                scoreboard.registerNewObjective("Kills", Criteria.TOTAL_KILL_COUNT, Component.text(ChatColor.GOLD + "UHC"));
                scoreboard.getObjective("Kills").setDisplaySlot(DisplaySlot.SIDEBAR);
            }

            Score pveScore = scoreboard.getObjective("Kills").getScore(ChatColor.GREEN + "" + ChatColor.ITALIC + "PVE");
            pveScore.setScore(0);

            sender.sendMessage(pref.getUHCPrefix() + "Killboard has been enabled.");
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("off")) {
            if (scoreboard.getObjective("Kills") == null) {
                sender.sendMessage(pref.getErrorPrefix() + "Killboard is not currently enabled.");
                return true;
            }
            scoreboard.getObjective("Kills").unregister();
            sender.sendMessage(pref.getUHCPrefix() + "Killboard has been disabled.");
            return true;
        }

        sender.sendMessage(pref.getErrorPrefix() + "Usage: /kb on/off");
        return true;
    }
}
