package com.dontstopshooting.dontstopshooting.entity;

import com.badlogic.gdx.math.Vector2;
import com.dontstopshooting.dontstopshooting.GameScreen;

public abstract class PhysicsEntity extends Entity {
    public static final float gravity = 250;
    public static final float bouncyRate = 0.1f;

    protected final Vector2 velocity = new Vector2();
    private final Vector2 acceleration = new Vector2(0, -gravity);

    public PhysicsEntity(GameScreen screen, Vector2 location) {
        super(screen, location);
    }

    @Override
    public void tick() {
        Vector2 dv = acceleration.cpy().scl(GameScreen.SPT);
        Vector2 dx = velocity.cpy().scl(GameScreen.SPT).add(dv.cpy().scl(0.5f * GameScreen.SPT));
        velocity.add(dv);

        // move in x direction
        location.add(dx.x , 0);
        if (screen.collisionCheck(hitBox)) {
            location.add(-dx.x, 0);
            while (!screen.collisionCheck(hitBox)) {
                location.add(Math.signum(dx.x), 0);
            }
            location.add(-Math.signum(dx.x), 0);
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
            velocity.y = (Math.abs(velocity.x)*0.33f + Math.abs(velocity.y)) * -bouncyRate * Math.signum(velocity.y);
        }
    }
}
