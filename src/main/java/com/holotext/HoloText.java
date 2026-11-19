
package com.holotext;

import org.bukkit.plugin.java.JavaPlugin;

public final class HoloText extends JavaPlugin {

    private static HoloText instance;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        getLogger().info("HoloText has been enabled!");
        getLogger().info("Professional Hologram System is ready.");
    }

    @Override
    public void onDisable() {
        getLogger().info("HoloText has been disabled!");
    }

    public static HoloText getInstance() {
        return instance;
    }
}
