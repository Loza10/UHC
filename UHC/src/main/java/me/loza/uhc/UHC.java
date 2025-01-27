package me.loza.uhc;

import me.loza.uhc.commands.*;
import me.loza.uhc.handlers.playerHandler;
import me.loza.uhc.listeners.*;
import me.loza.uhc.recipes.customRecipes;
import me.loza.uhc.scenarios.scenarioManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

public final class UHC extends JavaPlugin {
    public static Map<UUID, playerHandler> userSettings = new HashMap<>();
    public static boolean chat;
    public static boolean teams;
    public static boolean pvpStatus;
    Random random = new Random();

    public void onEnable() {

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        chat = true;
        teams = false;
        pvpStatus = true;

        // Listeners
        getServer().getPluginManager().registerEvents(new deathEvent(this), this);
        getServer().getPluginManager().registerEvents(new consumeEvent(this), this);
        getServer().getPluginManager().registerEvents(new interactEvent(this), this);
        getServer().getPluginManager().registerEvents(new mobDrop(this), this);
        getServer().getPluginManager().registerEvents(new blockBreak(this), this);
        getServer().getPluginManager().registerEvents(new invClick(this), this);
        getServer().getPluginManager().registerEvents(new craftItem(this), this);
        getServer().getPluginManager().registerEvents(new blockPlace(this), this);
        getServer().getPluginManager().registerEvents(new attackEvent(this), this);
        getServer().getPluginManager().registerEvents(new shearEvent(this), this);
        getServer().getPluginManager().registerEvents(new damageEvent(this), this);
        getServer().getPluginManager().registerEvents(new potionEvent(this), this);
        getServer().getPluginManager().registerEvents(new playerJoin(this), this);
        getServer().getPluginManager().registerEvents(new portalEvent(this), this);
        getServer().getPluginManager().registerEvents(new mobSpawn(this), this);
        getServer().getPluginManager().registerEvents(new chestEvent(this), this);
        getServer().getPluginManager().registerEvents(new playerMove(this), this);
        getServer().getPluginManager().registerEvents(new chatEvent(this), this);
        getServer().getPluginManager().registerEvents(new playerRespawn(this), this);

        // Commands
        getCommand("uhc").setExecutor(new uhcCmd(this));
        getCommand("wg").setExecutor(new worldCmd(this));
        getCommand("pregen").setExecutor(new pregenCmd(this));
        getCommand("pvp").setExecutor(new pvpCmd(this));
        getCommand("start").setExecutor(new startCmd(this));
        getCommand("freeze").setExecutor(new freezeCmd(this));
        getCommand("whitelist").setExecutor(new whitelistCommand(this));
        getCommand("shrink").setExecutor(new shrinkCommand(this));
        getCommand("fullbright").setExecutor(new fullbrightCmd(this));
        getCommand("team").setExecutor(new teamManager(this));
        getCommand("kb").setExecutor(new killboardCmd(this));
        getCommand("scatter").setExecutor(new scatterCommand(this));
        getCommand("pmc").setExecutor(new pmcCmd(this));
        getCommand("clear").setExecutor(new clearCommand());
        getCommand("feed").setExecutor(new feedCommand());
        getCommand("heal").setExecutor(new healCommand(this));
        getCommand("message").setExecutor(new messageCommand());
        getCommand("reply").setExecutor(new replyCommand());
        getCommand("xp").setExecutor(new expCommand());
        getCommand("chat").setExecutor(new chatCommand());
        getCommand("ac").setExecutor(new adminCommand());

        //Gamemode commands
        getCommand("gamemode").setExecutor(new gamemodeCommand());
        getCommand("gma").setExecutor(new gamemodeCommand());
        getCommand("gmc").setExecutor(new gamemodeCommand());
        getCommand("gmsp").setExecutor(new gamemodeCommand());
        getCommand("gms").setExecutor(new gamemodeCommand());

        //Scenario manager
        scenarioManager sm = new scenarioManager(this);
        sm.registerEvents();
        sm.registerCmds();

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        if (!getConfig().isSet("whitelist.enabled")) {
            getConfig().set("whitelist.enabled", false);
        }

        customRecipes cr = new customRecipes(this);
        if (this.getConfig().getBoolean("features.golden_heads")) {
            cr.registerGoldenHeadRecipe();
        }
        if (this.getConfig().getBoolean("features.trident_craft")) {
            cr.registerTridentRecipe();
        }
        if (this.getConfig().getInt("features.apple_rates") == 0) {
            this.getConfig().set("features.apple_rates", 1);
        }
        if (this.getConfig().getInt("features.flint_rates") == 0) {
            this.getConfig().set("features.flint_rates", 50);
        }

        this.saveConfig();

        List<ChatColor> colors = new ArrayList<>(List.of(
                ChatColor.GRAY, ChatColor.BLUE, ChatColor.WHITE, ChatColor.GOLD, ChatColor.DARK_GRAY, ChatColor.UNDERLINE, ChatColor.DARK_BLUE,
                ChatColor.DARK_PURPLE, ChatColor.LIGHT_PURPLE, ChatColor.AQUA, ChatColor.BLACK, ChatColor.DARK_AQUA, ChatColor.BOLD, ChatColor.STRIKETHROUGH,
                ChatColor.DARK_GREEN, ChatColor.DARK_RED, ChatColor.RED, ChatColor.GREEN, ChatColor.ITALIC, ChatColor.YELLOW
        ));

        int teamSize = colors.size();
        Random random = new Random();


        for (int i = 1; i <= teamSize; i++) {
            String teamName = String.valueOf(i);
            if (scoreboard.getTeam(teamName) == null) {
                scoreboard.registerNewTeam(teamName);

                int randomIndex = random.nextInt(colors.size());
                ChatColor tc = colors.get(randomIndex);
                colors.remove(randomIndex);

                scoreboard.getTeam(teamName).setColor(tc);
                scoreboard.getTeam(teamName).addPlayer(Bukkit.getOfflinePlayer("Dummy" + teamName));
            }
        }
        // Health system
        this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "scoreboard objectives add UHC dummy");
        this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "scoreboard objectives setdisplay list UHC");
        this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "scoreboard players add Dummy UHC 100");
        for (Player p : Bukkit.getOnlinePlayers()) {
            this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "scoreboard players set " + p.getName() + " UHC " + (int) (p.getHealth() / 2 * 10));
        }
        System.out.println("UHC Plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        System.out.println("UHC Plugin has been disabled!");
    }

    public void updateHealth(Player player) {
        Bukkit.getScheduler().runTask(this, () -> {
            this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "scoreboard players set " + player.getName() + " UHC " + (int) (player.getHealth() / 2 * 10));
        });
    }
}
