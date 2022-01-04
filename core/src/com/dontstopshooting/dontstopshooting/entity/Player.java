package com.dontstopshooting.dontstopshooting.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.dontstopshooting.dontstopshooting.GameScreen;
import com.dontstopshooting.dontstopshooting.utils.HitBox;

public class Player implements Entity {
    public final Vector2 location;
    private final Vector2 velocity = new Vector2();
    private final GameScreen screen;
    private final HitBox hitBox;

    public Player(GameScreen screen, Vector2 loc) {
        location = loc;
        this.screen = screen;
        this.hitBox = new HitBox(location, Vector2.Zero, 16, 16);
    }

    @Override
    public void tick() {
        Vector2 acceleration = new Vector2(0, -20);

        Vector2 dv = acceleration.cpy().scl(GameScreen.SPF);
        Vector2 dx = velocity.cpy().scl(GameScreen.SPF).add(dv.cpy().scl(0.5f * GameScreen.SPF));

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            dv.x = -1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            dv.x =  1;
        }

        location.add(dx);
        if (screen.collisionCheck(hitBox)) {
            location.add(-dx.x, -dx.y);
            velocity.set(0, 0);
        }
        velocity.add(dv);
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion region = GameScreen.atlas.findRegion("player");
        batch.draw(region, (int)location.x, (int)location.y, 16, 16);
    }
}
