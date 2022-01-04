package com.dontstopshooting.dontstopshooting.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.dontstopshooting.dontstopshooting.GameScreen;

public class Bomb extends Entity implements BulletHittable {

    private final TextureRegion sprite;

    public Bomb(GameScreen screen, Vector2 location) {
        super(screen, location);
        this.hitBox.width = 16.0f;
        this.hitBox.height = 16.0f;
        this.location = location.cpy();
        this.sprite = GameScreen.atlas.findRegion("bomb1");
    }

    @Override
    public void tick() {
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(sprite, location.x, location.y);
    }


    @Override
    public void onHit() {
        GameScreen.particles.createExplosion(location.x, location.y);
        screen.oldEntities.add(this);
    }
}
