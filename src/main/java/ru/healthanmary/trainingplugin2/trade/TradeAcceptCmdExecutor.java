package ru.healthanmary.trainingplugin2.trade;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TradeAcceptCmdExecutor implements CommandExecutor {
    private final TradeManager tradeManager;

    public TradeAcceptCmdExecutor(TradeManager tradeManager) {
        this.tradeManager = tradeManager;
    }
    private Map<UUID, UUID> tradeRequestsMap = new HashMap<>();
    public void addRequest(UUID uuid, UUID uuid2) {
        tradeRequestsMap.put(uuid, uuid2);
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Только для игроков!");
            return true;
        }

        Player tradeReciepent = (Player) sender;
        Player tradeSender = Bukkit.getPlayer(tradeRequestsMap.get(tradeReciepent.getUniqueId()));

        if (tradeRequestsMap.get(tradeReciepent.getUniqueId()) == null) {
            tradeReciepent.sendMessage("У вас нет активных заявок на трейд!");
            return true;
        }
        if (!tradeSender.isOnline()) {
            tradeReciepent.sendMessage("Отправитель трейда оффлайн!");
            return true;
        }
        tradeRequestsMap.remove(tradeReciepent.getUniqueId());
        tradeManager.createTrade(tradeSender, tradeReciepent);

        return true;
    }
}
