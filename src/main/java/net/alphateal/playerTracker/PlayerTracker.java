package net.alphateal.playerTracker;

import org.bukkit.plugin.java.JavaPlugin;

public class PlayerTracker extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getCommand("tracker").setExecutor(new Tracker(this));
    }

    @Override
    public void onDisable() {}
}