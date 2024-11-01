package ru.healthanmary.trainingplugin2.trade;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TradeListener implements Listener {
    private final TradeManager tradeManager;

    public TradeListener(TradeManager tradeManager) {
        this.tradeManager = tradeManager;
    }
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        Player clicker = (Player) e.getWhoClicked();
        TradeSession session = tradeManager.getTradeSession(clicker);
        if (session == null) return;
        // 1 - Сделать лист из лотов которые он не может нажать
        // 2 - Если добавляет в лист из разрешенных = добавляется и у другого игрока
        //
        List<Integer> notAllowedSlots = List.of(
                4, 5, 6, 7, 8, 13, 14, 15, 16, 22, 23, 24, 25, 31, 32, 33, 34, 40, 41, 42, 43, 45, 46, 47, 48, 49, 50, 51, 52, 53

        );
        if (notAllowedSlots.contains(e.getSlot())) e.setCancelled(true);
    }
    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        TradeSession session = tradeManager.getTradeSession(player);
        if (session == null) return;
        else {
            Player player1 = session.getPlayer1();
            Player player2 = session.getPlayer2();

            tradeManager.endTrade(player1, player2);
            Player otherPlayer = session.getOtherPlayer(player);
            otherPlayer.closeInventory();
            player1.sendMessage("Один из участников обмена закрыл меню. Трейд завершен.");
            player2.sendMessage("Один из участников обмена закрыл меню. Трейд завершен.");
        }
    }
}
