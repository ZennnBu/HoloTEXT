
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

        getCommand("holotext").setExecutor(holoCommand);
        getCommand("holotext").setTabCompleter(holoCommand);

        getLogger().info("HoloText v1.0.0 enabled!");
        getLogger().info("Hologram Manager initialized!");
        getLogger().info("Command system initialized!");
    }

    @Override
    public void onDisable() {
        if (hologramManager != null) {
            hologramManager.clear();
        }

        getLogger().info("HoloText disabled!");
        instance = null;
    }

    public static HoloText getInstance() {
        return instance;
    }

    public HologramManager getHologramManager() {
        return hologramManager;
    }
}
