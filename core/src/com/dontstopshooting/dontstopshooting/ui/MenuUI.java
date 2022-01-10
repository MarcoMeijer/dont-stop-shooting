package com.dontstopshooting.dontstopshooting.ui;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dontstopshooting.dontstopshooting.GameScreen;

public class MenuUI extends Table {

    public MenuUI(Game game, Skin skin) {
        super();
        this.setSkin(skin);
        this.setFillParent(true);

        Image logo = new Image(GameScreen.atlas.findRegion("logo"));

        Table buttons = new Table();
        buttons.defaults().pad(5);

        TextButton playButton = new TextButton("Play game", this.getSkin());
        buttons.row();
        buttons.add(playButton);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen());
            }
        });

        TextButton settingsButton = new TextButton("Settings", this.getSkin());
        buttons.row();
        buttons.add(settingsButton);

        TextButton quitButton = new TextButton("Quit", this.getSkin());
        buttons.row();
        buttons.add(quitButton);

        this.row();
        this.add(logo).size(logo.getWidth()*2.0f, logo.getHeight()*2.0f);
        this.row();
        this.add(buttons).center().pad(60);

    }
}
