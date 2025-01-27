package me.loza.uhc.commands;

import me.loza.uhc.UHC;
import me.loza.uhc.handlers.prefixHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.awt.*;
import java.util.*;
import java.util.List;

public class teamManager implements CommandExecutor {
    private final Scoreboard scoreboard;
    private final prefixHandler pref = new prefixHandler();
    private final Map<String, String> pendingInvites = new HashMap<>();
    private int limit = 10;
    private final UHC plugin;
    public teamManager(UHC plugin) {
        this.plugin = plugin;
        this.scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }
        Player player = (Player) sender;
        if (args.length < 1) {
            correctUsage(player);
            return true;
        }
        if (args[0].equalsIgnoreCase("on")) {
            UHC.teams = true;
            player.sendMessage(pref.getUHCPrefix() + "Team creation has been enabled!");
        }
        else if (args[0].equalsIgnoreCase("off")) {
            UHC.teams = false;
            player.sendMessage(pref.getUHCPrefix() + "Team creation has been disabled!");
        }
        else if (args[0].equalsIgnoreCase("create")) {
            if (UHC.teams) {
                Set<Team> teams = new HashSet<>(scoreboard.getTeams());
                if (scoreboard.getEntityTeam(player) == null) {
                    for (Team team : teams) {
                        if (team.getSize() == 1) {
                            team.addEntities(player);
                            player.sendMessage(pref.getUHCPrefix() + "Team created! Invite others using " + ChatColor.GOLD + "/team invite <player>");
                            return true;
                        }
                    }
                    player.sendMessage(pref.getErrorPrefix() + "No available teams to join!");
                    return true;
                } else {
                    player.sendMessage(pref.getErrorPrefix() + "You're already on a team!");
                    return true;
                }
            } else {
                player.sendMessage(pref.getErrorPrefix() + "Team creation is currently disabled!");
                return true;
            }
        }
        else if (args[0].equalsIgnoreCase("make")) {
            if (args.length > 1) {
                if (!player.isOp()) {
                    player.sendMessage(pref.getErrorPrefix() + "You do not have permission to do this!");
                    return true;
                }
                Set<String> playersToAdd = new HashSet<>(Arrays.asList(args).subList(1, args.length));
                Set<Team> teams = new HashSet<>(scoreboard.getTeams());
                for (Team team : teams) {
                    if (team.getSize() == 1) {
                        for (String s : playersToAdd) {
                            if (scoreboard.getEntryTeam(s) != null) {
                                scoreboard.getEntryTeam(s).removeEntry(s);
                            }
                            team.addEntry(s);
                        }
                        player.sendMessage(pref.getUHCPrefix() + "Team " + team.getName() + " created with: " + team.getColor() + playersToAdd.toString());
                        return true;
                    }
                }
                player.sendMessage(pref.getErrorPrefix() + "No available teams to add these players to!");
                return true;
            } else {
                player.sendMessage(pref.getErrorPrefix() + "You need to specify at least 1 player!");
                return true;
            }
        }
        else if (args[0].equalsIgnoreCase("invite")) {
            if (UHC.teams) {
                if (args.length > 1) {
                    if (scoreboard.getEntityTeam(player) == null) {
                        player.sendMessage(pref.getErrorPrefix() + "You're not on a team!");
                        return true;
                    }
                    Player targetPlayer = Bukkit.getPlayer(args[1]);
                    if (targetPlayer == null) {
                        player.sendMessage(pref.getErrorPrefix() + "Player not found.");
                        return true;
                    }
                    if (player == targetPlayer) {
                        player.sendMessage(pref.getErrorPrefix() + "You can not invite yourself!");
                        return true;
                    }
                    if (scoreboard.getEntityTeam(targetPlayer) != null) {
                        player.sendMessage(pref.getErrorPrefix() + "That player is already on a team!");
                        return true;
                    }
                    if ((scoreboard.getEntityTeam(player) != null) && (scoreboard.getEntityTeam(player).getSize() >= limit)) {
                        player.sendMessage(pref.getErrorPrefix() + "Your current team size is at the limit!");
                        return true;
                    }
                    pendingInvites.put(targetPlayer.getName(), player.getName());
                    targetPlayer.sendMessage(pref.getUHCPrefix() + "You've recieved an invite from " + ChatColor.GOLD + player.getName());
                    Component msg = Component.text(ChatColor.GREEN + "" + ChatColor.BOLD + "» Click here to join their team!").hoverEvent(HoverEvent.showText(Component.text(ChatColor.GREEN + "Join team!"))).clickEvent(ClickEvent.runCommand("/tm accept " + player.getName()));
                    targetPlayer.sendMessage(msg);
                    player.sendMessage(pref.getUHCPrefix() + "Team invite sent to " + ChatColor.GOLD + targetPlayer.getName());
                    return true;
                } else {
                    player.sendMessage(pref.getErrorPrefix() + "Specify a player to invite");
                    return true;
                }
            } else {
                player.sendMessage(pref.getErrorPrefix() + "Team creation is currently disabled!");
                return true;
            }
        }
        else if (args[0].equalsIgnoreCase("accept")) {
            if (UHC.teams) {
                if (args.length > 1) {
                    Player targetPlayer = Bukkit.getPlayer(args[1]);
                    if (targetPlayer == null) {
                        player.sendMessage(pref.getErrorPrefix() + "Player not found.");
                        return true;
                    }
                    if (scoreboard.getEntityTeam(player) != null) {
                        player.sendMessage(pref.getErrorPrefix() + "You're already on a team!");
                        return true;
                    }
                    String inviter = pendingInvites.get(player.getName());
                    if (inviter == null || !inviter.equalsIgnoreCase(args[1])) {
                        player.sendMessage(pref.getErrorPrefix() + "You have no pending invite from this player.");
                        return true;
                    }
                    if (scoreboard.getEntityTeam(targetPlayer) == null) {
                        player.sendMessage(pref.getErrorPrefix() + "That player is not on a team!");
                        return true;
                    }
                    scoreboard.getEntityTeam(targetPlayer).addEntity(player);
                    player.sendMessage(pref.getUHCPrefix() + "You have joined " + ChatColor.GOLD + targetPlayer.getName() + ChatColor.GRAY + "'s team!");
                    pendingInvites.remove(player.getName());
                    return true;
                }
            } else {
                player.sendMessage(pref.getErrorPrefix() + "Team creation is currently disabled!");
                return true;
            }
        }
        else if (args[0].equalsIgnoreCase("leave")) {
            if (UHC.teams) {
                if (args.length > 1) {
                    if (!player.isOp()) {
                        player.sendMessage(pref.getErrorPrefix() + "You do not have permission to do this!");
                        return true;
                    }
                    Player targetPlayer = Bukkit.getPlayer(args[1]);
                    if (targetPlayer == null) {
                        player.sendMessage(pref.getErrorPrefix() + "Player not found.");
                        return true;
                    }
                    if (scoreboard.getEntityTeam(targetPlayer) == null) {
                        player.sendMessage(pref.getErrorPrefix() + "That player is not on a team!");
                        return true;
                    }
                    scoreboard.getEntityTeam(targetPlayer).removeEntity(targetPlayer);
                    player.sendMessage(pref.getUHCPrefix() + "You have removed " + ChatColor.GOLD + targetPlayer.getName() + ChatColor.GRAY + " from their team!");
                    targetPlayer.sendMessage(pref.getUHCPrefix() + "You have been removed from your team!");
                    return true;
                } else {
                    if (scoreboard.getEntityTeam(player) == null) {
                        player.sendMessage(pref.getErrorPrefix() + "You're not on a team!");
                        return true;
                    }
                    scoreboard.getEntityTeam(player).removeEntity(player);
                    player.sendMessage(pref.getUHCPrefix() + "You have left your team!");
                    return true;
                }
            } else {
                player.sendMessage(pref.getErrorPrefix() + "Team creation is currently disabled!");
                return true;
            }
        }
        else if (args[0].equalsIgnoreCase("add")) {
            if (args.length > 2) {
                if (!player.isOp()) {
                    player.sendMessage(pref.getErrorPrefix() + "You do not have permission to do this!");
                    return true;
                }
                Player newPlayer = Bukkit.getPlayer(args[1]);
                if (newPlayer == null) {
                    player.sendMessage(pref.getErrorPrefix() + "Player 1 not found.");
                    return true;
                }

                Player teamPlayer = Bukkit.getPlayer(args[2]);
                if (teamPlayer == null) {
                    player.sendMessage(pref.getErrorPrefix() + "Player 2 not found.");
                    return true;
                }
                if (scoreboard.getEntityTeam(teamPlayer) == null) {
                    player.sendMessage(pref.getErrorPrefix() + "Player 2 must be on a team already!");
                    return true;
                }
                if (scoreboard.getEntityTeam(newPlayer) != null) {
                    scoreboard.getEntityTeam(newPlayer).removeEntity(newPlayer);
                }
                scoreboard.getEntityTeam(teamPlayer).addEntity(newPlayer);
                player.sendMessage(pref.getUHCPrefix() + "Added " + ChatColor.GOLD + newPlayer.getName() + ChatColor.GRAY + " to " + ChatColor.GOLD + teamPlayer.getName() + ChatColor.GRAY + "'s team!");
                newPlayer.sendMessage(pref.getUHCPrefix() + "You've been added to " + ChatColor.GOLD + teamPlayer.getName() + ChatColor.GRAY + "'s team!");
                teamPlayer.sendMessage(pref.getUHCPrefix() + "Added " + ChatColor.GOLD + newPlayer.getName() + ChatColor.GRAY + " to your team!");
                return true;
            } else {
                player.sendMessage(pref.getErrorPrefix() + "Correct usage: /team add <player> <player>");
            }
        }
        else if (args[0].equalsIgnoreCase("limit")) {
            if (args.length > 1) {
                if (!player.isOp()) {
                    player.sendMessage(pref.getErrorPrefix() + "You do not have permission to do this!");
                    return true;
                }
                int teamLimit;
                try {
                    teamLimit = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    player.sendMessage(pref.getErrorPrefix() + "Invalid limit for team size. Please enter a valid number.");
                    return true;
                }

                if (teamLimit <= 0) {
                    player.sendMessage(pref.getErrorPrefix() + "Team size limit must be greater than 0.");
                    return true;
                }
                limit = teamLimit + 1;
                player.sendMessage(pref.getUHCPrefix() + "Team size limit has been set to " + (limit-1) + "!");
                return true;
            } else {
                player.sendMessage(pref.getErrorPrefix() + "Specify a size to get the teams to!");
                return true;
            }
        }
        else if (args[0].equalsIgnoreCase("delete")) {
            if (args.length > 1) {
                if (!player.isOp()) {
                    player.sendMessage(pref.getErrorPrefix() + "You do not have permission to do this!");
                    return true;
                }
                if (scoreboard.getTeam(args[1]) == null) {
                    player.sendMessage(pref.getErrorPrefix() + "That team does not exist! Specify a number.");
                    return true;
                }
                Set<String> players = new HashSet<>(scoreboard.getTeam(args[1]).getEntries());
                for (String p : players) {
                    if (!p.toLowerCase().contains("dummy")) {
                        scoreboard.getTeam(args[1]).removeEntry(p);
                    }
                }
                player.sendMessage(pref.getUHCPrefix() + "Team " + ChatColor.GOLD + args[1] + ChatColor.GRAY + " has been reset!");
                return true;
            } else {
                player.sendMessage(pref.getErrorPrefix() + "Specify a team to reset!");
                return true;
            }
        }
        else if (args[0].equalsIgnoreCase("recolor")) {
            if (!player.isOp()) {
                player.sendMessage(pref.getErrorPrefix() + "You do not have permission to do this!");
                return true;
            }
            Random random = new Random();
            int randomIndex;
            java.util.List<ChatColor> colors = new ArrayList<>(List.of(
                    ChatColor.GRAY, ChatColor.BLUE, ChatColor.WHITE, ChatColor.GOLD, ChatColor.DARK_GRAY, ChatColor.UNDERLINE, ChatColor.DARK_BLUE,
                    ChatColor.DARK_PURPLE, ChatColor.LIGHT_PURPLE, ChatColor.AQUA, ChatColor.BLACK, ChatColor.DARK_AQUA, ChatColor.BOLD, ChatColor.STRIKETHROUGH,
                    ChatColor.DARK_GREEN, ChatColor.DARK_RED, ChatColor.RED, ChatColor.GREEN, ChatColor.ITALIC, ChatColor.YELLOW
            ));

            for (int i = 1; i <= scoreboard.getTeams().size(); i++) {
                String teamName = String.valueOf(i);
                if (scoreboard.getTeam(teamName) != null) {
                    randomIndex = random.nextInt(colors.size());
                    ChatColor tc = colors.get(randomIndex);
                    colors.remove(randomIndex);
                    scoreboard.getTeam(teamName).setColor(tc);
                }
            }
            player.sendMessage(pref.getUHCPrefix() + "Teams have been recolored!");
            return true;
        }
        else if (args[0].equalsIgnoreCase("damage")) {
            if (args.length > 1) {
                boolean damage;
                if (!player.isOp()) {
                    player.sendMessage(pref.getErrorPrefix() + "You do not have permission to do this!");
                    return true;
                }
                if (args[1].equalsIgnoreCase("on")) {
                    damage = true;
                    player.sendMessage(pref.getUHCPrefix() + "Friendly fire has been turned on!");
                } else {
                    damage = false;
                    player.sendMessage(pref.getUHCPrefix() + "Friendly fire has been turned off!");
                }
                for (Team t : scoreboard.getTeams()) {
                    t.setAllowFriendlyFire(damage);
                }
                return true;
            } else {
                player.sendMessage(pref.getErrorPrefix() + "Specify whether team damage should be on or off!");
                return true;
            }
        }
        else if (args[0].equalsIgnoreCase("reset")) {
            if (!player.isOp()) {
                player.sendMessage(pref.getErrorPrefix() + "You do not have permission to do this!");
                return true;
            }
            for (Team t : scoreboard.getTeams()) {
                for (String p : t.getEntries()) {
                    if (!p.toLowerCase().contains("dummy")) {
                        t.removeEntry(p);
                    }
                }
            }
            player.sendMessage(pref.getUHCPrefix() + "Teams have been reset!");
            return true;
        }
        else if (args[0].equalsIgnoreCase("list")) {
            for (Team t : scoreboard.getTeams()) {
                if (t.getSize() > 1) {
                    List<String> filteredEntries = new ArrayList<>();
                    for (String entry : t.getEntries()) {
                        if (!entry.equalsIgnoreCase("dummy" + t.getName())) {
                            filteredEntries.add(entry);
                        }
                    }
                    player.sendMessage(pref.getUHCPrefix() + "Team " + t.getName() + ": " + t.getColor() + filteredEntries.toString());
                }
            }
            return true;
        } else {
            correctUsage(player);
        }
        return true;
    }
    public void correctUsage(Player player) {
        if (player.isOp()) {
            player.sendMessage(pref.getUHCPrefix() + "Correct Usage:");
            player.sendMessage(ChatColor.GRAY + "» " + ChatColor.GOLD + "/team on/off");
            player.sendMessage(ChatColor.GRAY + "» " + ChatColor.GOLD + "/team create");
            player.sendMessage(ChatColor.GRAY + "» " + ChatColor.GOLD + "/team make <player> <player> <player>...");
            player.sendMessage(ChatColor.GRAY + "» " + ChatColor.GOLD + "/team invite <player>");
            player.sendMessage(ChatColor.GRAY + "» " + ChatColor.GOLD + "/team leave <player>");
            player.sendMessage(ChatColor.GRAY + "» " + ChatColor.GOLD + "/team add <player> <player>");
            player.sendMessage(ChatColor.GRAY + "» " + ChatColor.GOLD + "/team limit <number>");
            player.sendMessage(ChatColor.GRAY + "» " + ChatColor.GOLD + "/team delete <number>");
            player.sendMessage(ChatColor.GRAY + "» " + ChatColor.GOLD + "/team recolor");
            player.sendMessage(ChatColor.GRAY + "» " + ChatColor.GOLD + "/team damage <on/off>");
            player.sendMessage(ChatColor.GRAY + "» " + ChatColor.GOLD + "/team reset");
            player.sendMessage(ChatColor.GRAY + "» " + ChatColor.GOLD + "/team list");
        } else {
            player.sendMessage(pref.getUHCPrefix() + "Correct Usage:");
            player.sendMessage(ChatColor.GRAY + "» " + ChatColor.GOLD + "/team create");
            player.sendMessage(ChatColor.GRAY + "» " + ChatColor.GOLD + "/team invite <player>");
            player.sendMessage(ChatColor.GRAY + "» " + ChatColor.GOLD + "/team leave");
            player.sendMessage(ChatColor.GRAY + "» " + ChatColor.GOLD + "/team list");
        }
    }

}
