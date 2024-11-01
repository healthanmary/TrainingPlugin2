package ru.healthanmary.trainingplugin2.trade;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TradeManager {
    private final Map<UUID, TradeSession> activeTrades = new HashMap<>();
    public void createTrade(Player player1, Player player2) {
        TradeSession tradeSession = new TradeSession(player1, player2);
        activeTrades.put(player1.getUniqueId(), tradeSession);
        activeTrades.put(player2.getUniqueId(), tradeSession);
    }
    public void endTrade(Player player1, Player player2) {
        activeTrades.remove(player1.getUniqueId());
        activeTrades.remove(player2.getUniqueId());
    }
    public TradeSession getTradeSession(Player player) {
        return activeTrades.get(player.getUniqueId());
    }
}
