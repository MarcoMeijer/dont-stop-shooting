package com.dontstopshooting.dontstopshooting.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.dontstopshooting.dontstopshooting.GameScreen;

public class Player implements Entity {
    private final Vector2 location;
    private final Vector2 velocity = new Vector2();

    public Player(Vector2 loc) {
        location = loc;
    }

    @Override
    public void tick() {
        Vector2 acceleration = new Vector2(0, -10);

        Vector2 dv = acceleration.cpy().scl(GameScreen.SPF);
        Vector2 dx = velocity.cpy().scl(GameScreen.SPF).add(dv.cpy().scl(0.5f * GameScreen.SPF));

        location.add(dx);
        velocity.add(dv);
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion region = GameScreen.atlas.findRegion("player");
        batch.draw(region, location.x, location.y, 64, 64);
    }
}
