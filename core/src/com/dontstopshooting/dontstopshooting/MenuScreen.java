package com.dontstopshooting.dontstopshooting;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.dontstopshooting.dontstopshooting.ui.MenuUI;
import com.dontstopshooting.dontstopshooting.utils.PlayerCamera;

public class MenuScreen implements Screen {

    private final Stage stage;
    private final BackgroundScroller scroller;
    private final SpriteBatch batch;
    private final OrthographicCamera screenCamera;
    private float camX = 0.0f;

    public MenuScreen(Game game) {
        stage = new Stage();
        stage.addActor(new MenuUI(game, GameScreen.skin));
        scroller = new BackgroundScroller();
        batch = new SpriteBatch();
        screenCamera = new OrthographicCamera();
        screenCamera.setToOrtho(false, GameScreen.gameWidth, GameScreen.gameHeight);
        Gdx.input.setInputProcessor(stage);
        BackgroundMusic.begin();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F11)) {
            if (Gdx.graphics.isFullscreen()) {
                Gdx.graphics.setWindowedMode(1280, 720);
            } else {
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            BackgroundMusic.muteMusic = !BackgroundMusic.muteMusic;
        }
        BackgroundMusic.update(false);
        camX += 32.0f*delta;
        ScreenUtils.clear(56f / 255, 45f / 255, 107f / 255, 1.0f);
        batch.setProjectionMatrix(screenCamera.combined);
        batch.begin();
        scroller.render(batch, camX);
        batch.end();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int w, int h) {
        stage.getViewport().update(w, h, true);
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
