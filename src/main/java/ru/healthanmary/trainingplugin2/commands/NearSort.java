package ru.healthanmary.trainingplugin2.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor @Getter
public class NearSort implements Comparable<NearSort> {
    private int distance;
    private String playerName;

    @Override
    public int compareTo(@NotNull NearSort o) {
        return Integer.compare(this.distance, o.distance);
    }
}
