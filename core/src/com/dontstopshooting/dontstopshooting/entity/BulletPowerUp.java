package com.dontstopshooting.dontstopshooting.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.dontstopshooting.dontstopshooting.GameScreen;

public class BulletPowerUp extends PhysicsEntity implements PlayerCollidable {

    private final TextureRegion sprite;
    private float time = 0.0f;

    public BulletPowerUp(GameScreen screen, Vector2 location) {
        super(screen, location);
        sprite = GameScreen.atlas.findRegion("bigbullet");
        hasGravity = false;
        hitBox.width = 8;
        hitBox.height = 16;
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        time += Gdx.graphics.getDeltaTime(); // todo modulo
        int x = (int)location.x, y = (int)location.y;
        batch.draw(sprite, x, y + (float) (Math.sin(time*8.0f)*4.0f));
    }

    @Override
    public void onCollide(Player player) {
        player.bullets += 25;
        player.score += 1000;
        this.destroy();
    }
}
