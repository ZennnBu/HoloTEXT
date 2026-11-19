
package com.holotext;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.Arrays;
import java.util.List;

public class HoloTextCommand implements CommandExecutor {

    private final HoloText plugin;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public HoloTextCommand(HoloText plugin) {
        this.plugin = plugin;
    }

    private void send(CommandSender sender, String message) {
        sender.sendMessage(
            miniMessage.deserialize(
                plugin.getConfig().getString("messages.prefix", "") + " " + message
            )
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
            send(sender, "<aqua>HoloText Commands:");
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
            }

            default -> send(sender, "<red>Unknown command!");
        }

        return true;
    }
}
