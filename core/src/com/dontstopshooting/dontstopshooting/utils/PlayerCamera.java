package com.dontstopshooting.dontstopshooting.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.dontstopshooting.dontstopshooting.GameScreen;
import com.dontstopshooting.dontstopshooting.entity.Player;

import java.util.Random;

public class PlayerCamera {

    private final GameScreen gameScreen;
    private Player player;
    public float startX;
    public final OrthographicCamera camera;
    public float shakeFactor;

    public PlayerCamera(GameScreen gameScreen, Player player) {
        this.gameScreen = gameScreen;
        this.player = player;
        camera = new OrthographicCamera();
        this.startX = GameScreen.gameWidth/2.0f;
    }

    public void shake(float amount) {
        shakeFactor += amount;
    }

    public void update() {
        shakeFactor *= 0.95f;

        if (player.health > 0) {
            startX = Math.max(startX, startX + Gdx.graphics.getDeltaTime()*32.0f);
            startX = Math.max(startX, player.location.x + player.texture.getRegionWidth()/2f);
            if (player.location.x <= startX - GameScreen.gameWidth/2.0f) {
                player.die();
            }
        }

        // center camera to player
        camera.setToOrtho(false, GameScreen.gameWidth, GameScreen.gameHeight);
        camera.translate(-GameScreen.gameWidth/2.0f, -GameScreen.gameHeight/2.0f);
        camera.translate((int) startX, GameScreen.gameHeight/2.0f + 46.0f);
        Random random = new Random();
        camera.translate((int) ((random.nextFloat() - 0.5f)*shakeFactor), (int) ((random.nextFloat()-0.5f)*shakeFactor));
        camera.update();
    }
}
