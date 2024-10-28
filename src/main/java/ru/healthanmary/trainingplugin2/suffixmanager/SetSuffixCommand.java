package ru.healthanmary.trainingplugin2.suffixmanager;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SetSuffixCommand implements CommandExecutor {
    @Getter
    private final Map<UUID, String> suffixMap = new HashMap<>();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Указано неверное количество аргументов!");
            return true;
        }
        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (!targetPlayer.isOnline()) {
            sender.sendMessage("Игрок не найден!");
            return true;
        }
        suffixMap.put(targetPlayer.getUniqueId(), args[1]);
        return true;
    }
}
