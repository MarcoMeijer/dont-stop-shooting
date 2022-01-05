package com.dontstopshooting.dontstopshooting.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.dontstopshooting.dontstopshooting.GameScreen;

public class Bat extends PhysicsEntity implements Explosive, BulletHittable {
    public static final float speed = 20;
    public static final int startHealth = 6;

    private final boolean vertical;
    private float direction = 1;
    private int health = startHealth;

    private float time = 0;

    private final Animation<TextureRegion> flyingAnimation;

    public Bat(GameScreen screen, Vector2 location, boolean vertical) {
        super(screen, location);

        this.flyingAnimation = new Animation<>(.1f,
                GameScreen.atlas.findRegion("bat1"),
                GameScreen.atlas.findRegion("bat2"),
                GameScreen.atlas.findRegion("bat3"),
                GameScreen.atlas.findRegion("bat4"),
                GameScreen.atlas.findRegion("bat5"),
                GameScreen.atlas.findRegion("bat6"),
                GameScreen.atlas.findRegion("bat7"),
                GameScreen.atlas.findRegion("bat8"),
                GameScreen.atlas.findRegion("bat9")
        );

        hitBox.width = 15;
        hitBox.height = 11;
        hitBox.offset.set(4, 5);

        hasGravity = false;
        this.vertical = vertical;
        if (vertical) velocity.set(0, direction*speed);
        else velocity.set(direction*speed, 0);
    }

    @Override
    public void tick() {
        super.tick();

        if (velocity.x == 0 && velocity.y == 0) direction *= -1;

        if (vertical) velocity.y = direction*speed;
        else velocity.x = direction*speed;
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        time += Gdx.graphics.getDeltaTime();

        batch.draw(flyingAnimation.getKeyFrame(time, true), (int)location.x, (int)location.y);
    }

    public void kill() {
        screen.oldEntities.add(this);
        GameScreen.particles.createGunExplosion(hitBox.getCenter().x, hitBox.getCenter().y);
    }

    @Override
    public void onHit() {
        if (--health == 0) kill();
    }

    @Override
    public void onExplode() {
        kill();
    }
}