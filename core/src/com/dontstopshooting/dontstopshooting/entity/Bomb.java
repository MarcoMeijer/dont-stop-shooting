package com.dontstopshooting.dontstopshooting.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.dontstopshooting.dontstopshooting.GameScreen;

import java.util.Random;

public class Bomb extends PhysicsEntity implements BulletHittable {
    public static final float speed = 20;
    public static final int randomJump = (int) (7*GameScreen.TPS);

    private final Animation<TextureRegion> walkingAnimation;
    private final TextureRegion jump;
    private float time = 0;
    private boolean direction;

    public Bomb(GameScreen screen, Vector2 location) {
        super(screen, location);
        this.hitBox.width = 11.0f;
        this.hitBox.height = 16.0f;
        this.walkingAnimation = new Animation<>(.1f,
                GameScreen.atlas.findRegion("bomb1"),
                GameScreen.atlas.findRegion("bomb2"),
                GameScreen.atlas.findRegion("bomb3"),
                GameScreen.atlas.findRegion("bomb4")
        );
        jump = GameScreen.atlas.findRegion("bombjump");
        direction = new Random().nextBoolean();
        velocity.set(direction? speed: -speed, 0);
    }

    @Override
    public void tick() {
        super.tick();
        if (velocity.x == 0) {
            direction = !direction;
            velocity.x = (direction? 1f: -1f) * speed;
        }
        if (velocity.y != 0) velocity.x *= 0.999f;
        else velocity.x = (direction? 1f: -1f) * speed;
        if (Math.abs(velocity.y) == 0 && new Random().nextInt(randomJump) == 1) velocity.y = gravity*0.8f;
    }

    @Override
    public void render(SpriteBatch batch) {
        time += Gdx.graphics.getDeltaTime(); // todo modulo
        if (Math.abs(velocity.y) == 0) batch.draw(walkingAnimation.getKeyFrame(time, true), location.x, location.y);
        else batch.draw(jump, location.x, location.y);
    }


    @Override
    public void onHit() {
        GameScreen.particles.createExplosion(location.x, location.y);
        screen.oldEntities.add(this);
        screen.playerCamera.shake(8.0f);
    }
}
