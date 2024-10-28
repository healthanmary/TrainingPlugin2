package ru.healthanmary.trainingplugin2;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.healthanmary.trainingplugin2.commands.NearCommand;
import ru.healthanmary.trainingplugin2.commands.TradeAcceptCommand;
import ru.healthanmary.trainingplugin2.commands.TradeCommand;
import ru.healthanmary.trainingplugin2.suffixmanager.SuffixPlaceholder;

public final class TrainingPlugin2 extends JavaPlugin {

    private TrainingPlugin2 trainingPlugin2;

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
            new SuffixPlaceholder(this).register();
        getCommand("nearbyplayers").setExecutor(new NearCommand());
        getCommand("tradeaccept").setExecutor(new TradeAcceptCommand());
        getCommand("trade").setExecutor(new TradeCommand());
    }

    @Override
    public void onDisable() {
    }
}
