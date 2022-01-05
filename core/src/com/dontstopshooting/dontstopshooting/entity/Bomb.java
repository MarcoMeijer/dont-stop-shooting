package com.dontstopshooting.dontstopshooting.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.dontstopshooting.dontstopshooting.GameScreen;

import java.util.Random;

public class Bomb extends PhysicsEntity implements BulletHittable {
    public static final float speed = 20;
    public static final int randomJump = (int) (7*GameScreen.TPS);
    private static final int maxFuseTime = (int) GameScreen.TPS;

    private final Animation<TextureRegion> walkingAnimation;
    private final Animation<TextureRegion> fusingAnimation;
    private final TextureRegion jump;
    private float time = 0;
    private int fuse = -1;
    private boolean direction;

    public Bomb(GameScreen screen, Vector2 location) {
        super(screen, location);
        this.hitBox.width = 11.0f;
        this.hitBox.height = 16.0f;
        this.walkingAnimation = new Animation<>(.08f,
                GameScreen.atlas.findRegion("bomb1"),
                GameScreen.atlas.findRegion("bomb2"),
                GameScreen.atlas.findRegion("bomb3"),
                GameScreen.atlas.findRegion("bomb4")
        );
        this.fusingAnimation = new Animation<>(.05f,
                GameScreen.atlas.findRegion("bombexplode1"),
                GameScreen.atlas.findRegion("bombexplode2")
        );
        jump = GameScreen.atlas.findRegion("bombjump");
        direction = new Random().nextBoolean();
        velocity.set(direction? speed: -speed, 0);
    }

    public void explode() {
        GameScreen.particles.createExplosion(location.x, location.y);
        screen.oldEntities.add(this);
        screen.playerCamera.shake(8.0f);
    }

    @Override
    public void tick() {
        super.tick();
        if (fuse != -1) {
            if (fuse-- == 0) explode();
            return;
        }
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

        TextureRegion texture;
        if (fuse != -1) texture = fusingAnimation.getKeyFrame(Interpolation.slowFast.apply(0, maxFuseTime, maxFuseTime-fuse), true);
        else if (Math.abs(velocity.y) == 0) texture = walkingAnimation.getKeyFrame(time, true);
        else texture = jump;

        batch.draw(texture, (int)location.x, (int)location.y);
    }


    @Override
    public void onHit() {
        if (fuse != -1) return;
        fuse = (int) (GameScreen.TPS*0.25f);
        velocity.x = 0;
    }
}
