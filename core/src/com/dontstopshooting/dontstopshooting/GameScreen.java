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
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dontstopshooting.dontstopshooting.entity.*;
import com.dontstopshooting.dontstopshooting.levels.MapGenerator;
import com.dontstopshooting.dontstopshooting.ui.DebugUI;
import com.dontstopshooting.dontstopshooting.ui.GameUi;
import com.dontstopshooting.dontstopshooting.utils.HitBox;
import com.dontstopshooting.dontstopshooting.utils.ParticleHandler;
import com.dontstopshooting.dontstopshooting.utils.PlayerCamera;

import java.util.HashSet;
import java.util.Set;

public class GameScreen implements Screen {

    public final static float TPS = 240f;
    public final static float SPT = 1f / TPS;
    public final static int gameWidth = 384;
    public final static int gameHeight = 216;
    public final static Skin skin;
    public MapGenerator mapGenerator;

    public static final float spawnRadius = 250;
    public static final float despawnRadius = 300;

    private final FrameBuffer frameBuffer;
    private final Viewport viewport;
    public final static TextureAtlas atlas;
    public final static ParticleHandler particles;

    public final Set<Entity> entities = new HashSet<>();
    public final Set<Entity> newEntities = new HashSet<>();
    public final Set<Entity> oldEntities = new HashSet<>();

    public boolean debugMode = false;
    public boolean hitBoxes = false;
    private float time;
    private long tick = 0;
    public OrthographicCamera camera;
    public PlayerCamera playerCamera;
    private final OrthographicCamera screenCamera;
    private final BackgroundScroller backgroundScroller;
    public Player player;
    public Stage stage;
    public GameUi ui;
    public DebugUI debugUi;
    public SpriteBatch batch;
    public int highScore = 0;

    public boolean musicMute = false;
    public boolean keyboardControls = false;

    static {
        PixmapPacker packer = new PixmapPacker(1024, 1024, Pixmap.Format.RGBA8888, 2, true);
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
        for (int i=1; i<=9; i++) {
            packer.pack("bat"+i, new Pixmap(Gdx.files.internal("bat"+i+".png")));
            packer.pack("batdamage"+i, new Pixmap(Gdx.files.internal("batdamage"+i+".png")));
        }
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
        packer.pack("heart", new Pixmap(Gdx.files.internal("heart.png")));
        packer.pack("heartempty", new Pixmap(Gdx.files.internal("heartempty.png")));
        packer.pack("healthbar0", new Pixmap(Gdx.files.internal("healthbar0.png")));
        packer.pack("healthbar1", new Pixmap(Gdx.files.internal("healthbar1.png")));
        packer.pack("healthbar2", new Pixmap(Gdx.files.internal("healthbar2.png")));
        packer.pack("healthbar3", new Pixmap(Gdx.files.internal("healthbar3.png")));
        packer.pack("bigbullet", new Pixmap(Gdx.files.internal("bigbullet.png")));
        packer.pack("missile", new Pixmap(Gdx.files.internal("missile.png")));
        packer.pack("launcher", new Pixmap(Gdx.files.internal("launcher.png")));
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
        ui = new GameUi(skin, this);
        debugUi = new DebugUI(skin, this);
        debugUi.setVisible(false);
        stage.addActor(debugUi);
        stage.addActor(ui);
        batch = new SpriteBatch();
        restart();
    }

    public long getTick() {
        return tick;
    }

    public boolean collisionCheck(HitBox hitBox) {
        return mapGenerator.collisionCheck(hitBox);
    }

    public void explosion(Vector2 location, float radius, float blast) {
        GameScreen.particles.createExplosion(location.x, location.y);
        playerCamera.shake(8.0f);

        float radius2 = radius*radius;
        for (Entity e : entities) {
            if (!oldEntities.contains(e)) {
                if (e.hitBox.getCenter().dst2(location) <= radius2) {
                    if (e instanceof Explosive) ((Explosive) e).onExplode();
                }
                if (e instanceof PhysicsEntity) {
                    PhysicsEntity physicsEntity = (PhysicsEntity)e;
                    Vector2 vec = physicsEntity.hitBox.getCenter().sub(location);
                    Vector2 blastForce = vec.cpy().nor().scl(blast/vec.len2());
                    //System.out.println(blastForce);
                    physicsEntity.velocity.add(blastForce);
                }
            }
        }

        int iRadius = (int)Math.ceil(radius/16);
        int iRadius2 = iRadius*iRadius;
        GridPoint2 center = new GridPoint2((int) location.x/16, (int) location.y/16);
        for (int dx=-iRadius; dx<=iRadius; dx++) {
            for (int dy=-iRadius; dy<=iRadius; dy++) {
                if (dx*dx+dy*dy <= iRadius2) mapGenerator.onHit(center.cpy().add(dx, dy));
            }
        }
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        time += delta;
        while (time >= SPT) {
            tick++;
            for (Entity entity : newEntities.toArray(new Entity[0])) {
                if (entity.hitBox.getCenter().x >= playerCamera.startX - spawnRadius) {
                    entities.add(entity);
                    newEntities.remove(entity);
                    entity.onSpawn();
                } if (entity.hitBox.getCenter().x <= playerCamera.startX - despawnRadius) newEntities.remove(entity);
            }
            entities.removeAll(oldEntities);
            oldEntities.clear();
            for (Entity entity : entities) {
                if (entity.hitBox.getCenter().x <= playerCamera.startX - despawnRadius) entity.destroy();
                else entity.tick();
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
        if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
            debugMode = !debugMode;
            debugUi.setVisible(debugMode);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
            hitBoxes = !hitBoxes;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            musicMute = !musicMute;
            BackgroundMusic.setMute(musicMute);
        }

        ui.update();

        ScreenUtils.clear(56f / 255, 45f / 255, 107f / 255, 1.0f);
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
        batch.flush();

        frameBuffer.end();

        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
        debugUi.update();

        BackgroundMusic.update();
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
        if (player != null && player.score > highScore) highScore = player.score;
        newEntities.clear();
        oldEntities.clear();
        player = new Player(this, new Vector2(200.0f, 150.0f));
        //new MissileLauncher(this, player.location.cpy().add(100, 0));
        mapGenerator = new MapGenerator(this);
        playerCamera = new PlayerCamera(this, player);
        camera = playerCamera.camera;
        ui.player = player;
        BackgroundMusic.begin();
        BackgroundMusic.setMute(musicMute);
    }
}
