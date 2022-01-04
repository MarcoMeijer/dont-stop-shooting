package com.dontstopshooting.dontstopshooting.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.dontstopshooting.dontstopshooting.GameScreen;
import com.dontstopshooting.dontstopshooting.utils.HitBox;

public class Player implements Entity {
    public static final float bulletPushAcc = 100;

    public final Vector2 location;
    private final Vector2 velocity = new Vector2();
    private final Vector2 acceleration = new Vector2();
    private final GameScreen screen;
    private final HitBox hitBox;

    public Player(GameScreen screen, Vector2 loc) {
        location = loc;
        screen.entities.add(this);
        this.screen = screen;
        this.hitBox = new HitBox(location, Vector2.Zero, 16, 16);
    }

    public void shoot(Vector2 vec) {
        screen.newEntities.add(new PlayerBullet(location.cpy(), vec));
        velocity.add(vec.cpy().scl(-bulletPushAcc));
    }

    @Override
    public void tick() {
        acceleration.set(0, -150);
        if (screen.getTick() % GameScreen.FPS == 0) {
            shoot(new Vector2(-Gdx.graphics.getWidth()/2f + Gdx.input.getX(), Gdx.graphics.getHeight()/2f - Gdx.input.getY()).nor());
        }
        velocity.scl(0.99f);

        Vector2 dv = acceleration.cpy().scl(GameScreen.SPF);
        Vector2 dx = velocity.cpy().scl(GameScreen.SPF).add(dv.cpy().scl(0.5f * GameScreen.SPF));

        // move in x direction
        location.add(dx.x , 0);
        if (screen.collisionCheck(hitBox)) {
            location.add(-dx.x, 0);
            while (!screen.collisionCheck(hitBox)) {
                location.add(Math.signum(dx.x), 0);
            }
            location.add(-Math.signum(dx.x), 0);
            velocity.x = 0;
            acceleration.x = 0;
        }

        // move in y direction
        location.add(0 , dx.y);
        if (screen.collisionCheck(hitBox)) {
            location.add(0, -dx.y);
            while (!screen.collisionCheck(hitBox)) {
                location.add(0, Math.signum(dx.y));
            }
            location.add(0, -Math.signum(dx.y));
            velocity.y = 0;
            acceleration.y = 0;
        }

        velocity.add(dv);
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion region = GameScreen.atlas.findRegion("player");
        batch.draw(region, (int)location.x, (int)location.y, 16, 16);
    }
}
