package com.dontstopshooting.dontstopshooting.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.dontstopshooting.dontstopshooting.GameScreen;

public class Missile extends PhysicsEntity implements BulletHittable {
    public static final float speed = 80;
    public static final float rotationAcc = (float) (Math.PI*2*10);

    private final Player target;

    private float rotationSpeed = 0;

    private static final TextureRegion texture = GameScreen.atlas.findRegion("missile");

    public Missile(GameScreen screen, Vector2 location, Vector2 vec, Player target) {
        super(screen, location);

        this.target = target;

        gravityForce.vec.setZero();

        this.hitBox.width = 12;
        this.hitBox.height = 12;

        velocity.set(vec.cpy().scl(speed));
    }

    @Override
    public void tick() {
        super.tick();

        float realAcc = Math.signum(velocity.crs(target.hitBox.getCenter().mulAdd(hitBox.getCenter(), -1))) * rotationAcc;
        System.out.println(realAcc);
        float drs = realAcc * GameScreen.SPT;
        float dr = rotationSpeed * GameScreen.SPT + drs * 0.5f * GameScreen.SPT;
        rotationSpeed += drs;
        rotationSpeed *= 0.99f;
        velocity.nor();
        velocity.scl(speed);
        velocity.rotateRad(dr);
    }

    @Override
    public void onHit() {

    }

    @Override
    public void render(SpriteBatch batch) {
        float angle = this.velocity.angleDeg();
        batch.draw(texture, hitBox.getCenter().x-16, hitBox.getCenter().y-8, 16.0f, 8.0f, 32.0f, 16.0f, 1.0f, 1.0f, angle + 180.0f);
        Vector2 vec = velocity.cpy().nor().scl(-12).add(hitBox.getCenter());
        GameScreen.particles.createGunExplosion(vec.x, vec.y);
        super.render(batch);
    }
}