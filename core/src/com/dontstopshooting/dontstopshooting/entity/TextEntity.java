package com.dontstopshooting.dontstopshooting.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.dontstopshooting.dontstopshooting.GameScreen;


public class TextEntity extends Entity {

    private String text;
    private static TextureRegion[] regions;
    private int lifeTime = 0;

    static {
        regions = new TextureRegion[10];
        for (int i=0; i<=9; i++) {
            regions[i] = GameScreen.atlas.findRegion("number"+i);
        }
    }

    public TextEntity(String text, GameScreen screen, Vector2 location) {
        super(screen, location);
        this.text = text;
    }

    @Override
    public void tick() {
        location.y += 0.1f;
        this.lifeTime += 1;
        if (this.lifeTime >= 200) {
            this.destroy();
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
        Vector2 pos = location.cpy();
        for (int i=0; i<text.length(); i++) {
            TextureRegion region = regions[text.charAt(i) - '0'];
            batch.draw(region, pos.x, pos.y);
            pos.x += region.getRegionWidth();
        }
    }
}
