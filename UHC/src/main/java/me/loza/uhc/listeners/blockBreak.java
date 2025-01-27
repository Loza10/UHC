package me.loza.uhc.listeners;

import me.loza.uhc.UHC;
import me.loza.uhc.handlers.prefixHandler;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class blockBreak implements Listener {
    private final UHC plugin;
    prefixHandler pref = new prefixHandler();
    public blockBreak(UHC plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLeafDecay(LeavesDecayEvent event) {
        if (event.getBlock().getType() == Material.OAK_LEAVES || event.getBlock().getType() == Material.ACACIA_LEAVES || event.getBlock().getType() == Material.SPRUCE_LEAVES || event.getBlock().getType() == Material.DARK_OAK_LEAVES || event.getBlock().getType() == Material.BIRCH_LEAVES || event.getBlock().getType() == Material.AZALEA_LEAVES || event.getBlock().getType() == Material.CHERRY_LEAVES || event.getBlock().getType() == Material.JUNGLE_LEAVES || event.getBlock().getType() == Material.FLOWERING_AZALEA_LEAVES) {
            int appleRates = plugin.getConfig().getInt("features.apple_rates", 0);
            Random random = new Random();
            if (random.nextInt(100) < appleRates) {
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.APPLE));
            }
        }
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if ((event.getBlock().getType() == Material.GLOWSTONE) && !(plugin.getConfig().getBoolean("features.tier_ii"))) {
            event.setDropItems(false);
        }
        if (event.getBlock().getType() == Material.GRAVEL) {
            event.setDropItems(false);

            int flintRates = plugin.getConfig().getInt("features.flint_rates", 0);
            Random random = new Random();
            if (random.nextInt(100) < flintRates) {
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.FLINT));
            } else {
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.GRAVEL));
            }
        }
        if (event.getBlock().getType() == Material.OAK_LEAVES || event.getBlock().getType() == Material.ACACIA_LEAVES || event.getBlock().getType() == Material.SPRUCE_LEAVES || event.getBlock().getType() == Material.DARK_OAK_LEAVES || event.getBlock().getType() == Material.BIRCH_LEAVES || event.getBlock().getType() == Material.AZALEA_LEAVES || event.getBlock().getType() == Material.CHERRY_LEAVES || event.getBlock().getType() == Material.JUNGLE_LEAVES || event.getBlock().getType() == Material.FLOWERING_AZALEA_LEAVES) {
            int appleRates = plugin.getConfig().getInt("features.apple_rates", 0);
            Random random = new Random();
            if (random.nextInt(100) < appleRates) {
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.APPLE));
            }
        }
    }
}
