package ru.healthanmary.trainingplugin2;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.healthanmary.trainingplugin2.commands.NearCommand;
import ru.healthanmary.trainingplugin2.suffixmanager.SuffixPlaceholder;
import ru.healthanmary.trainingplugin2.trade.*;

public final class TrainingPlugin2 extends JavaPlugin {
    private TradeManager tradeManager;
    private TradeAcceptCmdExecutor tradeAcceptCmdExecutor;
    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
            new SuffixPlaceholder(this).register();
        tradeManager = new TradeManager(this);
        tradeAcceptCmdExecutor = new TradeAcceptCmdExecutor(tradeManager);
        getCommand("nearbyplayers").setExecutor(new NearCommand());
        getCommand("trade").setExecutor(new TradeCmdExecutor(tradeAcceptCmdExecutor));
        getCommand("tradeaccept").setExecutor(tradeAcceptCmdExecutor);
        getServer().getPluginManager().registerEvents(new TradeListener(tradeManager, this), this);
    }
    @Override
    public void onDisable() {
    }
}
