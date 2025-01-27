package me.loza.uhc.commands;

import me.loza.uhc.UHC;
import me.loza.uhc.handlers.prefixHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class startCmd implements CommandExecutor {
    private final UHC plugin;
    private final prefixHandler pref = new prefixHandler();

    public startCmd(UHC plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(pref.getErrorPrefix() + "Only players can use this command.");
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage(pref.getUHCPrefix() + "Usage: /start <pvp> <meetup>");
            return true;
        }

        int pvpTime;
        int meetupTime;

        try {
            pvpTime = Integer.parseInt(args[0]);
            meetupTime = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(pref.getErrorPrefix() + "Both arguments must be integers representing minutes.");
            return true;
        }

        int pvpSeconds = pvpTime * 60;
        int meetupSeconds = meetupTime * 60;
        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "heal *");
        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "feed *");
        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "ci *");
        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "xp 0");
        new BukkitRunnable() {
            int countdown = 3;

            @Override
            public void run() {
                if (countdown > 0) {
                    Bukkit.broadcastMessage(pref.getUHCPrefix() + "Starting in " + countdown + "...");
                    playSoundToAll(Sound.BLOCK_NOTE_BLOCK_CHIME);
                    playTitleToAll(String.valueOf(countdown), "Game starting in...");
                    countdown--;
                } else {
                    Bukkit.broadcastMessage(pref.getUHCPrefix() + "The game has begun! Good luck!");
                    Bukkit.broadcastMessage(pref.getUHCPrefix() + "You will be given a final heal when PVP turns on!");
                    plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "kb on");
                    plugin.getConfig().set("frozen", false);
                    plugin.saveConfig();
                    playSoundToAll(Sound.BLOCK_NOTE_BLOCK_BELL);
                    for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
                        onlinePlayer.clearTitle();
                    }
                    startTimers(pvpSeconds, meetupSeconds);
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 20);

        return true;
    }

    private void startTimers(int pvpSeconds, int meetupSeconds) {
        new BukkitRunnable() {
            int secondsLeft = pvpSeconds;

            @Override
            public void run() {
                if (secondsLeft <= 0) {
                    Bukkit.broadcastMessage(pref.getUHCPrefix() + "PvP is now enabled! This is your last heal!");
                    plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "heal *");
                    plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "pvp on");
                    playSoundToAll(Sound.BLOCK_NOTE_BLOCK_BELL);
                    startMeetupTimer(meetupSeconds);
                    cancel();
                    return;
                }

                String timeDisplay = formatTime(secondsLeft);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendActionBar(ChatColor.GOLD + "PvP" + ChatColor.GRAY + " » " + timeDisplay);
                }
                secondsLeft--;
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    private void startMeetupTimer(int meetupSeconds) {
        new BukkitRunnable() {
            int secondsLeft = meetupSeconds;

            @Override
            public void run() {
                if (secondsLeft <= 0) {
                    Bukkit.broadcastMessage(pref.getUHCPrefix() + "Meetup has started! Head to 0,0 ASAP!");
                    playSoundToAll(Sound.BLOCK_NOTE_BLOCK_BELL);
                    cancel();
                    return;
                }

                String timeDisplay = formatTime(secondsLeft);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendActionBar(ChatColor.GOLD + "Meetup" + ChatColor.GRAY + " » " + timeDisplay);
                }
                secondsLeft--;
            }
        }.runTaskTimer(plugin, 0, 20);
    }
    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format(ChatColor.YELLOW + "%d" + ChatColor.GRAY + "m" + ChatColor.YELLOW + "%d" + ChatColor.GRAY + "s", minutes, secs);
    }
    private void playSoundToAll(Sound sound) {
        for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
            onlinePlayer.playSound(onlinePlayer.getLocation(), sound, 1.0f, 1.0f);
        }
    }
    private void playTitleToAll(String header, String footer) {
        for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
            onlinePlayer.showTitle(Title.title(
                    Component.text(ChatColor.GOLD + header), Component.text(ChatColor.GRAY + footer)
            ));
        }
    }
}
