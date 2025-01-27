package me.loza.uhc.listeners;

import me.loza.uhc.UHC;
import me.loza.uhc.handlers.prefixHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

public class deathEvent implements Listener {
    private final UHC plugin;
    prefixHandler pref = new prefixHandler();

    public deathEvent(UHC plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (plugin.getConfig().getBoolean("features.golden_heads")) {
            ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta skullMeta = (SkullMeta) playerHead.getItemMeta();

            if (skullMeta != null) {
                skullMeta.setOwningPlayer(player);
                playerHead.setItemMeta(skullMeta);
            }

            player.getWorld().dropItemNaturally(player.getLocation(), playerHead);
        }

        DamageSource lastDamage = event.getDamageSource();
        if (!(lastDamage.getCausingEntity() instanceof Player)) {
            Bukkit.getScoreboardManager();
            Objective pveObjective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("Kills");
            if (pveObjective != null) {
                Score pveScore = pveObjective.getScore(ChatColor.GREEN + "" + ChatColor.ITALIC + "PVE");
                pveScore.setScore(pveScore.getScore() + 1);
            }
        }
    }
}
