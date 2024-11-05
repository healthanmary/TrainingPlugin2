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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import ru.healthanmary.trainingplugin2.TrainingPlugin2;

import java.util.List;

public class TradeListener implements Listener {
    private final TradeManager tradeManager;
    private final TrainingPlugin2 trainingPlugin2;
    public TradeListener(TradeManager tradeManager, TrainingPlugin2 trainingPlugin2) {
        this.tradeManager = tradeManager;
        this.trainingPlugin2 = trainingPlugin2;
    }
    @EventHandler
    public void onInventoryClickEvent2(InventoryClickEvent e) {
        if(!(e.getWhoClicked() instanceof Player clicker)) return;
        TradeSession session = tradeManager.getTradeSession(clicker);
        if (session == null) return;
        if (session.getReadyPlayer(clicker)) e.setCancelled(true);
        List<Integer> notAllowedSlots = List.of(
                4, 5, 6, 7, 8, 13, 14, 15, 16, 22, 23, 24, 25, 31, 32, 33, 34, 40, 41, 42, 43, 45, 46, 47, 48, 49, 50, 51, 52, 53
        );
        if (e.getClickedInventory() != null && e.getClickedInventory().getType() == InventoryType.CHEST)
            if (notAllowedSlots.contains(e.getSlot())) e.setCancelled(true);
        if (e.getSlot() == 45) {
            if (!tradeManager.getTradeSession(clicker).getReadyPlayer(clicker)) {
                tradeManager.getTradeSession(clicker).setReadyPlayer(clicker, true);
                ItemStack item = new ItemStack(Material.LIME_STAINED_GLASS_PANE, 1);
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.setDisplayName(ChatColor.GREEN+"Вы готовы "+ChatColor.GRAY+"(ЛКМ для отмены)");
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
                ItemStack item = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE, 1);
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.setDisplayName(ChatColor.YELLOW+"Подтвердите готовность "+ChatColor.GRAY+"(ЛКМ)");
                item.setItemMeta(itemMeta);
                session.getInventory(clicker).setItem(45, item);

                ItemStack red_pane = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
                ItemMeta red_pane_meta = red_pane.getItemMeta();
                red_pane_meta.setDisplayName(ChatColor.RED+"Игрок не готов");
                red_pane.setItemMeta(red_pane_meta);
                session.getOtherInventory(clicker).setItem(53, red_pane);
            }
            Player player1 = clicker;
            Player player2 = session.getOtherPlayer(clicker);
            if (session.getReadyPlayer(player1) && session.getReadyPlayer(player2)) {
                runTimerBeforeTrade(player1, session);
                runTimerBeforeTrade(player2, session);
            }
        }
    }
    @EventHandler
    public void onInventoryClickEvent3(InventoryClickEvent e) {
        if(!(e.getWhoClicked() instanceof Player clicker)) return;
        TradeSession session = tradeManager.getTradeSession(clicker);
        if (session == null) return;

        Inventory from = e.getInventory();
        Inventory to = session.player1.equals(clicker) ? session.getInventory(session.player2) : session.getInventory(session.player1);
//                allowedSlots = 0, 1, 2, 3, 9, 10, 11, 12, 18, 19, 20, 21, 27, 28, 29, 30, 36, 37, 38, 39
        List<Integer> notAllowedSlots = List.of(
                4, 5, 6, 7, 8, 13, 14, 15, 16, 22, 23, 24, 25, 31, 32, 33, 34, 40, 41, 42, 43, 45, 46, 47, 48, 49, 50, 51, 52, 53
        );
        if (e.getClickedInventory() != null && e.getClickedInventory().getType() == InventoryType.CHEST) {
            if (notAllowedSlots.contains(e.getSlot())) return;
        }
        InventoryAction action = e.getAction();
        if (session.getReadyPlayer(clicker)) return;
        if (e.getClickedInventory() != null && e.getClickedInventory().getType() != InventoryType.CHEST) return;
        if (action == InventoryAction.PLACE_ALL || action == InventoryAction.PLACE_ONE || action == InventoryAction.PLACE_SOME) {
            handlePlaceItem(e, from, to);
        } else if (action == InventoryAction.PICKUP_ALL || action == InventoryAction.PICKUP_ONE || action == InventoryAction.PICKUP_SOME) {
            handlePickupItem(e, from, to);
        } else if (action == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            handleMoveToOtherInventory(e, to);
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
        String prefix = ChatColor.GREEN+"Трейд "+ChatColor.GRAY+"» "+ChatColor.WHITE;
        Player player = (Player) e.getPlayer();
        TradeSession session = tradeManager.getTradeSession(player);
        if (session == null) return;

        Player player1 = session.getPlayer1();
        Player player2 = session.getPlayer2();

        tradeManager.endTrade(player1, player2);
        Player otherPlayer = session.getOtherPlayer(player);
        otherPlayer.closeInventory();

        returnItems(player, session);
        returnItems(otherPlayer, session);
        player.sendMessage(prefix+"Вы "+ChatColor.AQUA+"закрыли меню обмена,"+ChatColor.WHITE+" трейд завершен.");
        player.sendMessage("Ресурсы были возвращены.");
        otherPlayer.sendMessage(prefix+player.getName()+ChatColor.AQUA+" закрыл меню обмена,"+ChatColor.WHITE+" трейд завершен.");
        otherPlayer.sendMessage("Ресурсы были возвращены.");
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
    private void returnItems(Player player, TradeSession session) {
        List<Integer> allowedSlots = List.of(
                0, 1, 2, 3, 9, 10, 11, 12, 18, 19, 20, 21, 27, 28, 29, 30, 36, 37, 38, 39
        );
        Inventory inventory = session.getInventory(player);
        for (int slot : allowedSlots) {
            ItemStack item = inventory.getItem(slot);
            if (item == null) continue;
            player.getInventory().addItem(item);
        }
    }
    private void runTimerBeforeTrade(Player player, TradeSession session) {
        BukkitTask task = new BukkitRunnable() {
            private int counter = 10;
            @Override
            public void run() {
                if (counter > 0) {
                    session.setHasActiveTimer(true);
                    counter--;
                    ItemStack item = new ItemStack(Material.LIME_STAINED_GLASS_PANE, counter);
                    ItemMeta itemMeta = item.getItemMeta();
                    itemMeta.setDisplayName(ChatColor.GREEN + "До обмена осталось " + ChatColor.AQUA + counter +
                            ChatColor.GREEN + " секунд." + ChatColor.GRAY + " (ЛКМ для отмены)");
                    item.setItemMeta(itemMeta);
                    player.getInventory().setItem(45, item);
                    session.getInventory(player).setItem(45, item);
                } else {
                    session.setHasActiveTimer(false);
                    this.cancel();
                }
            }
        }.runTaskTimer(trainingPlugin2, 0L, 20L);
        session.setPlayerTaskID(player, task.getTaskId());
    }
}
