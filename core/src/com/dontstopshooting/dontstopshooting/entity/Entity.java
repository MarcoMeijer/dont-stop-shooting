package com.dontstopshooting.dontstopshooting.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dontstopshooting.dontstopshooting.utils.HitBox;

public abstract class Entity {

    public Vector2 location;
    public HitBox hitBox;

    public Entity(Vector2 location) {
        this.location = location;
        this.hitBox = new HitBox(location, new Vector2(0.0f, 0.0f), 0.0f, 0.0f);
    }

    public abstract void tick();

    public abstract void render(SpriteBatch batch);
}
