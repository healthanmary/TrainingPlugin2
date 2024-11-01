package ru.healthanmary.trainingplugin2;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.healthanmary.trainingplugin2.commands.NearCommand;
import ru.healthanmary.trainingplugin2.suffixmanager.SuffixPlaceholder;
import ru.healthanmary.trainingplugin2.trade.TradeAcceptCmdExecutor;
import ru.healthanmary.trainingplugin2.trade.TradeCmdExecutor;
import ru.healthanmary.trainingplugin2.trade.TradeListener;
import ru.healthanmary.trainingplugin2.trade.TradeManager;

public final class TrainingPlugin2 extends JavaPlugin {
    private TradeManager tradeManager;
    private TradeAcceptCmdExecutor tradeAcceptCmdExecutor;
    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
            new SuffixPlaceholder(this).register();
        tradeManager = new TradeManager();
        tradeAcceptCmdExecutor = new TradeAcceptCmdExecutor(tradeManager);
        getCommand("nearbyplayers").setExecutor(new NearCommand());
        getCommand("trade").setExecutor(new TradeCmdExecutor(tradeAcceptCmdExecutor));
        getCommand("tradeaccept").setExecutor(tradeAcceptCmdExecutor);
        getServer().getPluginManager().registerEvents(new TradeListener(tradeManager), this);
    }
    @Override
    public void onDisable() {
    }
}
