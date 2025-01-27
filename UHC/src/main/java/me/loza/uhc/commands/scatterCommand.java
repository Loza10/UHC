package me.loza.uhc.commands;

import me.loza.uhc.UHC;
import me.loza.uhc.handlers.prefixHandler;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

public class scatterCommand implements CommandExecutor {
    private final UHC plugin;
    private final prefixHandler pref = new prefixHandler();
    public scatterCommand(UHC plugin) {
        this.plugin = plugin;
    }
    private final Random random = new Random();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(pref.getErrorPrefix() + "Usage: /scatter <player/teams/*> <world>");
            return true;
        }

        String targetType = args[0];
        String worldName = args[1];

        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            sender.sendMessage(pref.getErrorPrefix() + "World " + worldName + " does not exist.");
            return false;
        }

        int borderRadius = (int) world.getWorldBorder().getSize() / 2;
        Map<String, List<Player>> teamsToScatter = getTeamsToScatter(targetType, sender);

        if (teamsToScatter == null || teamsToScatter.isEmpty()) {
            sender.sendMessage(pref.getErrorPrefix() + "No players found to scatter.");
            return false;
        }

        List<Location> scatterLocations = generateScatterLocations(teamsToScatter.size(), world, borderRadius);

        new BukkitRunnable() {
            @Override
            public void run() {
                loadChunks(scatterLocations, () -> teleportTeams(teamsToScatter, scatterLocations));
            }
        }.runTaskAsynchronously(plugin);

        return true;
    }

    private Map<String, List<Player>> getTeamsToScatter(String targetType, CommandSender sender) {
        Map<String, List<Player>> teams = new HashMap<>();
        if (targetType.equalsIgnoreCase("*")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                String teamName = getTeamName(player);
                teams.computeIfAbsent(teamName, k -> new ArrayList<>()).add(player);
            }
        } else if (targetType.equalsIgnoreCase("teams")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.getName().toLowerCase().startsWith("dummy")) {
                    String teamName = getTeamName(player);
                    teams.computeIfAbsent(teamName, k -> new ArrayList<>()).add(player);
                }
            }
        } else {
            Player player = Bukkit.getPlayer(targetType);
            if (player == null) {
                sender.sendMessage(pref.getErrorPrefix() + "Player " + targetType + " not found.");
                return null;
            }
            teams.put("solo", Collections.singletonList(player));
        }
        return teams;
    }

    private String getTeamName(Player player) {
        return player.getScoreboard().getEntryTeam(player.getName()) != null
                ? player.getScoreboard().getEntryTeam(player.getName()).getName()
                : player.getName();
    }

    private List<Location> generateScatterLocations(int count, World world, int radius) {
        List<Location> locations = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int quadrant = i % 4;
            int x = getRandomCoordinate(radius) * (quadrant < 2 ? 1 : -1);
            int z = getRandomCoordinate(radius) * (quadrant % 3 == 0 ? 1 : -1);
            int y = world.getHighestBlockYAt(x, z) + 1;
            locations.add(new Location(world, x, y, z));
        }
        return locations;
    }

    private int getRandomCoordinate(int radius) {
        return random.nextInt(radius);
    }

    private void loadChunks(List<Location> locations, Runnable callback) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Location loc : locations) {
                    Chunk chunk = loc.getChunk();
                    chunk.load(true);
                    sendActionBar("Loading chunks at scatter location...");
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        callback.run();
                    }
                }.runTask(plugin);
            }
        }.runTask(plugin);
    }

    private void teleportTeams(Map<String, List<Player>> teams, List<Location> locations) {
        int index = 0;
        for (Map.Entry<String, List<Player>> entry : teams.entrySet()) {
            Location loc = locations.get(index);
            for (Player player : entry.getValue()) {
                player.teleport(loc);
                sendActionBar("Scattering players " + ChatColor.GOLD + (index + 1) + "/" + locations.size());
            }
            index++;
        }
    }
    private void sendActionBar(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendActionBar(ChatColor.DARK_GRAY + " » " + ChatColor.GRAY + message);
        }
    }
}
