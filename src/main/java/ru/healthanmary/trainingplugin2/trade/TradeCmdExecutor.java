package ru.healthanmary.trainingplugin2.trade;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TradeCmdExecutor implements CommandExecutor {
    private final TradeAcceptCmdExecutor tradeAcceptCmdExecutor;

    public TradeCmdExecutor(TradeAcceptCmdExecutor tradeAcceptCmdExecutor) {
        this.tradeAcceptCmdExecutor = tradeAcceptCmdExecutor;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        String prefix = ChatColor.GREEN+"Трейд "+ChatColor.GRAY+"» "+ChatColor.WHITE;
        if (!(sender instanceof Player)) {
            sender.sendMessage(prefix+"Доступно только для игроков!");
            return true;
        }
        Player playerSender = (Player) sender;

        if (args.length == 0) {
            sender.sendMessage(prefix+"Укажите игрока для обмена!");
            sender.sendMessage("/trade <Ник>");
            return true;
        }

        Player recipient = Bukkit.getPlayer(args[0]);
        if (recipient == null || !recipient.isOnline()) {
            sender.sendMessage(prefix+"Игрок не найден!");
            return true;
        }
        if (recipient.getName().equals(sender.getName())) {
            sender.sendMessage(prefix+"Вы не можете отправить трейд самому себе!");
            return true;
        }

        Location locSender = playerSender.getLocation();
        Location locRecipient = recipient.getLocation();
        double distance = locSender.distance(locRecipient);
        if (distance > 10) {
            sender.sendMessage(prefix+"Игрок должен находиться в 10-ти блоках от вас!");
            return true;
        }
        tradeAcceptCmdExecutor.addRequest(recipient.getUniqueId(), playerSender.getUniqueId());

        sender.sendMessage(prefix+"Вы отправили запрос на трейд игроку "+ ChatColor.AQUA+recipient.getName());
        recipient.sendMessage(prefix+"Вам отправил запрос на трейд игрок " +ChatColor.AQUA+ sender.getName());
        recipient.sendMessage("Чтобы принять его, введите: " + ChatColor.AQUA+"/tradeaccept");
        return true;
    }
}
