package com.dontstopshooting.dontstopshooting.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.dontstopshooting.dontstopshooting.GameScreen;

public class PlayerBullet implements Entity {
    public static final float bulletSpeed = 500;
    public static final long maxAge = (long) (GameScreen.FPS * 3);

    private final GameScreen screen;
    private long age = 0;
    private final Vector2 location;
    private final Vector2 velocity;
    private final TextureRegion sprite;

    public PlayerBullet(GameScreen screen, Vector2 location, Vector2 vec) {
        screen.newEntities.add(this);
        this.screen = screen;
        this.location = location;
        this.velocity = vec.cpy().scl(bulletSpeed);
        this.sprite = GameScreen.atlas.findRegion("bullet");
    }

    @Override
    public void tick() {
        location.add(velocity.cpy().scl(GameScreen.SPF));
        if (++age >= maxAge) screen.oldEntities.add(this);
    }

    @Override
    public void render(SpriteBatch batch) {
        float angle = this.velocity.angleDeg();
        batch.draw(sprite, location.x, location.y, 2.0f, 3.0f, 4.0f, 6.0f, 1.0f, 1.0f, angle - 90.0f);
    }
}
