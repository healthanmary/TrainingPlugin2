package ru.healthanmary.trainingplugin2.trade;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class TradeListener implements Listener {
    private final TradeManager tradeManager;

    public TradeListener(TradeManager tradeManager) {
        this.tradeManager = tradeManager;
    }
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        if(!(e.getWhoClicked() instanceof Player clicker)) return;
        TradeSession session = tradeManager.getTradeSession(clicker);
        if (session == null) return;
        List<Integer> notAllowedSlots = List.of(
                4, 5, 6, 7, 8, 13, 14, 15, 16, 22, 23, 24, 25, 31, 32, 33, 34, 40, 41, 42, 43, 45, 46, 47, 48, 49, 50, 51, 52, 53
        );
        if (e.getClickedInventory() != null && e.getClickedInventory().getType() == InventoryType.CHEST) {
            if (notAllowedSlots.contains(e.getSlot())) e.setCancelled(true);
        }
    }
    @EventHandler
    public void onInventoryClickEvent2(InventoryClickEvent e) {
        if(!(e.getWhoClicked() instanceof Player clicker)) return;
        TradeSession session = tradeManager.getTradeSession(clicker);
        if (session == null) return;
        if (e.getSlot() != 45) {
            e.setCancelled(true);
            return;
        }
        /*
        1. Получаем инв кликера, редачим
        2. Получаем инв другого игрока, редачим
        3. Проверка на готовность двух игроков. написать это в trademanager
        */

        // Если не готов
        if (!tradeManager.getTradeSession(clicker).getReadyPlayer(clicker)) {
            tradeManager.getTradeSession(clicker).setReadyPlayer(clicker, true);
            ItemStack item = new ItemStack(Material.LIME_STAINED_GLASS_PANE, 1);
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(ChatColor.GREEN+"Вы готовы");
            item.setItemMeta(itemMeta);
            session.getInventory(clicker).setItem(45, item);

            ItemStack itemOther = new ItemStack(Material.LIME_STAINED_GLASS_PANE, 1);
            ItemMeta itemMetaOther = itemOther.getItemMeta();
            itemMetaOther.setDisplayName(ChatColor.GREEN+"Игрок готов");
            itemOther.setItemMeta(itemMetaOther);
            session.getOtherInventory(clicker).setItem(53, itemOther);
        }
        else {
            tradeManager.getTradeSession(clicker).setReadyPlayer(clicker, false);
            /*
            1. Получаем инв кликера, редачим
            2. Получаем инв другого игрока, редачим
            3. Проверка на готовность двух игроков. написать это в trademanager
            */
            ItemStack item = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE, 1);
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(ChatColor.YELLOW+"Подтвердите готовность (ЛКМ)");
            item.setItemMeta(itemMeta);
            session.getInventory(clicker).setItem(45, item);

            ItemStack red_pane = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
            ItemMeta red_pane_meta = red_pane.getItemMeta();
            red_pane_meta.setDisplayName(ChatColor.RED+"Игрок не готов");
            red_pane.setItemMeta(red_pane_meta);
            session.getOtherInventory(clicker).setItem(53, red_pane);
        }
    }

    @EventHandler
    public void onInventoryClickEvent3(InventoryClickEvent e) {
        if(!(e.getWhoClicked() instanceof Player clicker)) return;
        TradeSession session = tradeManager.getTradeSession(clicker);
        if (session == null) return;

        Inventory from = e.getInventory();
        Inventory to = session.player1.equals(clicker) ? session.getInventory(session.player2) : session.getInventory(session.player1);
//        List<Integer> allowedSlots = List.of(
//                0, 1, 2, 3, 9, 10, 11, 12, 18, 19, 20, 21, 27, 28, 29, 30, 36, 37, 38, 39
//        );
        List<Integer> notAllowedSlots = List.of(
                4, 5, 6, 7, 8, 13, 14, 15, 16, 22, 23, 24, 25, 31, 32, 33, 34, 40, 41, 42, 43, 45, 46, 47, 48, 49, 50, 51, 52, 53
        );
        if (e.getClickedInventory() != null && e.getClickedInventory().getType() == InventoryType.CHEST) {
            if (notAllowedSlots.contains(e.getSlot())) return;
        }
        InventoryAction action = e.getAction();
        if (e.getClickedInventory() != null && e.getClickedInventory().getType() != InventoryType.CHEST) return;
        if (action == InventoryAction.PLACE_ALL || action == InventoryAction.PLACE_ONE || action == InventoryAction.PLACE_SOME) {
            handlePlaceItem(e, from, to);
        } else if (action == InventoryAction.PICKUP_ALL || action == InventoryAction.PICKUP_ONE || action == InventoryAction.PICKUP_SOME) {
            handlePickupItem(e, from, to);
        } else if (action == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            handleMoveToOtherInventory(e, to);
        }
    }
    public void handlePlaceItem(InventoryClickEvent e, Inventory from, Inventory to) {
        int slot = e.getSlot();
        ItemStack item = e.getCursor();
        if (item != null) {
            to.setItem(slot + 5, item);
        }
    }
    private void handlePickupItem(InventoryClickEvent e, Inventory from, Inventory to) {
        int slot = e.getSlot();
        to.setItem(slot + 5, null);
    }
    private void handleMoveToOtherInventory(InventoryClickEvent e, Inventory to) {
        int slot = e.getSlot();
        ItemStack item = e.getCurrentItem();
        if (item != null) {
            to.setItem(slot + 5, item);
        }
    }
    @EventHandler
    public void onInventoryDragEvent(InventoryDragEvent e) {
        if(!(e.getWhoClicked() instanceof Player clicker)) return;
        TradeSession session = tradeManager.getTradeSession(clicker);
        if (session == null) return;
        e.setCancelled(true);
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
