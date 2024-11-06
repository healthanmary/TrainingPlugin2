package ru.healthanmary.trainingplugin2.trade;


import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class TradeSession {
    @Getter
    public final Player player1;
    @Getter
    public final Player player2;
    private final Inventory invPlayer1;
    private final Inventory invPlayer2;
    private boolean readyPlayer1 = false;
    private boolean readyPlayer2 = false;
    private boolean hasActiveTimer;
    @Setter @Getter
    private int taskIdPlayer1;
    @Setter @Getter
    private int taskIdPlayer2;
    private Inventory fillInventory(String nick) {
        Inventory inv = Bukkit.createInventory(null, 54, "Трейд с " + nick);
        ItemStack pink_pane = new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE, 1);
        ItemMeta pink_pane_meta = pink_pane.getItemMeta();
        pink_pane_meta.setDisplayName(" ");
        pink_pane.setItemMeta(pink_pane_meta);

        for (int i = 4; i < 50; i += 9) {
            inv.setItem(i, pink_pane);
        }
        inv.setItem(51, pink_pane);
        inv.setItem(52, pink_pane);
        inv.setItem(47, pink_pane);
        inv.setItem(46, pink_pane);

        ItemStack book = new ItemStack(Material.BOOK, 1);
        ItemMeta book_meta = book.getItemMeta();
        book_meta.setDisplayName(ChatColor.AQUA+"Инструкция по трейду");
        book_meta.setLore(List.of(
                " ",
                ChatColor.GOLD+"1."+ChatColor.WHITE+" В левое меню положите ресурсы, которые",
                ChatColor.WHITE+"   хотите отдать.",
                ChatColor.GOLD+"2."+ChatColor.WHITE+" В правом меню вы увидите, какие",
                ChatColor.WHITE+"   ресурсы отдаст другой игрок.",
                ChatColor.GOLD+"3."+ChatColor.WHITE+" Подтвердите обмен, нажав",
                ChatColor.WHITE+"   нажав на желтую стеклянную панель.",
                ChatColor.GOLD+"4."+ChatColor.WHITE+" Дождись подтверждения второго игрока.",
                ChatColor.GOLD+"5."+ChatColor.WHITE+" После подтверждений начнется таймер,",
                ChatColor.WHITE+"   во время его трейд можно отменить.",
                ChatColor.GOLD+"6."+ChatColor.WHITE+" После окончанию таймера произойдет",
                ChatColor.WHITE+"   обмен ресурсами."
        ));
        book.setItemMeta(book_meta);
        inv.setItem(48, book);
        inv.setItem(50, book);

        ItemStack yellow_pane = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE, 1);
        ItemMeta yellow_pane_meta = yellow_pane.getItemMeta();
        yellow_pane_meta.setDisplayName(ChatColor.YELLOW+"Подтвердите готовность (ЛКМ)");
        yellow_pane.setItemMeta(yellow_pane_meta);
        inv.setItem(45, yellow_pane);

        ItemStack red_pane = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        ItemMeta red_pane_meta = red_pane.getItemMeta();
        red_pane_meta.setDisplayName(ChatColor.RED+"Игрок не готов");
        red_pane.setItemMeta(red_pane_meta);
        inv.setItem(53, red_pane);
        return inv;
    }
    public TradeSession(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;

        this.invPlayer1 = fillInventory(player2.getName());
        this.invPlayer2 = fillInventory(player1.getName());

        player1.openInventory(invPlayer1);
        player2.openInventory(invPlayer2);
    }
    public Inventory getInventory(Player player) {
        return player.equals(player1) ? invPlayer1 : invPlayer2;
    }
    public Player getOtherPlayer(Player player) {
        return player.equals(player1) ? player2 : player1;
    }
    public Inventory getOtherInventory(Player player) {
        return player.equals(player1) ? getInventory(player2) : getInventory(player1);
    }
    public void setReadyPlayer(Player player, boolean readyPlayer) {
        if (player.equals(player1)) this.readyPlayer1 = readyPlayer;
        else this.readyPlayer2 = readyPlayer;
    }
    public boolean getReadyPlayer(Player player) {
        if (player.equals(player1)) return readyPlayer1;
        else return readyPlayer2;
    }

    public void setIsHasActiveTimer(boolean hasActiveTimer) {
        this.hasActiveTimer = hasActiveTimer;
    }

    public boolean getIsHasActiveTimer() {
        return hasActiveTimer;
    }
    public void setPlayerTaskID(Player player, int taskID) {
        if (player.equals(player1)) this.taskIdPlayer1 = taskID;
        else this.taskIdPlayer2 = taskID;
    }
}
