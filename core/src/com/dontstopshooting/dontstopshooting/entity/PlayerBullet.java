package com.dontstopshooting.dontstopshooting.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.dontstopshooting.dontstopshooting.GameScreen;
import com.dontstopshooting.dontstopshooting.utils.HitBox;

public class PlayerBullet extends Entity {
    public static final float bulletSpeed = 500;
    public static final long maxAge = (long) (GameScreen.TPS * 1.5);

    private long age = 0;
    private final Vector2 velocity;
    private final TextureRegion sprite;

    public PlayerBullet(GameScreen screen, Vector2 location, Vector2 vec) {
        super(screen, location);
        this.location = location;
        this.velocity = vec.cpy().scl(bulletSpeed);
        this.sprite = GameScreen.atlas.findRegion("bullet");
    }

    @Override
    public void tick() {
        location.add(velocity.cpy().scl(GameScreen.SPT));
        if (++age >= maxAge) {
            screen.oldEntities.add(this);
            return;
        }

        for (Entity entity : screen.entities) {
            if (entity instanceof BulletHittable) {
                if (HitBox.intersect(entity.hitBox, this.hitBox)) {
                    BulletHittable hittable = (BulletHittable) entity;
                    hittable.onHit();
                    screen.oldEntities.add(this);
                    return;
                }
            }
        }

        if (screen.collisionCheck(this.hitBox)) {
            GameScreen.particles.createBullet(location.x, location.y);
            screen.mapGenerator.onHit(new GridPoint2((int)location.x/16, (int)location.y/16));
            screen.oldEntities.add(this);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        float angle = this.velocity.angleDeg();
        batch.draw(sprite, location.x, location.y, 2.0f, 3.0f, 4.0f, 6.0f, 1.0f, 1.0f, angle - 90.0f);
    }
}
