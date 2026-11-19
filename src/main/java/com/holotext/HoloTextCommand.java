
package com.holotext;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HoloTextCommand implements CommandExecutor, TabCompleter {

    private final HoloText plugin;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public HoloTextCommand(HoloText plugin) {
        this.plugin = plugin;
    }

    private void send(CommandSender sender, String message) {
        String prefix = plugin.getConfig().getString("messages.prefix", "");

        sender.sendMessage(
            miniMessage.deserialize(prefix + " " + message)
        );
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (!sender.hasPermission("holotext.admin")) {
            send(sender, "<red>You don't have permission!");
            return true;
        }

        if (args.length == 0) {
            send(sender, "<gradient:#8A2BE2:#00D4FF><bold>HoloText Commands</bold></gradient>");
            send(sender, "<gray>/ht create <name> <text>");
            send(sender, "<gray>/ht delete <name>");
            send(sender, "<gray>/ht list");
            return true;
        }

        switch (args[0].toLowerCase()) {

            case "create" -> {

                if (!(sender instanceof Player player)) {
                    send(sender, "<red>Players only!");
                    return true;
                }

                if (args.length < 3) {
                    send(sender, "<red>Usage: /ht create <name> <text>");
                    return true;
                }

                String name = args[1];

                String text = String.join(
                    " ",
                    Arrays.copyOfRange(args, 2, args.length)
                );

                Location location = player.getLocation();

                boolean created = plugin.getHologramManager().create(
                    name,
                    location,
                    List.of(text)
                );

                if (!created) {
                    send(sender, "<red>Hologram already exists!");
                    return true;
                }

                send(sender, "<green>Hologram <white>" + name + "</white> created!");
            }

            case "delete" -> {

                if (args.length < 2) {
                    send(sender, "<red>Usage: /ht delete <name>");
                    return true;
                }

                boolean deleted = plugin.getHologramManager().delete(args[1]);

                if (!deleted) {
                    send(sender, "<red>Hologram not found!");
                    return true;
                }

                send(sender, "<green>Hologram deleted!");
            }

            case "list" -> {

                send(sender, "<aqua>Holograms:");

                for (String name : plugin.getHologramManager().getHologramNames()) {
                    send(sender, "<gray>- <white>" + name);
                }

                if (plugin.getHologramManager().getHologramNames().isEmpty()) {
                    send(sender, "<gray>No holograms found.");
                }
            }

            default -> send(sender, "<red>Unknown command!");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(
            CommandSender sender,
            Command command,
            String alias,
            String[] args
    ) {

        if (!sender.hasPermission("holotext.admin")) {
            return Collections.emptyList();
        }

        if (args.length == 1) {
            return Arrays.asList("create", "delete", "list")
                .stream()
                .filter(s -> s.startsWith(args[0].toLowerCase()))
                .collect(Collectors.toList());
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("delete")) {
            return plugin.getHologramManager().getHologramNames()
                .stream()
                .filter(s -> s.startsWith(args[1].toLowerCase()))
                .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }
}
