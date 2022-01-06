package com.dontstopshooting.dontstopshooting.entity;

import com.badlogic.gdx.math.Vector2;
import com.dontstopshooting.dontstopshooting.GameScreen;

import java.util.ArrayList;
import java.util.List;

public abstract class PhysicsEntity extends Entity {
    public static final float gravity = 175;
    public static final float bouncyRate = 0.22f;

    public static class ForceComp {
        public Vector2 vec;

        public ForceComp(Vector2 vec) {
            this.vec = vec;
        }
    }

    public final Vector2 velocity = new Vector2();
    protected ForceComp gravityForce = new ForceComp(new Vector2(0, -gravity));

    protected List<ForceComp> forces = new ArrayList<>();

    public PhysicsEntity(GameScreen screen, Vector2 location) {
        super(screen, location);
        forces.add(gravityForce);
    }

    @Override
    public void tick() {
        Vector2 acceleration = new Vector2();
        for (ForceComp force : forces) {
            acceleration.add(force.vec);
        }
        acceleration.scl(1);
        Vector2 dv = acceleration.cpy().scl(GameScreen.SPT);
        Vector2 dx = velocity.cpy().scl(GameScreen.SPT).add(dv.cpy().scl(0.5f * GameScreen.SPT));
        velocity.add(dv);

        // move in x direction
        if (!screen.collisionCheck(hitBox)) {
            location.add(dx.x, 0);
            if (screen.collisionCheck(hitBox)) {
                location.add(-dx.x, 0);
                while (!screen.collisionCheck(hitBox)) {
                    location.add(Math.signum(dx.x), 0);
                }
                location.add(-Math.signum(dx.x), 0);
                velocity.x = 0;
            }
        }

        // move in y direction
        if (!screen.collisionCheck(hitBox)) {
            location.add(0, dx.y);
            if (screen.collisionCheck(hitBox)) {
                location.add(0, -dx.y);
                while (!screen.collisionCheck(hitBox)) {
                    location.add(0, Math.signum(dx.y));
                }
                location.add(0, -Math.signum(dx.y));
                if (!gravityForce.vec.isZero() && Math.abs(velocity.y) >= 3) velocity.y *= -bouncyRate;
                else velocity.y = 0;
            }
        }
    }
}
