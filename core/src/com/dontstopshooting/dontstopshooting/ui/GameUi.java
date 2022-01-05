package com.dontstopshooting.dontstopshooting.ui;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.dontstopshooting.dontstopshooting.GameScreen;
import com.dontstopshooting.dontstopshooting.entity.Player;

import java.util.ArrayList;

public class GameUi extends Table {

    public Player player;
    private final Image healthBar;
    private final Label scoreLabel;

    public GameUi(Skin skin) {
        super(skin);
        this.player = null;

        this.setFillParent(true);

        // health bar
        healthBar = new Image(GameScreen.atlas.findRegion("healthbar3"));
        this.row();
        this.add(healthBar).left().size(236, 44).pad(8.0f);

        // score
        scoreLabel = new Label("SCORE: 000.000.000", getSkin());
        scoreLabel.setFontScale(2.0f);
        this.row();
        this.add(scoreLabel).bottom().left().pad(8.0f).expand();
    }

    public void update() {
        this.healthBar.setDrawable(new SpriteDrawable(new Sprite(GameScreen.atlas.findRegion("healthbar"+player.health))));
        scoreLabel.setText(String.format("SCORE: %09d", player.score));
    }
}
