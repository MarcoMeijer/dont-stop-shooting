package com.dontstopshooting.dontstopshooting.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.dontstopshooting.dontstopshooting.GameScreen;

public class MissileLauncher extends Entity implements BulletHittable {
    public static final int spawnFrequency = (int) (GameScreen.TPS * 7);
    private final Player target;
    private int age = spawnFrequency/2;
    private final TextureRegion sprite;

    public MissileLauncher(GameScreen screen, Vector2 location) {
        super(screen, location);
        target = screen.player;
        this.sprite = GameScreen.atlas.findRegion("launcher");
    }

    @Override
    public void tick() {
        if (++age == spawnFrequency) {
            age = 0;
            if (target != null)
                new Missile(screen, location.cpy().add(8, 8), new Vector2(Math.signum(target.location.x-location.x), 0), target);
        }
    }

    @Override
    public void onHit() {
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        batch.draw(sprite, location.x, location.y);
    }
}
