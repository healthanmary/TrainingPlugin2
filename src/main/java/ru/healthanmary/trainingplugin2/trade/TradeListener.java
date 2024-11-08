package ru.healthanmary.trainingplugin2.trade;

import org.bukkit.Bukkit;
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
    public void onInventoryClickEvent(InventoryClickEvent e) {
        if(!(e.getWhoClicked() instanceof Player clicker)) return;
        TradeSession session = tradeManager.getTradeSession(clicker);
        if (session == null) return;
        if (session.getReadyPlayer(clicker)) e.setCancelled(true);
        InventoryAction action = e.getAction();
        if (e.getClick() == ClickType.SWAP_OFFHAND) {
            e.setCancelled(true);
            Bukkit.getScheduler().runTaskLater(trainingPlugin2, () -> {
                ItemStack offHandItem = clicker.getInventory().getItemInOffHand();
                clicker.getInventory().setItemInOffHand(offHandItem);
            }, 1L);
        }
        if (e.getAction() == InventoryAction.COLLECT_TO_CURSOR) {
            e.setCancelled(true);
        }
        // Запрет на перемещение шифтом
        if (action == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            e.setCancelled(true);
        }
        // Запрет на перемещение цифрами
        if (e.getHotbarButton() != -1 || e.getClick().isKeyboardClick() || e.getClick() == ClickType.NUMBER_KEY) {
            e.setCancelled(true);
        }
        List<Integer> notAllowedSlots = List.of(
                4, 5, 6, 7, 8, 13, 14, 15, 16, 22, 23, 24, 25, 31, 32, 33, 34, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53
        );
        if (e.getClickedInventory().getType() == InventoryType.CHEST)
            if (notAllowedSlots.contains(e.getSlot())) e.setCancelled(true);
        if (e.getSlot() == 45) {
            e.setCancelled(true);
            if (!tradeManager.getTradeSession(clicker).getReadyPlayer(clicker) && !session.getIsHasActiveTimer()) {
                setReadyInv(session, clicker, true);
                session.setReadyPlayer(clicker, true);
            } else if (tradeManager.getTradeSession(clicker).getReadyPlayer(clicker) && !session.getIsHasActiveTimer()) {
                setReadyInv(session, clicker, false);
                session.setReadyPlayer(clicker, false);
            }
            Player player1 = clicker;
            Player player2 = session.getOtherPlayer(clicker);
            if (session.getReadyPlayer(player1) && session.getReadyPlayer(player2) && session.getIsHasActiveTimer()) {
                Bukkit.getScheduler().cancelTask(session.getTaskId());
                session.setIsHasActiveTimer(false);
                session.setReadyPlayer(player1, false);
                session.setReadyPlayer(player2, false);
                session.setCounter(11);
                setReadyInv(session, clicker, false);
                setReadyInv(session, player2, false);
            } else if (session.getReadyPlayer(player1) && session.getReadyPlayer(player2) && !session.getIsHasActiveTimer()) {
                runTimerBeforeTrade(player1, session);
            }
        }
    }
    @EventHandler
    public void syncInventories(InventoryClickEvent e) {
        if(!(e.getWhoClicked() instanceof Player clicker)) return;
        TradeSession session = tradeManager.getTradeSession(clicker);
        if (session == null) return;
//                allowedSlots = 0, 1, 2, 3, 9, 10, 11, 12, 18, 19, 20, 21, 27, 28, 29, 30, 36, 37, 38, 39
        List<Integer> notAllowedSlots = List.of(
                4, 5, 6, 7, 8, 13, 14, 15, 16, 22, 23, 24, 25, 31, 32, 33, 34, 40, 41, 42, 43, 45, 46, 47, 48, 49, 50, 51, 52, 53
        );
        if (e.getClickedInventory() != null && e.getClickedInventory().getType() == InventoryType.CHEST) {
            if (notAllowedSlots.contains(e.getSlot())) return;
        }
        Bukkit.getScheduler().runTaskLater(trainingPlugin2, () -> {
            sync(clicker, session);
        }, 1L);
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
        if (session.getIsHasActiveTimer() || !session.isActive()) return;

        Player player1 = session.getPlayer1();
        Player player2 = session.getPlayer2();

        tradeManager.endTrade(player1, player2, session);
        Player otherPlayer = session.getOtherPlayer(player);
        otherPlayer.closeInventory();

        returnItems(player, session);
        returnItems(otherPlayer, session);
        player.sendMessage(prefix+"Вы "+ChatColor.AQUA+"закрыли меню обмена,"+ChatColor.WHITE+" трейд завершен.");
        player.sendMessage("Ресурсы были возвращены.");
        otherPlayer.sendMessage(prefix+player.getName()+ChatColor.AQUA+" закрыл меню обмена,"+ChatColor.WHITE+" трейд завершен.");
        otherPlayer.sendMessage("Ресурсы были возвращены.");
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
        Player otherPlayer = session.getOtherPlayer(player);
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                int counter = session.getCounter();
                if (counter > 1) {
                    counter--;
                    session.setIsHasActiveTimer(true);
                    session.setCounter(counter);
                    ItemStack item = new ItemStack(Material.LIME_STAINED_GLASS_PANE, counter);
                    ItemMeta itemMeta = item.getItemMeta();
                    itemMeta.setDisplayName(ChatColor.GREEN + "До обмена осталось " + ChatColor.AQUA + counter +
                            ChatColor.GREEN + " секунд." + ChatColor.GRAY + " (ЛКМ для отмены)");
                    item.setItemMeta(itemMeta);
                    session.getInventory(player).setItem(45, item);
                    session.getInventory(otherPlayer).setItem(45, item);
                } else {
                    String prefix = ChatColor.GREEN+"Трейд "+ChatColor.GRAY+"» "+ChatColor.WHITE;
                    changeItems(session);

                    player.closeInventory();
                    otherPlayer.closeInventory();
                    player.sendMessage(prefix+"Вы успешно обменялись с игроком " + ChatColor.AQUA+otherPlayer.getName());
                    otherPlayer.sendMessage(prefix+"Вы успешно обменялись с игроком " + ChatColor.AQUA+player.getName());
                    session.setIsHasActiveTimer(false);
                    tradeManager.endTrade(player, otherPlayer, session);
                    this.cancel();
                }
            }
        }.runTaskTimer(trainingPlugin2, 0L, 20L);
        session.setPlayerTaskID(player, task.getTaskId());
    }
    private void sync(Player player, TradeSession session) {
        List<Integer> allowedSlots = List.of(
                0, 1, 2, 3, 9, 10, 11, 12, 18, 19, 20, 21, 27, 28, 29, 30, 36, 37, 38, 39
        );
        Inventory inventory = session.getInventory(player);
        for (int slot : allowedSlots) {
            ItemStack item = inventory.getItem(slot);
            if (item == null) {
                session.getOtherInventory(player).setItem(slot+5, null);
            }
            session.getOtherInventory(player).setItem(slot+5, item);
        }
    }
    private void setReadyInv(TradeSession session, Player player, boolean ready) {
        ItemStack item = new ItemStack(ready ? Material.LIME_STAINED_GLASS_PANE : Material.YELLOW_STAINED_GLASS_PANE, 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ready ? ChatColor.GREEN + "Вы готовы " + ChatColor.GRAY + "(ЛКМ для отмены)" : ChatColor.YELLOW + "Подтвердите готовность " + ChatColor.GRAY + "(ЛКМ)");
        item.setItemMeta(itemMeta);
        session.getInventory(player).setItem(45, item);

        ItemStack otherPane = new ItemStack(ready ? Material.LIME_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE, 1);
        ItemMeta otherPaneMeta = otherPane.getItemMeta();
        otherPaneMeta.setDisplayName(ready ? ChatColor.GREEN + "Игрок готов" : ChatColor.RED + "Игрок не готов");
        otherPane.setItemMeta(otherPaneMeta);
        session.getOtherInventory(player).setItem(53, otherPane);
    }
    private void changeItems(TradeSession session) {
        Player player = session.getPlayer1();
        Player player2 = session.getPlayer2();

        giveItem(player, session);
        giveItem(player2, session);
    }
    private void giveItem(Player player, TradeSession session) {
        Player otherPlayer = session.getOtherPlayer(player);
        List<Integer> allowedSlots = List.of(
                0, 1, 2, 3, 9, 10, 11, 12, 18, 19, 20, 21, 27, 28, 29, 30, 36, 37, 38, 39
        );
        for (int slot : allowedSlots) {
            ItemStack item = session.getInventory(player).getItem(slot);
            if (item != null) {
                otherPlayer.getInventory().addItem(item);
            }
        }
    }
}
