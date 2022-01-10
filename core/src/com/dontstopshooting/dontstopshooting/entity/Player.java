package com.dontstopshooting.dontstopshooting.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.dontstopshooting.dontstopshooting.GameScreen;
import com.dontstopshooting.dontstopshooting.Sounds;
import com.dontstopshooting.dontstopshooting.utils.HitBox;

public class Player extends PhysicsEntity implements Explosive {
    public enum DamageCause {
        AMMO("You are out of ammo"),
        FELL_PIT("You fell into a dark pit"),
        LEFT_WALL("You were too slow"),
        BAT("You caught COVID-19 from a bat"),
        EXPLOSION("You exploded"),
        CRYSTAL("You stabbed yourself in a crystal");

        public final String deathMessage;

        DamageCause(String msg) {
            this.deathMessage = msg;
        }
    }

    public static final float bulletPushAcc = 22;
    private float rpm = 480;

    private Vector2 cursor = Vector2.X;

    public TextureRegion texture = GameScreen.atlas.findRegion("player");
    public int health;
    public int score;
    public int bullets = 200;
    public DamageCause deathCause;
    private float invincibility = 0.0f;
    private static final float maxInvincibility = 3.0f;

    public Player(GameScreen screen, Vector2 loc) {
        super(screen, loc);
        hitBox.offset.x = 5;
        hitBox.width = 5;
        hitBox.height = 14;
        health = 3;
        score = 0;
    }

    public void shoot(Vector2 vec) {
        Sounds.gun.play();
        Vector2 bulletLocation = location.cpy().add(8, 8);
        bulletLocation.add(vec.cpy().scl(9f));
        new PlayerBullet(screen, bulletLocation.cpy(), vec);
        velocity.add(vec.cpy().scl(-bulletPushAcc));
        GameScreen.particles.createGunExplosion(bulletLocation.x, bulletLocation.y);
        score += 10;
        bullets--;
        if (bullets == 0) {
            kill(DamageCause.AMMO);
        }
    }

    @Override
    public void tick() {
        super.tick();
        velocity.scl(0.995f);
        invincibility -= GameScreen.SPT;

        if (screen.keyboardControls) {
            rpm = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? 960 : 480;

            int x = 0;
            int y = 0;
            if (Gdx.input.isKeyPressed(Input.Keys.A)) x++;
            if (Gdx.input.isKeyPressed(Input.Keys.D)) x--;
            if (Gdx.input.isKeyPressed(Input.Keys.S)) y++;
            if (Gdx.input.isKeyPressed(Input.Keys.W)) y--;
            if (x == 0 && y == 0) y = -1;
            cursor = new Vector2(x, y).nor();
        } else {
            rpm = Gdx.input.isButtonPressed(Input.Buttons.LEFT) ? 960 : 480;
            Vector3 screenCoords = screen.camera.project(new Vector3(location.x + 8, location.y + 8, 0));
            cursor = new Vector2(-screenCoords.x + Gdx.input.getX(), Gdx.graphics.getHeight() - screenCoords.y - Gdx.input.getY()).nor();
        }

        if (screen.getTick() % ((GameScreen.TPS*60)/rpm) == 0) {
            shoot(cursor);
        }

        for (Entity entity : screen.entities) {
            if (entity instanceof PlayerCollidable) {
                if (HitBox.intersect(this.hitBox, entity.hitBox)) {
                    ((PlayerCollidable) entity).onCollide(this);
                }
            }
        }

        if (location.y <= 0) kill(DamageCause.FELL_PIT);
    }

    @Override
    public void render(SpriteBatch batch) {

        float angle = (450.0f - cursor.angleDeg())%360.0f;

        int ang = (int) ((angle + 45.0f/2.0f)/45.0f);
        if (angle > 180.0f) {
            ang = (int) ((360.0f - angle + 45.0f/2.0f)/45.0f);
        }

        String name = "player" + (ang+1);
        texture = GameScreen.atlas.findRegion(name);
        if (invincibility <= 0.0f || Math.sin(Interpolation.pow2OutInverse.apply(1.0f - invincibility/maxInvincibility)*80.0f) > 0.0f) {
            if (angle < 180.0f) batch.draw(texture, (int)location.x, (int)location.y, texture.getRegionWidth(), texture.getRegionHeight());
            else batch.draw(texture, (int)location.x+texture.getRegionWidth(), (int)location.y, -texture.getRegionWidth(), texture.getRegionHeight());
        }

        super.render(batch);
    }

    @Override
    public void onExplode() {
        takeDamage(DamageCause.EXPLOSION);
    }

    public void takeDamage(DamageCause cause) {
        if (invincibility > 0.0f) {
            return;
        }
        this.health--;
        screen.playerCamera.shake(8.0f);
        if (this.health == 0) {
            kill(cause);
        } else {
            Sounds.hurt.play();
        }
        health = Math.max(health, 0);
        invincibility = maxInvincibility;
    }

    public void kill(DamageCause cause) {
        deathCause = cause;
        destroy();
        GameScreen.particles.createExplosion(location.x, location.y);
        Sounds.gameOver.play();
        this.health = 0;
    }
}
