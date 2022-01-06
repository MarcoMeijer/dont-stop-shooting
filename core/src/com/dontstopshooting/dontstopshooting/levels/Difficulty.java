package com.dontstopshooting.dontstopshooting.levels;

import com.badlogic.gdx.utils.JsonValue;
import com.dontstopshooting.dontstopshooting.GameScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Difficulty {

    private final List<String> levels;
    private final int maxRepeat;
    private int repeated = 0;

    public Difficulty(JsonValue value) {
        this.levels = new ArrayList<>();
        JsonValue levels = value.get("levels");
        for (int i=0; i<levels.size; i++) {
            JsonValue level = levels.get(i);
            this.levels.add(level.asString());
        }

        JsonValue repeat = value.get("repeat");
        int repeatMin = repeat.getInt(0);
        int repeatMax = repeat.getInt(1);
        maxRepeat = repeatMin + new Random().nextInt(repeatMax - repeatMin + 1);
    }

    public String getNext() {
        repeated++;
        return this.levels.get(new Random().nextInt(levels.size()));
    }

    public boolean isDone() {
        return repeated >= maxRepeat;
    }

}
