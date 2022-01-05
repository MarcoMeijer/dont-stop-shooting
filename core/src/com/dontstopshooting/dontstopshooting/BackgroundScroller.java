package com.dontstopshooting.dontstopshooting;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.List;

public class BackgroundScroller {

    public static class SingleBackground {
        final TextureRegion texture;
        final float speed;

        public SingleBackground(String textureName, float speed) {
            texture = GameScreen.atlas.findRegion(textureName);
            this.speed = speed;
        }
    }

    private final SingleBackground[] layers = new SingleBackground[] {
            new SingleBackground("caves1", 0.05f),
            new SingleBackground("caves2", 0.2f),
            new SingleBackground("caves3", 0.6f),
    };

    void render(SpriteBatch batch, float camaraX) {
        for (SingleBackground bg : layers) {
            float x = (camaraX*bg.speed)%bg.texture.getRegionWidth();
            batch.draw(bg.texture, -x, 0);
            batch.draw(bg.texture, bg.texture.getRegionWidth()-x, 0);
        }
        batch.flush();
    }
}
