package ru.healthanmary.trainingplugin2;

import org.bukkit.plugin.java.JavaPlugin;
import ru.healthanmary.trainingplugin2.commands.NearCommand;

public final class TrainingPlugin2 extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("nearbyplayers").setExecutor(new NearCommand());
    }

    @Override
    public void onDisable() {
    }
}
