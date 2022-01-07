package com.dontstopshooting.dontstopshooting.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.dontstopshooting.dontstopshooting.GameScreen;
import com.dontstopshooting.dontstopshooting.utils.HitBox;

public class Missile extends PhysicsEntity implements BulletHittable {
    public static final float speed = 80;
    public static final float rotationAcc = (float) (Math.PI*2*10);
    public static final float spawnRadius = 250;
    public static final float despawnRadius = 600;

    private final Player target;

    private float rotationSpeed = 0;

    private boolean spawned = false;

    private static final TextureRegion texture = GameScreen.atlas.findRegion("missile");

    public Missile(GameScreen screen, Vector2 location, Vector2 vec, Player target) {
        super(screen, location);

        this.target = target;

        gravityForce.vec.setZero();

        this.hitBox.width = 6;
        this.hitBox.height = 6;

        velocity.set(vec.cpy().scl(speed));
    }

    @Override
    public void onWallCollide(boolean vertical) {
        explode();
    }

    @Override
    public void tick() {
        Vector2 vecToTarget = target.hitBox.getCenter().sub(hitBox.getCenter());
        if (spawned) {
            if (vecToTarget.len2() < despawnRadius * despawnRadius) {
                super.tick();

                for (Entity e : screen.entities) {
                    if (e instanceof PhysicsEntity && e != this) {
                        if (HitBox.intersect(e.hitBox, hitBox)) {
                            explode();
                            return;
                        }
                    }
                }

                float realAcc = Math.signum(velocity.cpy().crs(vecToTarget)) * rotationAcc;
                spawned = true;
                float drs = realAcc * GameScreen.SPT;
                float dr = rotationSpeed * GameScreen.SPT + drs * 0.5f * GameScreen.SPT;
                rotationSpeed += drs;
                rotationSpeed *= 0.99f;
                velocity.rotateRad(dr);
            } else {
                destroy();
                spawned = false;
            }
        } else if (vecToTarget.len2() < spawnRadius * spawnRadius) {
            spawned = true;
        }
    }

    public void explode() {
        destroy();
        screen.explosion(hitBox.getCenter(), 25, 10000);
    }

    @Override
    public void onHit() {
        explode();
    }

    @Override
    public void render(SpriteBatch batch) {
        if (spawned) {
            float angle = this.velocity.angleDeg();
            batch.draw(texture, hitBox.getCenter().x - 8, hitBox.getCenter().y - 4, 8.0f, 4.0f, 16.0f, 9.0f, 1.0f, 1.0f, angle + 180.0f);
            Vector2 vec = velocity.cpy().nor().scl(-7).add(hitBox.getCenter());
            GameScreen.particles.createGunExplosion(vec.x, vec.y);
            super.render(batch);
        }
    }
}
