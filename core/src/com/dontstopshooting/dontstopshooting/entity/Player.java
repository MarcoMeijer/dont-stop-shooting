package com.dontstopshooting.dontstopshooting.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.dontstopshooting.dontstopshooting.GameScreen;
import com.dontstopshooting.dontstopshooting.utils.HitBox;

public class Player extends Entity {
    public static final float bulletPushAcc = 40;
    public static final float gravity = 250;
    public static final float friction = 0.97f;
    public static final float bouncyRate = 0.1f;
    public static final float bouncyThreshold = 13f;
    private float rpm = 960;

    private final Vector2 velocity = new Vector2();
    private final Vector2 acceleration = new Vector2();
    private final GameScreen screen;
    private Vector2 cursor = Vector2.Zero;

    public TextureRegion texture = GameScreen.atlas.findRegion("player");

    public Player(GameScreen screen, Vector2 loc) {
        super(loc);
        hitBox.offset.x = 5;
        hitBox.width = 5;
        hitBox.height = 15;
        screen.entities.add(this);
        this.screen = screen;
    }

    public void shoot(Vector2 vec) {
        new PlayerBullet(screen, location.cpy().add(hitBox.width/2f, hitBox.height/2f), vec);
        velocity.add(vec.cpy().scl(-bulletPushAcc));
    }

    @Override
    public void tick() {
        acceleration.set(0, -gravity);
        if (screen.getTick() % ((GameScreen.FPS*60)/rpm) == 0) {
            shoot(cursor);
        }
        velocity.scl(0.995f);

        Vector2 dv = acceleration.cpy().scl(GameScreen.SPF);
        Vector2 dx = velocity.cpy().scl(GameScreen.SPF).add(dv.cpy().scl(0.5f * GameScreen.SPF));
        velocity.add(dv);

        // move in x direction
        location.add(dx.x , 0);
        if (screen.collisionCheck(hitBox)) {
            location.add(-dx.x, 0);
            while (!screen.collisionCheck(hitBox)) {
                location.add(Math.signum(dx.x), 0);
            }
            location.add(-Math.signum(dx.x), 0);
            velocity.y *= friction;
            velocity.x = 0;
        }

        // move in y direction
        location.add(0 , dx.y);
        if (screen.collisionCheck(hitBox)) {
            location.add(0, -dx.y);
            while (!screen.collisionCheck(hitBox)) {
                location.add(0, Math.signum(dx.y));
            }
            location.add(0, -Math.signum(dx.y));
            if (Math.abs(velocity.y) <= bouncyThreshold) velocity.y = 0;
            else velocity.y = (Math.abs(velocity.x)*0.33f + Math.abs(velocity.y)) * -bouncyRate * Math.signum(velocity.y);
            velocity.x *= friction;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        Vector3 screenCoords = screen.camera.project(new Vector3(location.x + hitBox.width/2.0f, location.y + hitBox.height/2.0f, 0));
        cursor = new Vector2(-screenCoords.x + Gdx.input.getX(), Gdx.graphics.getHeight() - screenCoords.y - Gdx.input.getY()).nor();

        float angle = (450.0f - cursor.angleDeg())%360.0f;

        int ang = (int) ((angle + 45.0f/2.0f)/45.0f);
        if (angle > 180.0f) {
            ang = (int) ((360.0f - angle + 45.0f/2.0f)/45.0f);
        }

        String name = "player" + (ang+1);
        texture = GameScreen.atlas.findRegion(name);
        if (angle < 180.0f) batch.draw(texture, (int)location.x, (int)location.y, texture.getRegionWidth(), texture.getRegionHeight());
        else batch.draw(texture, (int)location.x+texture.getRegionWidth(), (int)location.y, -texture.getRegionWidth(), texture.getRegionHeight());
    }
}
