package me.loza.uhc.scenarios;

import me.loza.uhc.UHC;
import me.loza.uhc.listeners.deathEvent;

public class scenarioManager {
    private final UHC plugin;
    public scenarioManager(UHC plugin) {
        this.plugin = plugin;
    }

    public void registerEvents() {
        plugin.getServer().getPluginManager().registerEvents(new smClick(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new cutcleanLogic(plugin), plugin);
    }
    public void registerCmds() {
        plugin.getCommand("sm").setExecutor(new smCmd(plugin));
        plugin.getCommand("scen").setExecutor(new scenCmd(plugin));
    }
}
