package com.dontstopshooting.dontstopshooting.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dontstopshooting.dontstopshooting.GameScreen;
import com.dontstopshooting.dontstopshooting.utils.HitBox;

public abstract class Entity {

    protected final GameScreen screen;
    public Vector2 location;
    public final HitBox hitBox;

    public Entity(GameScreen screen, Vector2 location) {
        screen.newEntities.add(this);
        this.screen = screen;
        this.location = location;
        this.hitBox = new HitBox(location, new Vector2(0.0f, 0.0f), 0.0f, 0.0f);
    }

    public void onSpawn() {
    }

    public abstract void tick();

    public void render(SpriteBatch batch) {
        if (screen.hitBoxes) hitBox.render(batch);
    }

    public void destroy() {
        screen.oldEntities.add(this);
    }
}
