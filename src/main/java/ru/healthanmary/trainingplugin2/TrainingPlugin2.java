package ru.healthanmary.trainingplugin2;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ru.healthanmary.trainingplugin2.commands.NearCommand;
import ru.healthanmary.trainingplugin2.tradecmd.TradeAcceptCommand;
import ru.healthanmary.trainingplugin2.tradecmd.TradeCommand;
import ru.healthanmary.trainingplugin2.suffixmanager.SuffixPlaceholder;

public final class TrainingPlugin2 extends JavaPlugin {
    private TradeCommand tradeCommand;
    @Override
    public void onEnable() {
        tradeCommand = new TradeCommand();
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
            new SuffixPlaceholder(this).register();
        getCommand("nearbyplayers").setExecutor(new NearCommand());
        getCommand("tradeaccept").setExecutor(new TradeAcceptCommand(tradeCommand));
        getCommand("trade").setExecutor(tradeCommand);
    }
    @Override
    public void onDisable() {
    }
}
