package com.dontstopshooting.dontstopshooting.ui;


import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class GameUi extends Table {

    public GameUi(Skin skin) {
        super(skin);
        this.setFillParent(true);

        Label scoreLabel = new Label("SCORE: 000000", getSkin());
        scoreLabel.setFontScale(2.0f);
        this.row();
        this.add(scoreLabel).top().left().pad(10.0f).expand();
    }
}
