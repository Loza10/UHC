package me.loza.uhc.handlers;

import org.bukkit.entity.Player;

public class playerHandler {
    Player lastReply;
    public playerHandler(Player replyPlayer) {
        lastReply = replyPlayer;
    }
    public Player getLastReply() {
        return lastReply;
    }
    public void setLastReply(Player reply) {
        lastReply = reply;
    }
}
