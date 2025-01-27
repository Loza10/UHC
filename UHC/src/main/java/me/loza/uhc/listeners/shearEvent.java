package me.loza.uhc.listeners;

import me.loza.uhc.UHC;
import org.bukkit.Material;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;

public class shearEvent implements Listener {
    private final UHC plugin;
    public shearEvent(UHC plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onPlayerShearSheep(PlayerShearEntityEvent event) {
        if ((event.getEntity() instanceof Sheep) && (plugin.getConfig().getBoolean("features.shear_string"))) {
            Sheep sheep = (Sheep) event.getEntity();
            double chance = Math.random();
            if (chance <= 0.5) {
                ItemStack stringItem = new ItemStack(Material.STRING, 1);
                sheep.getWorld().dropItemNaturally(sheep.getLocation(), stringItem);
            }
        }
    }
}
