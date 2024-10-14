package ru.healthanmary.trainingplugin2.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
@AllArgsConstructor @Getter
public class NearCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Доступно только игрокам!");
            return true; }

        Map<String, Integer> players = returnNearbyPlayers((Player) sender);
        if (players.isEmpty()) {
            sender.sendMessage(ChatColor.GOLD + "Поблизости никого нет.");
            return true; }
        sender.sendMessage(ChatColor.GOLD + "Игроки поблизости: ");
        for (Map.Entry<String, Integer> entry : players.entrySet()) {
            String nick = entry.getKey();
            Integer distance = entry.getValue();
            if (Bukkit.getPlayer(nick).hasPotionEffect(PotionEffectType.INVISIBILITY))
                sender.sendMessage(ChatColor.GREEN + nick + ChatColor.WHITE + " - " + ChatColor.YELLOW + distance + "м" + ChatColor.GRAY + " (Невидимый)");
            else sender.sendMessage(ChatColor.GREEN + nick + ChatColor.WHITE + " - " + ChatColor.YELLOW + distance + "м");
        }
        return true;
    }
    private HashMap<String, Integer> returnNearbyPlayers(Player player) {
        HashMap<String, Integer> map = new HashMap<>();
        for (Player targetPlayer : Bukkit.getOnlinePlayers()) {
            if (targetPlayer.equals(player)) continue;
            int distance = (int) targetPlayer.getLocation().distance(player.getLocation());
            if (distance > 200) continue;
            if (targetPlayer.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                if (player.hasPermission("near.see.invisible")) map.put(targetPlayer.getName(), distance);
            } else map.put(targetPlayer.getName(), distance);
        }
        return map;
    }
//    @AllArgsConstructor @Getter
//    private static class PlayerDistance implements Comparable<PlayerDistance> {
//
//        private final int distance;
//        private final String playername;
//        @Override
//        public int compareTo(@NotNull NearCommand.PlayerDistance other) {
//            return Integer.compare(this.distance, other.distance);
//        }
//    }
}
