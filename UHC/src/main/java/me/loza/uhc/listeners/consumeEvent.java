package me.loza.uhc.listeners;

import me.loza.uhc.UHC;
import me.loza.uhc.handlers.prefixHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class consumeEvent implements Listener {
    private final UHC plugin;
    prefixHandler pref = new prefixHandler();
    public consumeEvent(UHC plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onPlayerConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        if ((event.getItem().getType() == Material.ENCHANTED_GOLDEN_APPLE) && !(plugin.getConfig().getBoolean("features.god_apples"))) {
            event.setCancelled(true);
            player.sendMessage(pref.getErrorPrefix() + "God Apples are currently disabled!");
        }
        if ((event.getItem().getType() == Material.GOLDEN_APPLE) && (event.getItem().getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Golden Head")) && (plugin.getConfig().getBoolean("features.golden_heads"))) {
            Bukkit.getScheduler().runTask(plugin, () -> {
                player.removePotionEffect(PotionEffectType.REGENERATION);
                PotionEffect strongerRegeneration = new PotionEffect(PotionEffectType.REGENERATION, 100, 2);
                player.addPotionEffect(strongerRegeneration);
            });
        }
    }
}
