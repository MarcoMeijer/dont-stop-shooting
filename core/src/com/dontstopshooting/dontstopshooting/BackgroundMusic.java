package com.dontstopshooting.dontstopshooting;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;

public class BackgroundMusic {
    private static final FileHandle s = Gdx.files.internal("soundstart.mp3"), e = Gdx.files.internal("soundend.mp3");
    private static final Music start = Gdx.audio.newMusic(s), end = Gdx.audio.newMusic(e);
    public static boolean muteMusic = false;

    public static void begin() {
        start.stop();
        end.stop();

        start.play();
    }

    public static void update(boolean muted) {
        muted |= muteMusic;
        start.setVolume(muted? 0: 1);
        end.setVolume(muted? 0: 1);
        if (!start.isPlaying() && !end.isPlaying()) {
            end.play();
            end.setLooping(true);
        }
    }
}
