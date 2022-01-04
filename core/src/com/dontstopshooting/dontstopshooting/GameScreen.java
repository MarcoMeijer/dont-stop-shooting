package com.dontstopshooting.dontstopshooting;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.dontstopshooting.dontstopshooting.entity.Entity;
import com.dontstopshooting.dontstopshooting.entity.Player;
import com.dontstopshooting.dontstopshooting.utils.HitBox;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {

    public final static float FPS = 240f;
    public final static float SPF = 1f/FPS;

    public final static TextureAtlas atlas;

    public final List<Entity> entities = new ArrayList<>();
    public final List<Entity> newEntities = new ArrayList<>();
    public final List<Entity> oldEntities = new ArrayList<>();
    private float time;
    private long tick = 0;
    private final OrthographicCamera camera;
    private final Player player;
    private LevelMap level;

    static {
        PixmapPacker packer = new PixmapPacker(512, 512, Pixmap.Format.RGBA8888, 2, true);
        packer.pack("player", new Pixmap(Gdx.files.internal("player.png")));
        packer.pack("player1", new Pixmap(Gdx.files.internal("player1.png")));
        packer.pack("player2", new Pixmap(Gdx.files.internal("player2.png")));
        packer.pack("player3", new Pixmap(Gdx.files.internal("player3.png")));
        packer.pack("player4", new Pixmap(Gdx.files.internal("player4.png")));
        packer.pack("player5", new Pixmap(Gdx.files.internal("player5.png")));
        packer.pack("player6", new Pixmap(Gdx.files.internal("player6.png")));
        packer.pack("player7", new Pixmap(Gdx.files.internal("player7.png")));
        packer.pack("player8", new Pixmap(Gdx.files.internal("player8.png")));
        packer.pack("bullet", new Pixmap(Gdx.files.internal("bullet.png")));
        atlas = packer.generateTextureAtlas(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest, false);
        packer.dispose();
    }

    public GameScreen() {
        player = new Player(this, new Vector2(100.0f, 150.0f));

        camera = new OrthographicCamera();

        level = new LevelMap("level1.tmx");

    }

    public long getTick() {
        return tick;
    }

    public boolean collisionCheck(HitBox hitBox) {
        return level.collisionCheck(hitBox);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        time += delta;
        while (time >= SPF) {
            tick++;
            entities.addAll(newEntities);
            entities.removeAll(oldEntities);
            newEntities.clear();
            oldEntities.clear();
            for (Entity entity : entities) {
                entity.tick();
            }

            time -= SPF;
        }

        ScreenUtils.clear(0.7f, 1.0f, 0.5f, 1.0f);

        // center camera to player
        camera.zoom = 1.0f;
        camera.setToOrtho(false, Gdx.graphics.getWidth()/4.0f, Gdx.graphics.getHeight()/4.0f);
        camera.translate(-Gdx.graphics.getWidth()/8.0f, -Gdx.graphics.getHeight()/8.0f);
        camera.translate((int) player.location.x, (int) player.location.y);
        camera.update();

        level.render(camera);

        // render entities
        SpriteBatch batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (Entity entity : entities) {
            entity.render(batch);
        }
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
    }
}
