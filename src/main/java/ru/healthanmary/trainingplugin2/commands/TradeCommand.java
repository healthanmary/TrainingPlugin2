package ru.healthanmary.trainingplugin2.commands;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TradeCommand implements CommandExecutor {
    @Getter
    private Map<UUID, UUID> senderRecipientMap = new HashMap<>();
    private void fillInventory(String nick, Inventory inv) {
        ItemStack yellow_pane = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE, 1);
        ItemMeta pane_meta = yellow_pane.getItemMeta();
        pane_meta.setDisplayName(" ");
        yellow_pane.setItemMeta(pane_meta);


        for (int i = 4; i < 50; i += 9) {
            inv.setItem(i, yellow_pane);
        }
    }
    private void openInventories(String senderNick, String recipientNick) {
        Inventory senderInv = Bukkit.createInventory(null, 54, "Трейд с "+recipientNick);
        Inventory recipientInv = Bukkit.createInventory(null, 54, "Трейд с "+senderNick);
        fillInventory(senderNick, senderInv);
        fillInventory(recipientNick, recipientInv);

        Player sender = Bukkit.getPlayer(senderNick);
        Player recipient = Bukkit.getPlayer(recipientNick);
        sender.openInventory(senderInv);
        recipient.openInventory(recipientInv);
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Доступно только для игроков!");
            return true;
        }
        Player playerSender = (Player) sender;

        if (args.length == 0) {
            sender.sendMessage("Укажите игрока для обмена!");
            sender.sendMessage("/trade <Ник>");
            return true;
        }

        Player recipient = Bukkit.getPlayer(args[0]);
        if (recipient == null || !recipient.isOnline()) {
            sender.sendMessage("Игрок не найден!");
            return true;
        }

        recipient.sendMessage("Вам отправил трейд игрок " + sender.getName());
        recipient.sendMessage("Чтобы принять его, введите: " + ChatColor.AQUA+"/tradeaccept");


        Location locSender = playerSender.getLocation();
        Location locRecipient = recipient.getLocation();
        double distance = locSender.distance(locRecipient);

        if (distance > 10) {
            sender.sendMessage("Игрок должен находиться в 10-ти блоках от вас!");
            return true;
        }
        openInventories(sender.getName(), recipient.getName());
        return true;
    }
}
