
package com.holotext;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.util.Transformation;

import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.*;

public class HologramManager {

    private final HoloText plugin;
    private final Map<String, List<TextDisplay>> holograms = new HashMap<>();
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public HologramManager(HoloText plugin) {
        this.plugin = plugin;
    }

    public boolean create(String name, Location location, List<String> lines) {

        if (holograms.containsKey(name.toLowerCase())) {
            return false;
        }

        List<TextDisplay> displays = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {

            TextDisplay display = location.getWorld().spawn(
                location.clone().add(0, -i * 0.25, 0),
                TextDisplay.class
            );

            display.text(miniMessage.deserialize(lines.get(i)));
            display.setBillboard(Display.Billboard.CENTER);
            display.setShadowed(true);
            display.setSeeThrough(false);
            display.setPersistent(true);
            display.setTransformation(new Transformation(
                display.getTransformation().getTranslation(),
                display.getTransformation().getLeftRotation(),
                display.getTransformation().getScale(),
                display.getTransformation().getRightRotation()
            ));

            displays.add(display);
        }

        holograms.put(name.toLowerCase(), displays);
        return true;
    }

    public boolean delete(String name) {

        List<TextDisplay> displays = holograms.remove(name.toLowerCase());

        if (displays == null) {
            return false;
        }

        displays.forEach(Entity::remove);
        return true;
    }

    public boolean exists(String name) {
        return holograms.containsKey(name.toLowerCase());
    }

    public Set<String> getHologramNames() {
        return Collections.unmodifiableSet(holograms.keySet());
    }

    public void clear() {
        holograms.values().forEach(list ->
            list.forEach(Entity::remove)
        );

        holograms.clear();
    }
}
