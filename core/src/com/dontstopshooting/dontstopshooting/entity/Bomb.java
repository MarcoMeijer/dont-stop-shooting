package com.dontstopshooting.dontstopshooting.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.dontstopshooting.dontstopshooting.GameScreen;

public class Bomb extends PhysicsEntity implements BulletHittable {
    public static final float speed = 60;

    private final Animation<TextureRegion> sprite;
    private float time = 0;
    private boolean direction = false;

    public Bomb(GameScreen screen, Vector2 location) {
        super(screen, location);
        this.hitBox.width = 16.0f;
        this.hitBox.height = 16.0f;
        this.sprite = new Animation<>(.1f,
                GameScreen.atlas.findRegion("bomb1"),
                GameScreen.atlas.findRegion("bomb2"),
                GameScreen.atlas.findRegion("bomb3"),
                GameScreen.atlas.findRegion("bomb4")
        );
        velocity.set(-speed, 0);
    }

    @Override
    public void tick() {
        super.tick();
        if (velocity.x == 0) {
            direction = !direction;
            velocity.x = (direction? 1f: -1f) * speed;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        time += Gdx.graphics.getDeltaTime(); // todo modulo
        batch.draw(sprite.getKeyFrame(time, true), location.x, location.y);
    }


    @Override
    public void onHit() {
        GameScreen.particles.createExplosion(location.x, location.y);
        screen.oldEntities.add(this);
    }
}
