package me.loza.uhc.commands;

import me.loza.uhc.handlers.prefixHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class gamemodeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        prefixHandler pref = new prefixHandler();
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Player targetPlayer;
            if (label.equalsIgnoreCase("gm") || label.equalsIgnoreCase("gamemode")) {
                if (args.length > 0) {
                    if (args.length > 1) {
                        targetPlayer = Bukkit.getPlayerExact(args[1]);
                        if (targetPlayer == null) {
                            player.sendMessage(pref.getErrorPrefix() + "That player is not online");
                            return true;
                        }
                    } else {
                        targetPlayer = player;
                    }
                    if (player.isOp()) {
                        String changedGamemode = "";
                        if (args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("1")) {
                            targetPlayer.setGameMode(GameMode.CREATIVE);
                            changedGamemode = ChatColor.GOLD + "Creative";
                        } else if (args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("0")) {
                            targetPlayer.setGameMode(GameMode.SURVIVAL);
                            changedGamemode = ChatColor.GOLD + "Survival";
                        } else if (args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("sp") || args[0].equalsIgnoreCase("3")) {
                            targetPlayer.setGameMode(GameMode.SPECTATOR);
                            changedGamemode = ChatColor.GOLD + "Spectator";
                        } else if (args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("a") || args[0].equalsIgnoreCase("2")) {
                            targetPlayer.setGameMode(GameMode.ADVENTURE);
                            changedGamemode = ChatColor.GOLD + "Adventure";
                        } else {
                            player.sendMessage(pref.getErrorPrefix() + "Specify a valid gamemode");
                            return true;
                        }
                        player.sendMessage(pref.getUHCPrefix() + "Gamemode changed to: " + changedGamemode);
                        if (targetPlayer != player) {
                            player.sendMessage(pref.getUHCPrefix() + "Gamemode changed to: " + changedGamemode);
                        }
                    } else {
                        player.sendMessage(pref.getErrorPrefix() + "You do not have permission for this command");
                    }
                } else {
                    player.sendMessage(pref.getErrorPrefix() + "Specify the gamemode you want to change to");
                }
            } else if (label.equalsIgnoreCase("gmc")) {
                if (args.length > 0) {
                    targetPlayer = Bukkit.getPlayerExact(args[0]);
                    if (targetPlayer == null) {
                        player.sendMessage(pref.getErrorPrefix() + "That player is not online");
                        return true;
                    }
                } else {
                    targetPlayer = player;
                }
                targetPlayer.setGameMode(GameMode.CREATIVE);
                player.sendMessage(pref.getUHCPrefix() + "Gamemode changed to: " + ChatColor.GOLD + "Creative");
                if (targetPlayer != player) {
                    player.sendMessage(pref.getUHCPrefix() + "Gamemode changed to: " + ChatColor.GOLD + "Creative");
                }
            } else if (label.equalsIgnoreCase("gms")) {
                if (args.length > 0) {
                    targetPlayer = Bukkit.getPlayerExact(args[0]);
                    if (targetPlayer == null) {
                        player.sendMessage(pref.getErrorPrefix() + "That player is not online");
                        return true;
                    }
                } else {
                    targetPlayer = player;
                }
                targetPlayer.setGameMode(GameMode.SURVIVAL);
                player.sendMessage(pref.getUHCPrefix() + "Gamemode changed to: " + ChatColor.GOLD + "Survival");
                if (targetPlayer != player) {
                    player.sendMessage(pref.getUHCPrefix() + "Gamemode changed to: " + ChatColor.GOLD + "Survival");
                }
            } else if (label.equalsIgnoreCase("gmsp")) {
                if (args.length > 0) {
                    targetPlayer = Bukkit.getPlayerExact(args[0]);
                    if (targetPlayer == null) {
                        player.sendMessage(pref.getErrorPrefix() + "That player is not online");
                        return true;
                    }
                } else {
                    targetPlayer = player;
                }
                targetPlayer.setGameMode(GameMode.SPECTATOR);
                player.sendMessage(pref.getUHCPrefix() + "Gamemode changed to: " + ChatColor.GOLD + "Spectator");
                if (targetPlayer != player) {
                    player.sendMessage(pref.getUHCPrefix() + "Gamemode changed to: " + ChatColor.GOLD + "Spectator");
                }
            } else if (label.equalsIgnoreCase("gma")) {
                if (args.length > 0) {
                    targetPlayer = Bukkit.getPlayerExact(args[0]);
                    if (targetPlayer == null) {
                        player.sendMessage(pref.getErrorPrefix() + "That player is not online");
                        return true;
                    }
                } else {
                    targetPlayer = player;
                }
                targetPlayer.setGameMode(GameMode.ADVENTURE);
                player.sendMessage(pref.getUHCPrefix() + "Gamemode changed to: " + ChatColor.GOLD + "Adventure");
                if (targetPlayer != player) {
                    player.sendMessage(pref.getUHCPrefix() + "Gamemode changed to: " + ChatColor.GOLD + "Adventure");
                }
            }
        }
        return true;
    }
}
