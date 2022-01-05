package com.dontstopshooting.dontstopshooting;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dontstopshooting.dontstopshooting.entity.Entity;
import com.dontstopshooting.dontstopshooting.entity.Player;
import com.dontstopshooting.dontstopshooting.ui.GameUi;
import com.dontstopshooting.dontstopshooting.utils.HitBox;
import com.dontstopshooting.dontstopshooting.utils.ParticleHandler;
import com.dontstopshooting.dontstopshooting.utils.PlayerCamera;

import java.util.HashSet;
import java.util.Set;

public class GameScreen implements Screen {

    public final static float TPS = 240f;
    public final static float SPT = 1f/TPS;
    public final static int gameWidth = 384;
    public final static int gameHeight = 216;
    public final static Skin skin;
    public MapGenerator mapGenerator;

    private final FrameBuffer frameBuffer;
    private final Viewport viewport;
    public final static TextureAtlas atlas;
    public final static ParticleHandler particles;

    public final Set<Entity> entities = new HashSet<>();
    public final Set<Entity> newEntities = new HashSet<>();
    public final Set<Entity> oldEntities = new HashSet<>();

    public boolean debugMode = false;
    private float time;
    private long tick = 0;
    public OrthographicCamera camera;
    public PlayerCamera playerCamera;
    private final OrthographicCamera screenCamera;
    private final BackgroundScroller backgroundScroller;
    public Player player;
    public Stage stage;

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
        packer.pack("bat1", new Pixmap(Gdx.files.internal("bat1.png")));
        packer.pack("bat2", new Pixmap(Gdx.files.internal("bat2.png")));
        packer.pack("bat3", new Pixmap(Gdx.files.internal("bat3.png")));
        packer.pack("bat4", new Pixmap(Gdx.files.internal("bat4.png")));
        packer.pack("bat5", new Pixmap(Gdx.files.internal("bat5.png")));
        packer.pack("bat6", new Pixmap(Gdx.files.internal("bat6.png")));
        packer.pack("bat7", new Pixmap(Gdx.files.internal("bat7.png")));
        packer.pack("bat8", new Pixmap(Gdx.files.internal("bat8.png")));
        packer.pack("bat9", new Pixmap(Gdx.files.internal("bat9.png")));
        packer.pack("bomb1", new Pixmap(Gdx.files.internal("bomb1.png")));
        packer.pack("bomb2", new Pixmap(Gdx.files.internal("bomb2.png")));
        packer.pack("bomb3", new Pixmap(Gdx.files.internal("bomb3.png")));
        packer.pack("bomb4", new Pixmap(Gdx.files.internal("bomb4.png")));
        packer.pack("bombjump", new Pixmap(Gdx.files.internal("bombjump.png")));
        packer.pack("bombready", new Pixmap(Gdx.files.internal("bombready.png")));
        packer.pack("bombexplode1", new Pixmap(Gdx.files.internal("bombexplode1.png")));
        packer.pack("bombexplode2", new Pixmap(Gdx.files.internal("bombexplode2.png")));
        packer.pack("plank", new Pixmap(Gdx.files.internal("plank.png")));
        packer.pack("caves1", new Pixmap(Gdx.files.internal("caves1.png")));
        packer.pack("caves2", new Pixmap(Gdx.files.internal("caves2.png")));
        packer.pack("caves3", new Pixmap(Gdx.files.internal("caves3.png")));
        atlas = packer.generateTextureAtlas(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest, false);
        packer.dispose();
        particles = new ParticleHandler();
        skin = new Skin(Gdx.files.internal("skin/skin.json"));
        for (Texture texture : skin.getAtlas().getTextures()) {
            texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        }
    }

    public GameScreen() {
        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, gameWidth, gameHeight, false);
        screenCamera = new OrthographicCamera();
        screenCamera.setToOrtho(false, gameWidth, gameHeight);
        viewport = new FitViewport(gameWidth, gameHeight, screenCamera);
        backgroundScroller = new BackgroundScroller();
        stage = new Stage();
        stage.addActor(new GameUi(skin));
        restart();
    }

    public long getTick() {
        return tick;
    }

    public boolean collisionCheck(HitBox hitBox) {
        return mapGenerator.collisionCheck(hitBox);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        time += delta;
        while (time >= SPT) {
            tick++;
            entities.addAll(newEntities);
            entities.removeAll(oldEntities);
            newEntities.clear();
            oldEntities.clear();
            for (Entity entity : entities) {
                entity.tick();
            }

            time -= SPT;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F11)) {
            if (Gdx.graphics.isFullscreen()) {
                Gdx.graphics.setWindowedMode(1280, 720);
            } else {
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) restart();
        if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) debugMode = !debugMode;

        ScreenUtils.clear(56f/255, 45f/255, 107f/255, 1.0f);
        SpriteBatch batch = new SpriteBatch();
        batch.begin();

        batch.setProjectionMatrix(screenCamera.combined);
        backgroundScroller.render(batch, playerCamera.startX);

        frameBuffer.begin();
        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 0.0f);

        playerCamera.update();


        mapGenerator.render(camera);

        // render particles
        batch.setProjectionMatrix(camera.combined);
        particles.draw(batch, delta);
        batch.end();

        frameBuffer.end();

        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.begin();
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
        stage.act(delta);
        stage.draw();
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

    public void restart() {
        entities.clear();
        player = new Player(this, new Vector2(200.0f, 150.0f));
        mapGenerator = new MapGenerator(this);
        playerCamera = new PlayerCamera(this, player);
        camera = playerCamera.camera;
    }
}
