package com.dontstopshooting.dontstopshooting.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dontstopshooting.dontstopshooting.GameScreen;
import com.dontstopshooting.dontstopshooting.MenuScreen;

public class GameOverUI extends Table {

    public GameOverUI(Skin skin, GameScreen screen) {
        super();
        this.setSkin(skin);
        this.setFillParent(true);

        Table buttons = new Table();
        buttons.defaults().pad(5);

        Image gameOver = new Image(GameScreen.atlas.findRegion("gameover"));
        this.row();
        this.add(gameOver).size(gameOver.getWidth()*2.0f, gameOver.getHeight()*2.0f);

        this.row();
        this.add(new Label("Your score is "+screen.player.score, skin, "big"));

        if (screen.highScore < screen.player.score) {
            this.row();
            this.add(new Label("New highscore", skin, "highscore"));
        }

        TextButton playButton = new TextButton("Retry", this.getSkin());
        buttons.row();
        buttons.add(playButton);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.restart();
            }
        });

        TextButton quitButton = new TextButton("Main menu", this.getSkin());
        buttons.row();
        buttons.add(quitButton);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.game.setScreen(new MenuScreen(screen.game));
            }
        });

        this.row();
        this.add(buttons).center().pad(60);

    }
}
