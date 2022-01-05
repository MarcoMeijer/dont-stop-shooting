package com.dontstopshooting.dontstopshooting.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.dontstopshooting.dontstopshooting.GameScreen;

import java.util.Random;

public class Bomb extends PhysicsEntity implements BulletHittable, Explosive {
    public static final float speed = 20;
    public static final int randomJump = (int) (7*GameScreen.TPS);
    public static final int preJumpTime = (int) (0.4f*GameScreen.TPS);
    private static final int maxFuseTime = (int) GameScreen.TPS;
    private static final float explosionRadius = 52;

    enum State {
        WALKING,
        PRE_JUMP,
        FUSING
    }

    private final Animation<TextureRegion> walkingAnimation;
    private final Animation<TextureRegion> fusingAnimation;
    private final TextureRegion falling;
    private final TextureRegion preJump;
    private float time = 0;
    private int fuseTimer = -1;
    private int preJumpTimer = -1;
    private State state = State.WALKING;
    private boolean direction;

    public Bomb(GameScreen screen, Vector2 location) {
        super(screen, location);
        this.hitBox.width = 11.0f;
        this.hitBox.height = 14.0f;

        this.walkingAnimation = new Animation<>(.08f,
                GameScreen.atlas.findRegion("bomb1"),
                GameScreen.atlas.findRegion("bomb2"),
                GameScreen.atlas.findRegion("bomb3"),
                GameScreen.atlas.findRegion("bomb4")
        );
        this.fusingAnimation = new Animation<>(.1f,
                GameScreen.atlas.findRegion("bombexplode1"),
                GameScreen.atlas.findRegion("bombexplode2")
        );
        this.preJump = GameScreen.atlas.findRegion("bombready");
        falling = GameScreen.atlas.findRegion("bombjump");

        direction = new Random().nextBoolean();
        velocity.set(direction? speed: -speed, 0);
    }

    public void explode() {
        GameScreen.particles.createExplosion(location.x, location.y);
        screen.oldEntities.add(this);
        screen.playerCamera.shake(8.0f);

        for (Entity e : screen.entities) {
            if (!screen.oldEntities.contains(e)) {
                if (e instanceof Explosive && e.hitBox.getCenter().dst(hitBox.getCenter()) <= explosionRadius) {
                    ((Explosive) e).onExplode();
                }
            }
        }

        GridPoint2 center = new GridPoint2((int) hitBox.getCenter().x/16, (int) hitBox.getCenter().y/16);
        for (int dx=-2; dx<=2; dx++) {
            for (int dy=-2; dy<=2; dy++) {
                screen.mapGenerator.onHit(center.cpy().add(dx, dy));
            }
        }
    }

    @Override
    public void onExplode() {
        if (state == State.FUSING) return;
        fuseTimer = (int) (GameScreen.TPS*(0.6 + new Random().nextFloat()*0.35f));
        velocity.x = 0;
        state = State.FUSING;
    }

    @Override
    public void tick() {
        super.tick();
        switch (state) {
            case PRE_JUMP:
                if (--preJumpTimer == 0) {
                    state = State.WALKING;
                    velocity.y = gravity*0.8f;
                    velocity.x = (direction? 1f: -1f) * speed;
                }
                break;
            case WALKING:
                if (velocity.x == 0 && velocity.y == 0) {
                    direction = !direction;
                    velocity.x = (direction? 1f: -1f) * speed;
                }

                if (velocity.y != 0) velocity.x *= 0.999f;
                else velocity.x = (direction? 1f: -1f) * speed;

                if (Math.abs(velocity.y) == 0 && new Random().nextInt(randomJump) == 1) {
                    state = State.PRE_JUMP;
                    preJumpTimer = preJumpTime;
                    velocity.x = 0;
                }
                break;
            case FUSING:
                if (--fuseTimer == 0) explode();
                break;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        time += Gdx.graphics.getDeltaTime(); // todo modulo

        TextureRegion texture = null;
        switch (state) {
            case FUSING:
                texture = fusingAnimation.getKeyFrame(Interpolation.pow2OutInverse.apply((maxFuseTime-fuseTimer)/(float)maxFuseTime), true);
                break;
            case WALKING:
                if (velocity.y == 0) texture = walkingAnimation.getKeyFrame(time, true);
                else texture = falling;
                break;
            case PRE_JUMP:
                texture = preJump;
        }

        batch.draw(texture, (int)location.x, (int)location.y);
    }


    @Override
    public void onHit() {
        if (state == State.FUSING) {
            fuseTimer -= 10;
            fuseTimer = Math.max(fuseTimer, 1);
            return;
        }
        fuseTimer = (int) (GameScreen.TPS*(0.4 + new Random().nextFloat()*0.1f));
        velocity.x = 0;
        state = State.FUSING;
    }
}
