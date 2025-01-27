package me.loza.uhc.listeners;

import me.loza.uhc.UHC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class potionEvent implements Listener {
    private final UHC plugin;
    public potionEvent(UHC plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onPlayerConsume(EntityPotionEffectEvent event) {
        if ((event.getNewEffect().getType() == PotionEffectType.ABSORPTION) && !(plugin.getConfig().getBoolean("features.absorption"))) {
            event.setCancelled(true);
        }
        if ((event.getNewEffect().getType() == PotionEffectType.STRENGTH) && !(plugin.getConfig().getBoolean("features.strength_potions"))) {
            event.setCancelled(true);
        }
    }
}
