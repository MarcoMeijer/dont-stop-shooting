package com.dontstopshooting.dontstopshooting;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class BackgroundMusic {
    private final Music start = Gdx.audio.newMusic(Gdx.files.internal("soundstart.mp3")), end = Gdx.audio.newMusic(Gdx.files.internal("soundend.mp3"));

    public void destroy() {
        start.dispose();
        end.dispose();
    }

    public BackgroundMusic() {
        start.play();
    }

    public void update() {
        if (!start.isPlaying() && !end.isPlaying()) {
            end.play();
            end.setLooping(true);
            start.dispose();
        }
    }
}
