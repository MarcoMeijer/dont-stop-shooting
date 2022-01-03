package com.dontstopshooting.dontstopshooting.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Entity {
    void tick();
    void render(SpriteBatch batch);
}
