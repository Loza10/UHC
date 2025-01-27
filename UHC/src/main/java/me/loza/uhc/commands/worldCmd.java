package me.loza.uhc.commands;

import me.loza.uhc.UHC;
import me.loza.uhc.handlers.prefixHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;

public class worldCmd implements CommandExecutor {
    private final UHC plugin;

    public worldCmd(UHC plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        prefixHandler pref = new prefixHandler();
        if (!(sender instanceof Player)) {
            sender.sendMessage(pref.getErrorPrefix() + "Only players can use this command!");
            return true;
        } else {
            Player player = (Player) sender;
            if (label.equalsIgnoreCase("wg")) {
                if (args.length < 1) {
                    helpList(player, pref);
                    return true;
                } else {
                    if (args[0].equalsIgnoreCase("create")) {
                        if (args.length > 1) {
                            World.Environment env = World.Environment.NORMAL;
                            long seed = -1;
                            if (args.length > 2) {
                                if (args[2].equalsIgnoreCase("nether")) {
                                    env = World.Environment.NETHER;
                                } else if (args[2].equalsIgnoreCase("end")) {
                                    env = World.Environment.THE_END;
                                }
                            }
                            if (args.length > 3) {
                                try {
                                    seed = Long.parseLong(args[3]);
                                } catch (NumberFormatException e) {
                                    player.sendMessage(pref.getErrorPrefix() + "Invalid seed!");
                                    return true;
                                }
                            }
                            player.sendMessage(pref.getUHCPrefix() + "Creating " + env.toString() + " world: " + ChatColor.GOLD + args[1]);
                            World createdWorld;
                            if (seed != -1) {
                                createdWorld = Bukkit.createWorld(new WorldCreator(args[1]).environment(env).seed(seed));
                            } else {
                                createdWorld = Bukkit.createWorld(new WorldCreator(args[1]).environment(env));
                            }
                            if (createdWorld != null) {
                                player.sendMessage(pref.getUHCPrefix() + "World created!");
                            } else {
                                player.sendMessage(pref.getErrorPrefix() + "World could not be created!");
                            }
                        } else {
                            player.sendMessage(pref.getErrorPrefix() + "Specify a world name!");
                        }
                    } else if (args[0].equalsIgnoreCase("delete")) {
                        if (args.length > 1) {
                            World deletedWorld;
                            deletedWorld = Bukkit.getWorld(args[1]);
                            if (deletedWorld == null) {
                                player.sendMessage(pref.getErrorPrefix() + "World with that name does not exist!");
                            } else {
                                player.sendMessage(pref.getUHCPrefix() + "Deleting world: " + ChatColor.GOLD + args[1]);
                                Bukkit.unloadWorld(args[1], false);
                                File worldLoc = deletedWorld.getWorldFolder();
                                deleteDirectory(worldLoc);
                                player.sendMessage(pref.getUHCPrefix() + "World deleted!");
                            }
                        } else {
                            player.sendMessage(pref.getErrorPrefix() + "Specify a world name!");
                        }
                    } else if (args[0].equalsIgnoreCase("tp")) {
                        if (args.length > 1) {
                            World tpWorld;
                            tpWorld = Bukkit.getWorld(args[1]);
                            if (tpWorld == null) {
                                player.sendMessage(pref.getErrorPrefix() + "That world does not exist!");
                            } else {
                                player.teleport(tpWorld.getSpawnLocation());
                                player.sendMessage(pref.getUHCPrefix() + "Moved to world: " + args[1]);

                            }
                        } else {
                            player.sendMessage(pref.getErrorPrefix() + "Specify a world name!");
                        }
                    } else if (args[0].equalsIgnoreCase("list")) {
                        List<World> worldList = Bukkit.getWorlds();
                        player.sendMessage(pref.getUHCPrefix() + "List of worlds:");
                        for (World world : worldList) {
                            player.sendMessage(ChatColor.GRAY + "» " + ChatColor.GOLD + world.getName());
                        }
                    } else {
                        helpList(player, pref);
                    }
                }
            } else {
                helpList(player, pref);
            }
        }
        return true;
    }
    public void deleteDirectory(File file) {
        if (file.isDirectory()) {
            for (File f : file.listFiles())
                if (f.isDirectory()) {
                    deleteDirectory(f);
                } else {
                    f.delete();
                }
        }
        file.delete();
    }
    public void helpList(Player player, prefixHandler pf) {
        player.sendMessage(pf.getUHCPrefix() + "Correct Usage:");
        player.sendMessage(ChatColor.GRAY + "» " + ChatColor.GOLD + "/worldgen create <worldname> <normal/end/nether> <seed>");
        player.sendMessage(ChatColor.GRAY + "» " + ChatColor.GOLD + "/worldgen delete <worldname>");
        player.sendMessage(ChatColor.GRAY + "» " + ChatColor.GOLD + "/worldgen tp <worldname>");
        player.sendMessage(ChatColor.GRAY + "» " + ChatColor.GOLD + "/worldgen list");
    }
}
