package me.loza.uhc.scenarios;

import me.loza.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class cutcleanLogic implements Listener {

    private final Map<Material, Material> oreToSmelted = new HashMap<>();
    private final Map<EntityType, Material> mobToCookedFood = new HashMap<>();
    private final Random random = new Random();
    private final UHC plugin;


    public cutcleanLogic(UHC plugin) {
        // Mapping ores to their smelted counterparts
        this.plugin = plugin;
        oreToSmelted.put(Material.IRON_ORE, Material.IRON_INGOT);
        oreToSmelted.put(Material.GOLD_ORE, Material.GOLD_INGOT);
        oreToSmelted.put(Material.COPPER_ORE, Material.COPPER_INGOT);
        oreToSmelted.put(Material.DEEPSLATE_IRON_ORE, Material.IRON_INGOT);
        oreToSmelted.put(Material.DEEPSLATE_GOLD_ORE, Material.GOLD_INGOT);
        oreToSmelted.put(Material.RAW_IRON_BLOCK, Material.IRON_BLOCK);
        oreToSmelted.put(Material.RAW_GOLD_BLOCK, Material.GOLD_BLOCK);

        // Mapping mobs to their cooked food drops
        mobToCookedFood.put(EntityType.COW, Material.COOKED_BEEF);
        mobToCookedFood.put(EntityType.PIG, Material.COOKED_PORKCHOP);
        mobToCookedFood.put(EntityType.CHICKEN, Material.COOKED_CHICKEN);
        mobToCookedFood.put(EntityType.SHEEP, Material.COOKED_MUTTON);
        mobToCookedFood.put(EntityType.RABBIT, Material.COOKED_RABBIT);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (plugin.getConfig().getBoolean("scenarios.cutclean")) {
            Player player = event.getPlayer();
            Material blockType = event.getBlock().getType();

            if (oreToSmelted.containsKey(blockType)) {
                // Cancel default drop
                event.setDropItems(false);

                // Drop smelted item at block location
                Material smelted = oreToSmelted.get(blockType);
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(smelted));

                // Drop experience at block location
                event.getBlock().getWorld().spawn(event.getBlock().getLocation(), org.bukkit.entity.ExperienceOrb.class).setExperience(5);
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (plugin.getConfig().getBoolean("scenarios.cutclean")) {
            EntityType entityType = event.getEntityType();
            List<ItemStack> drops = event.getDrops();
            Player killer = event.getEntity().getKiller();

            if (mobToCookedFood.containsKey(entityType)) {
                // Replace raw food with cooked food but retain other drops
                Material cookedFood = mobToCookedFood.get(entityType);

                drops.removeIf(item -> item.getType() == getRawFoodForEntity(entityType));
                drops.add(new ItemStack(cookedFood, 2));
            }
        }
    }

    private Material getRawFoodForEntity(EntityType entityType) {
        return switch (entityType) {
            case COW -> Material.BEEF;
            case PIG -> Material.PORKCHOP;
            case CHICKEN -> Material.CHICKEN;
            case SHEEP -> Material.MUTTON;
            case RABBIT -> Material.RABBIT;
            default -> null;
        };
    }
}
