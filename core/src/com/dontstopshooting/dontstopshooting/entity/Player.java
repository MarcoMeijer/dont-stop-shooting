package com.dontstopshooting.dontstopshooting.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.dontstopshooting.dontstopshooting.GameScreen;
import com.dontstopshooting.dontstopshooting.utils.HitBox;

public class Player implements Entity {
    public static final float bulletPushAcc = 40;
    public static final float gravity = 150;

    public final Vector2 location;
    private final Vector2 velocity = new Vector2();
    private final Vector2 acceleration = new Vector2();
    private final GameScreen screen;
    public final HitBox hitBox;
    private float rpm = 4;
    private Vector2 cursor = Vector2.Zero;

    public static final TextureRegion texture = GameScreen.atlas.findRegion("player");

    public Player(GameScreen screen, Vector2 loc) {
        location = loc;
        screen.entities.add(this);
        this.screen = screen;
        this.hitBox = new HitBox(location, new Vector2(5, 0), 5, 15);
    }

    public void shoot(Vector2 vec) {
        new PlayerBullet(screen, location.cpy().add(hitBox.width/2f, hitBox.height/2f), vec);
        velocity.add(vec.cpy().scl(-bulletPushAcc));
    }

    @Override
    public void tick() {
        acceleration.set(0, -gravity);
        if (screen.getTick() % (GameScreen.FPS/rpm) == 0) {
            shoot(cursor);
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
        cursor = new Vector2(-Gdx.graphics.getWidth()/2f + Gdx.input.getX(), Gdx.graphics.getHeight()/2f - Gdx.input.getY()).nor();
        if (cursor.x > 0) batch.draw(texture, (int)location.x, (int)location.y, texture.getRegionWidth(), texture.getRegionHeight());
        else batch.draw(texture, (int)location.x+ texture.getRegionWidth(), (int)location.y, -texture.getRegionWidth(), texture.getRegionHeight());
    }
}
