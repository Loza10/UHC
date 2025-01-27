package me.loza.uhc.commands;

import me.loza.uhc.UHC;
import me.loza.uhc.handlers.prefixHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class pmcCmd implements CommandExecutor {
    private final UHC plugin;
    private final prefixHandler pref = new prefixHandler();
    Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
    public pmcCmd(UHC plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(pref.getErrorPrefix() + "Only players can use this command!");
            return true;
        }
        Player player = (Player) sender;
        if (scoreboard.getEntityTeam(player) == null) {
            player.sendMessage(pref.getErrorPrefix() + "You must be on a team to use this command!");
            return true;
        }
        Set<String> filteredEntries = scoreboard.getEntityTeam(player).getEntries();
        for (String s : filteredEntries) {
            if (Bukkit.getPlayer(s) != null) {
                Bukkit.getPlayer(s).sendMessage(pref.getTeamPrefix() + scoreboard.getEntityTeam(player).getColor() + player.getName() + ChatColor.GRAY + " X: " + ChatColor.YELLOW + (Math.round(player.getLocation().getX() * 100.0) / 100.0) + ChatColor.GRAY + " Y: " + ChatColor.YELLOW + (Math.round(player.getLocation().getY() * 100.0) / 100.0) + ChatColor.GRAY + " Z: " + ChatColor.YELLOW + (Math.round(player.getLocation().getZ() * 100.0) / 100.0));
            }
        }
        return true;
    }
}
