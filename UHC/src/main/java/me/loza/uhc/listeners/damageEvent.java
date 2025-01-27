package me.loza.uhc.listeners;

import me.loza.uhc.UHC;
import me.loza.uhc.handlers.prefixHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class damageEvent implements Listener {
    private final UHC plugin;
    private final prefixHandler pref = new prefixHandler();
    public damageEvent(UHC plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onHeal(EntityRegainHealthEvent event) {
        if (event.getEntity() instanceof Player) {
            plugin.updateHealth((Player) event.getEntity());
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Bukkit.getScheduler().runTask(plugin, () -> {
                plugin.updateHealth((Player) event.getEntity());
            });
        }
        if ((event.getEntity() instanceof Player) && (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE)) {
            Player player = (Player) event.getEntity();
            if (event instanceof EntityDamageByEntityEvent entityEvent) {
                if ((entityEvent.getDamager() instanceof EnderPearl) && !(plugin.getConfig().getBoolean("features.pearl_damage"))) {
                    event.setCancelled(true);
                }
            }
        }
        if (plugin.getConfig().getBoolean("features.axe_nerf")) {
            if (event.getEntity() instanceof Player) {
                if (event instanceof EntityDamageByEntityEvent entityEvent) {
                    if (entityEvent.getDamager() instanceof Player player) {
                        if (player.getInventory().getItemInMainHand().getType().toString().endsWith("_AXE")) {
                            double originalDamage = event.getDamage();
                            event.setDamage(originalDamage * 0.50);
                        }
                    }
                }
            }
        }
        if (event.getEntity() instanceof Player) {
            if (event instanceof EntityDamageByEntityEvent entityEvent) {
                if (entityEvent.getDamager() instanceof Arrow) {
                    if (plugin.getConfig().getBoolean("features.bow_nerf")) {
                        double originalDamage = event.getDamage();
                        event.setDamage(originalDamage * 0.80);
                    }
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        if (event.getDamageSource().getCausingEntity() instanceof Player attacker) {
                            if (event.getEntity() instanceof Player victim) {
                                attacker.sendMessage(pref.getUHCPrefix() + ChatColor.GOLD + victim.getName() + ChatColor.GRAY + " is now on " + ChatColor.GOLD + (victim.getHealth() / 2) + ChatColor.GRAY + " hearts.");
                            }
                        }
                    });
                }
            }
        }
    }
}
