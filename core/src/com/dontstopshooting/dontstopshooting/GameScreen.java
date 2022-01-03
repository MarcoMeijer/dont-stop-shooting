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

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {

    public final static float FPS = 240f;
    public final static float SPF = 1f/FPS;

    public final static TextureAtlas atlas;

    public List<Entity> entities;
    private float time;
    private OrthographicCamera camera;
    private Player player;

    static {
        PixmapPacker packer = new PixmapPacker(512, 512, Pixmap.Format.RGBA8888, 2, true);
        packer.pack("player", new Pixmap(Gdx.files.internal("player.png")));
        atlas = packer.generateTextureAtlas(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest, false);
        packer.dispose();
    }

    public GameScreen() {
        player = new Player(new Vector2(0.0f, 0.0f));

        entities = new ArrayList<>();
        entities.add(player);

        camera = new OrthographicCamera();

        // center camera to player
        camera.zoom = 1.0f;
        camera.setToOrtho(false);
        camera.translate(-Gdx.graphics.getWidth()/2.0f, -Gdx.graphics.getHeight()/2.0f);
        camera.translate(player.location);
        camera.zoom = 1.0f/4.0f;
        camera.update();

    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        time += delta;
        while (time >= SPF) {
            for (Entity entity : entities) {
                entity.tick();
            }
            time -= SPF;
        }

        ScreenUtils.clear(0.7f, 1.0f, 0.5f, 1.0f);
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
