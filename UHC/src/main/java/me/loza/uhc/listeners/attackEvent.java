package me.loza.uhc.listeners;

import me.loza.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class attackEvent implements Listener {
    private final UHC plugin;
    public attackEvent(UHC plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            ItemStack weapon = player.getInventory().getItemInMainHand();
            if ((weapon.containsEnchantment(Enchantment.FIRE_ASPECT)) && !(plugin.getConfig().getBoolean("features.fire_aspect"))) {
                Bukkit.getScheduler().runTask(plugin, () -> {
                    event.getEntity().setFireTicks(0);
                });
            }
        }
    }
}
