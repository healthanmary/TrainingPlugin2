package ru.healthanmary.trainingplugin2.trade;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import ru.healthanmary.trainingplugin2.TrainingPlugin2;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TradeManager {
    private TrainingPlugin2 trainingPlugin2;
    private final Map<UUID, TradeSession> activeTrades = new HashMap<>();

    public TradeManager(TrainingPlugin2 trainingPlugin2) {
        this.trainingPlugin2 = trainingPlugin2;
    }
    public void createTrade(Player player1, Player player2) {
        TradeSession tradeSession = new TradeSession(player1, player2);
        tradeSession.setActive(true);
        activeTrades.put(player1.getUniqueId(), tradeSession);
        activeTrades.put(player2.getUniqueId(), tradeSession);
    }
    public void endTrade(Player player1, Player player2, TradeSession tradeSession) {
        tradeSession.setActive(false);
        activeTrades.remove(player1.getUniqueId());
        activeTrades.remove(player2.getUniqueId());

    }
    public Inventory getInventory(Player player) { return player.getInventory();}
    public TradeSession getTradeSession(Player player) {
        return activeTrades.get(player.getUniqueId());
    }
}
