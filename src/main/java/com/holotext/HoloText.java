
package com.holotext;

import org.bukkit.plugin.java.JavaPlugin;

public final class HoloText extends JavaPlugin {

    private static HoloText instance;
    private HologramManager hologramManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        hologramManager = new HologramManager(this);

        HoloTextCommand holoCommand = new HoloTextCommand(this);

        if (getCommand("holotext") != null) {
            getCommand("holotext").setExecutor(holoCommand);
        }

        getLogger().info("================================");
        getLogger().info("          HoloText");
        getLogger().info("     Professional Holograms");
        getLogger().info("================================");
        getLogger().info("Hologram Manager: Enabled");
        getLogger().info("Command System: Enabled");
        getLogger().info("HoloText started successfully!");
    }

    @Override
    public void onDisable() {
        if (hologramManager != null) {
            hologramManager.clear();
        }

        getLogger().info("HoloText has been disabled!");

        instance = null;
    }

    public static HoloText getInstance() {
        return instance;
    }

    public HologramManager getHologramManager() {
        return hologramManager;
    }
}
