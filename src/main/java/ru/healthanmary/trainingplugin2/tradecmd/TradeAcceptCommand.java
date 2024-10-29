package ru.healthanmary.trainingplugin2.tradecmd;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TradeAcceptCommand implements CommandExecutor {
    private TradeCommand tradeCommand;
    private Map<UUID, UUID> tradeRequestsMap;
    public TradeAcceptCommand(TradeCommand tradeCommand) {
        this.tradeCommand = tradeCommand;
        this.tradeRequestsMap = tradeCommand.getTradeRequests();
    }
    private void fillInventory(String otherNick, Inventory inv) {
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
        yellow_pane_meta.setDisplayName(ChatColor.GREEN+"Подтвердите готовность (ЛКМ)");
        yellow_pane.setItemMeta(yellow_pane_meta);
        inv.setItem(45, yellow_pane);

        ItemStack red_pane = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        ItemMeta red_pane_meta = red_pane.getItemMeta();
        red_pane_meta.setDisplayName(ChatColor.RED+"Игрок не готов");
        red_pane.setItemMeta(red_pane_meta);
        inv.setItem(53, red_pane);
    }
    private void openInventories(Player tradeSender, Player tradeRecieper) {
        String senderNick = tradeSender.getName();
        String recipientNick = tradeRecieper.getName();
        Inventory senderInv = Bukkit.createInventory(null, 54, "Трейд с "+recipientNick);
        Inventory recipientInv = Bukkit.createInventory(null, 54, "Трейд с "+senderNick);
        fillInventory(recipientNick, senderInv);
        fillInventory(senderNick, recipientInv);

        tradeSender.openInventory(senderInv);
        tradeRecieper.openInventory(recipientInv);
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Только для игроков!");
            return true;
        }

        Player tradeReciepent = (Player) sender;
        Player tradeSender = Bukkit.getPlayer(tradeRequestsMap.get(tradeReciepent.getUniqueId()));

        /*null выводит*/ System.out.println("get recipient - " + tradeRequestsMap.get(tradeReciepent.getUniqueId()));
        if (tradeRequestsMap.get(tradeReciepent.getUniqueId()) == null) {
            tradeReciepent.sendMessage("У вас нет активных заявок на трейд!");
            return true;
        }
        if (!tradeSender.isOnline()) {
            tradeReciepent.sendMessage("Отправитель трейда оффлайн!");
            return true;
        }

        openInventories(tradeSender, tradeReciepent);

        return true;
    }
}
