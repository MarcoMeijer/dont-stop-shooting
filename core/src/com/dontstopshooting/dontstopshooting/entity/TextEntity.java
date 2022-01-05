package com.dontstopshooting.dontstopshooting.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.dontstopshooting.dontstopshooting.GameScreen;


public class TextEntity extends Entity {

    private String text;

    public TextEntity(String text, GameScreen screen, Vector2 location) {
        super(screen, location);
        this.text = text;
    }

    @Override
    public void tick() {
        location.y += 0.05f;
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
        Label label = new Label(text, screen.ui.getSkin());
        label.setPosition(location.x, location.y);
        label.draw(batch, 1.0f);
    }
}
