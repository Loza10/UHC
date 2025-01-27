package me.loza.uhc.listeners;

import me.loza.uhc.UHC;
import me.loza.uhc.handlers.prefixHandler;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class chatEvent implements Listener {
    private final UHC plugin;
    public chatEvent(UHC plugin) {
        this.plugin = plugin;
    }

    prefixHandler pref = new prefixHandler();
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (!event.getPlayer().isOp() && UHC.chat == false) {
            event.getPlayer().sendMessage(pref.getErrorPrefix() + "Chat is currently disabled!");
            event.setCancelled(true);
        } else if (event.getPlayer().isOp()) {
            event.setFormat(pref.getOPPrefix() + ChatColor.GOLD + event.getPlayer().getDisplayName() + ChatColor.DARK_GRAY + " » " + ChatColor.WHITE + event.getMessage());
        } else if (!event.getPlayer().isOp() && UHC.chat == true) {
            event.setFormat(ChatColor.WHITE + event.getPlayer().getDisplayName() + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY + event.getMessage());
        }
    }
}
