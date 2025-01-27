package me.loza.uhc.commands;

import me.loza.uhc.UHC;
import me.loza.uhc.handlers.prefixHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class shrinkCommand implements CommandExecutor {
    private final UHC plugin;
    prefixHandler pref = new prefixHandler();
    public shrinkCommand(UHC plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(pref.getUHCPrefix() + "Usage: /shrink <world> <radius>");
            return true;
        }

        String worldName = args[0];
        World world = Bukkit.getWorld(worldName);

        if (world == null) {
            sender.sendMessage(pref.getErrorPrefix() + "World not found: " + worldName);
            return true;
        }

        int radius;
        try {
            radius = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(pref.getErrorPrefix() + "Invalid radius. Please enter a valid number.");
            return true;
        }

        if (radius <= 0) {
            sender.sendMessage(pref.getErrorPrefix() + "Radius must be greater than 0.");
            return true;
        }

        WorldBorder border = world.getWorldBorder();
        border.setCenter(0, 0);
        border.setSize(radius * 2);

        for (Player target : world.getPlayers()) {
            Location playerLoc = target.getLocation();
            double x = playerLoc.getX();
            double z = playerLoc.getZ();

            if (Math.abs(x) > radius || Math.abs(z) > radius) {
                double clampedX = Math.max(-(radius - 1), Math.min(radius - 1, x));
                double clampedZ = Math.max(-(radius - 1), Math.min(radius - 1, z));

                Location safeLocation = findSurface(world, clampedX, clampedZ);

                if (safeLocation != null) {
                    target.teleport(safeLocation);
                } else {
                    target.sendMessage(pref.getErrorPrefix() + "Failed to find a safe location for teleportation.");
                }
            }
        }

        sender.sendMessage(pref.getUHCPrefix() + "World border successfully shrunk to radius " + radius + ".");
        return true;
    }
    private Location findSurface(World world, double x, double z) {
        int y = world.getMaxHeight();
        for (; y > world.getMinHeight(); y--) {
            Location loc = new Location(world, x, y, z);
            if (!loc.getBlock().isPassable() && loc.add(0, 1, 0).getBlock().isPassable()) {
                return loc.add(0, 1, 0);
            }
        }
        return null;
    }
}
