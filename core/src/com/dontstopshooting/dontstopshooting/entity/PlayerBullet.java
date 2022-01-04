package com.dontstopshooting.dontstopshooting.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dontstopshooting.dontstopshooting.GameScreen;

public class PlayerBullet implements Entity {
    public static final float bulletSpeed = 5;

    private final Vector2 location;
    private final Vector2 velocity;

    public PlayerBullet(Vector2 location, Vector2 vec) {
        this.location = location;
        this.velocity = vec.cpy().scl(bulletSpeed);
    }

    @Override
    public void tick() {
        location.add(velocity.cpy().scl(GameScreen.SPF));
    }

    @Override
    public void render(SpriteBatch batch) {

    }
}
