package me.loza.uhc.commands;

import me.loza.uhc.UHC;
import me.loza.uhc.handlers.prefixHandler;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class pregenCmd implements CommandExecutor {
    private final UHC plugin;
    prefixHandler pref = new prefixHandler();
    public pregenCmd(UHC plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(pref.getErrorPrefix() + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        // Check for the correct number of arguments
        if (args.length != 2) {
            player.sendMessage(pref.getUHCPrefix() + "Usage: /pregen <world> <radius>");
            return true;
        }

        String worldName = args[0];
        World world = Bukkit.getWorld(worldName);

        if (world == null) {
            player.sendMessage(pref.getErrorPrefix() + "World not found: " + worldName);
            return true;
        }

        int radius;
        try {
            radius = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(pref.getErrorPrefix() + "Invalid radius. Please enter a valid number.");
            return true;
        }

        if (radius <= 0) {
            player.sendMessage(pref.getErrorPrefix() + "Radius must be greater than 0.");
            return true;
        }

        player.sendMessage(pref.getUHCPrefix() + "Starting pre-generation for world: " + ChatColor.GOLD + worldName + " with radius: " + ChatColor.GOLD + radius);
        world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        world.setGameRule(GameRule.NATURAL_REGENERATION, false);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.setGameRule(GameRule.COMMAND_BLOCK_OUTPUT, false);
        world.setGameRule(GameRule.DO_TRADER_SPAWNING, false);
        world.getWorldBorder().setCenter(0,0);
        world.getWorldBorder().setSize(radius*2);

        int centerX = 0 >> 4;
        int centerZ = 0 >> 4;

        int chunkRadius = (radius + 15) / 16; // Convert block radius to chunk radius

        new ChunkGeneratorTask(world, centerX, centerZ, chunkRadius, player).runTaskTimer(plugin, 0, 2L);
        return true;
    }

    private static class ChunkGeneratorTask extends BukkitRunnable {
        private final World world;
        private final int centerX;
        private final int centerZ;
        private final int radius;
        private final Player player;
        private int currentX;
        private int currentZ;
        private static final int CHUNKS_PER_TICK = 1;
        private prefixHandler pref = new prefixHandler();
        private int currProg = 0;

        public ChunkGeneratorTask(World world, int centerX, int centerZ, int radius, Player player) {
            this.world = world;
            this.centerX = centerX;
            this.centerZ = centerZ;
            this.radius = radius;
            this.player = player;
            this.currentX = -radius;
            this.currentZ = -radius;
        }


        @Override
        public void run() {
            int processedChunks = 0;

            while (processedChunks < CHUNKS_PER_TICK) {
                if (currentX > radius) {
                    player.sendMessage(pref.getUHCPrefix() + "Pre-generation complete.");
                    this.cancel();
                    return;
                }

                int chunkX = centerX + currentX;
                int chunkZ = centerZ + currentZ;

                world.getChunkAt(chunkX, chunkZ, true).load(true);

                currentZ++;
                if (currentZ > radius) {
                    float currPercent = (float) currProg / (radius * 2.0f);
                    Bukkit.getServer().broadcastMessage(pref.getUHCPrefix() + "Pregenning " + world.getName() + ": " + ChatColor.GOLD + currPercent * 100.0f + "%");
                    currentZ = -radius;
                    currentX++;
                    currProg++;
                }

                processedChunks++;
            }
        }
    }
}

