package com.dontstopshooting.dontstopshooting.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.dontstopshooting.dontstopshooting.GameScreen;

public class Player extends PhysicsEntity implements Explosive {
    public static final float bulletPushAcc = 22;
    private float rpm = 960;

    private Vector2 cursor = Vector2.X;

    public TextureRegion texture = GameScreen.atlas.findRegion("player");

    public Player(GameScreen screen, Vector2 loc) {
        super(screen, loc);
        hitBox.offset.x = 5;
        hitBox.width = 5;
        hitBox.height = 15;
    }

    public void shoot(Vector2 vec) {
        Vector2 bulletLocation = location.cpy().add(8, 8);
        bulletLocation.add(vec.cpy().scl(9f));
        new PlayerBullet(screen, bulletLocation.cpy(), vec);
        velocity.add(vec.cpy().scl(-bulletPushAcc));
        GameScreen.particles.createGunExplosion(bulletLocation.x, bulletLocation.y);
    }

    @Override
    public void tick() {
        super.tick();
        velocity.scl(0.995f);
        if (screen.getTick() % ((GameScreen.TPS*60)/rpm) == 0) {
            shoot(cursor);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        Vector3 screenCoords = screen.camera.project(new Vector3(location.x + 8, location.y + 8, 0));
        cursor = new Vector2(-screenCoords.x + Gdx.input.getX(), Gdx.graphics.getHeight() - screenCoords.y - Gdx.input.getY()).nor();

        float angle = (450.0f - cursor.angleDeg())%360.0f;

        int ang = (int) ((angle + 45.0f/2.0f)/45.0f);
        if (angle > 180.0f) {
            ang = (int) ((360.0f - angle + 45.0f/2.0f)/45.0f);
        }

        String name = "player" + (ang+1);
        texture = GameScreen.atlas.findRegion(name);
        if (angle < 180.0f) batch.draw(texture, (int)location.x, (int)location.y, texture.getRegionWidth(), texture.getRegionHeight());
        else batch.draw(texture, (int)location.x+texture.getRegionWidth(), (int)location.y, -texture.getRegionWidth(), texture.getRegionHeight());
    }

    @Override
    public void onExplode() {
        System.out.println("player exploded");
    }
}
