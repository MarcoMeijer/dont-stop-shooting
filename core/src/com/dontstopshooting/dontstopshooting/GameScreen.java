package com.dontstopshooting.dontstopshooting;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dontstopshooting.dontstopshooting.entity.Bomb;
import com.dontstopshooting.dontstopshooting.entity.Entity;
import com.dontstopshooting.dontstopshooting.entity.Player;
import com.dontstopshooting.dontstopshooting.utils.HitBox;
import jdk.internal.net.http.hpack.HPACK;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {

    public final static float FPS = 240f;
    public final static float SPF = 1f/FPS;
    public final static int gameWidth = 384;
    public final static int gameHeight = 216;

    private final FrameBuffer frameBuffer;
    private final Viewport viewport;
    public final static TextureAtlas atlas;
    public final static ParticleHandler particles;

    public final List<Entity> entities = new ArrayList<>();
    public final List<Entity> newEntities = new ArrayList<>();
    public final List<Entity> oldEntities = new ArrayList<>();
    private float time;
    private long tick = 0;
    public final OrthographicCamera camera;
    private final OrthographicCamera screenCamera;
    private final Player player;
    private final LevelMap level;

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
        packer.pack("circle", new Pixmap(Gdx.files.internal("circle.png")));
        packer.pack("bomb1", new Pixmap(Gdx.files.internal("bomb1.png")));
        packer.pack("bomb2", new Pixmap(Gdx.files.internal("bomb2.png")));
        packer.pack("bomb3", new Pixmap(Gdx.files.internal("bomb3.png")));
        packer.pack("bomb4", new Pixmap(Gdx.files.internal("bomb4.png")));
        atlas = packer.generateTextureAtlas(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest, false);
        packer.dispose();
        particles = new ParticleHandler();
    }

    public GameScreen() {
        player = new Player(this, new Vector2(100.0f, 150.0f));
        new Bomb(this, new Vector2(400.0f, 32.0f));
        new Bomb(this, new Vector2(500.0f, 32.0f));

        level = new LevelMap("level1.tmx");

        camera = new OrthographicCamera();
        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, gameWidth, gameHeight, false);

        screenCamera = new OrthographicCamera();
        screenCamera.setToOrtho(false, gameWidth, gameHeight);
        viewport = new FitViewport(gameWidth, gameHeight, screenCamera);
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            if (Gdx.graphics.isFullscreen()) {
                Gdx.graphics.setWindowedMode(1280, 720);
            } else {
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            }
        }


        // center camera to player
        camera.setToOrtho(false, gameWidth, gameHeight);
        camera.translate(-gameWidth/2.0f, -gameHeight/2.0f);
        camera.translate((int) Math.max(gameWidth/2.0f, player.location.x + player.texture.getRegionWidth()/2f), gameHeight/2.0f);
        camera.update();

        frameBuffer.begin();
        ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1.0f);

        level.render(camera);

        // render particles
        SpriteBatch batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        particles.draw(batch, delta);
        batch.end();

        frameBuffer.end();

        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.begin();
        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1.0f);
        batch.setProjectionMatrix(screenCamera.combined);
        Texture frameTexture = frameBuffer.getColorBufferTexture();
        frameTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        batch.draw(frameTexture, 0, gameHeight, gameWidth, -gameHeight);
        batch.flush();
        batch.setProjectionMatrix(camera.combined);
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
