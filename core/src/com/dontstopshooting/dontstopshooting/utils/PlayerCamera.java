package com.dontstopshooting.dontstopshooting.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.dontstopshooting.dontstopshooting.GameScreen;
import com.dontstopshooting.dontstopshooting.entity.Player;

public class PlayerCamera {

    private final GameScreen gameScreen;
    private Player player;
    public float startX;
    public final OrthographicCamera camera;

    public PlayerCamera(GameScreen gameScreen, Player player) {
        this.gameScreen = gameScreen;
        this.player = player;
        camera = new OrthographicCamera();
        this.startX = GameScreen.gameWidth/2.0f;
    }

    public void update() {

        if (this.player != null) {
            startX = Math.max(startX, startX + Gdx.graphics.getDeltaTime()*32.0f);
            startX = Math.max(startX, player.location.x + player.texture.getRegionWidth()/2f);
            if (player.location.x <= startX - GameScreen.gameWidth/2.0f) {
                GameScreen.particles.createExplosion(player.location.x, player.location.y);
                gameScreen.oldEntities.add(player);
                this.player = null;
            }
        }

        // center camera to player
        camera.setToOrtho(false, GameScreen.gameWidth, GameScreen.gameHeight);
        camera.translate(-GameScreen.gameWidth/2.0f, -GameScreen.gameHeight/2.0f);
        camera.translate((int) startX, GameScreen.gameHeight/2.0f);
        camera.update();
    }
}
