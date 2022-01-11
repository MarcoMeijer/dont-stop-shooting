package com.dontstopshooting.dontstopshooting.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.dontstopshooting.dontstopshooting.GameScreen;
import com.dontstopshooting.dontstopshooting.Sounds;

public class HeartPowerUp extends Entity implements PlayerCollidable {

    private final TextureRegion sprite;
    private float time = 0.0f;

    public HeartPowerUp(GameScreen screen, Vector2 location) {
        super(screen, location);
        sprite = GameScreen.atlas.findRegion("heart");
        hitBox.width = 15;
        hitBox.height = 15;
    }

    @Override
    public void tick() {
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        time += Gdx.graphics.getDeltaTime();
        int x = (int)location.x, y = (int)location.y;
        batch.draw(sprite, x, y + (float) (Math.sin(time*6.0f)*2.0f));
    }

    @Override
    public void onCollide(Player player) {
        if (player.health == 3) return;
        player.health++;
        this.destroy();
        Sounds.heartPickup.play();
        screen.createPoints(hitBox.getCenter(), 200);
    }
}
