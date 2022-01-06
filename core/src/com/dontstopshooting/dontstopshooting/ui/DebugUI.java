package com.dontstopshooting.dontstopshooting.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.dontstopshooting.dontstopshooting.GameScreen;

public class DebugUI extends Table {

    private Label fpsLabel;
    private Label drawCalls;
    private GameScreen screen;
    private int lastRenderCalls = 0;

    public DebugUI(Skin skin, GameScreen screen) {
        this.setSkin(skin);
        this.setFillParent(true);
        this.screen = screen;

        // fps
        fpsLabel = new Label("FPS:", getSkin());
        this.row();
        this.add(fpsLabel).right().top().expandX();

        // draw calls
        drawCalls = new Label("Draw calls:", getSkin());
        this.row();
        this.add(drawCalls).right().top().expand();
    }

    public void update() {
        fpsLabel.setText(String.format("FPS: %d", Gdx.graphics.getFramesPerSecond()));
        drawCalls.setText(String.format("Draw calls: %d", screen.batch.totalRenderCalls - lastRenderCalls));
        lastRenderCalls = screen.batch.totalRenderCalls;
    }
}
