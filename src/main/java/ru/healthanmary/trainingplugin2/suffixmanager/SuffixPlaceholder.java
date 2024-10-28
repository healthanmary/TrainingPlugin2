package ru.healthanmary.trainingplugin2.suffixmanager;


import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.jetbrains.annotations.NotNull;
import ru.healthanmary.trainingplugin2.TrainingPlugin2;

public class SuffixPlaceholder extends PlaceholderExpansion {
    private final TrainingPlugin2 trainingPlugin2;

    public SuffixPlaceholder(TrainingPlugin2 trainingPlugin2) {
        this.trainingPlugin2 = trainingPlugin2;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "training";
    }

    @Override
    public @NotNull String getAuthor() {
        return "healthanmary";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }
}
