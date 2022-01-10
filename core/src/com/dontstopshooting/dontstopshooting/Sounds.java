package com.dontstopshooting.dontstopshooting;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.Random;

public class Sounds {
    public static float mainVolume = 1.0f;

    public static class SoundEffect {
        public static class SoundID {
            private final int index;
            private final long id;

            SoundID(int index, long id) {
                this.index = index;
                this.id = id;
            }
        }

        public final Sound[] sounds;
        public float volume;

        SoundEffect(String wav, float volume) {
            sounds = new Sound[1];
            sounds[0] = Gdx.audio.newSound(Gdx.files.internal("sounds/" + wav + ".wav"));
            this.volume = volume;
        }

        SoundEffect(String[] files, float volume) {
            sounds = new Sound[files.length];
            for (int i = 0; i < sounds.length; ++i)
                sounds[i] = Gdx.audio.newSound(Gdx.files.internal("sounds/" + files[i] + ".wav"));
            this.volume = volume;
        }

        public SoundID play() {
            int i = new Random().nextInt(sounds.length);
            return new SoundID(i, sounds[i].play(mainVolume*volume));
        }

        public void stop(SoundID id) {
            sounds[id.index].stop(id.id);
        }

        @Override
        protected void finalize() {
            for (Sound s : sounds) s.dispose();
        }
    }

    public static final SoundEffect gun         = new SoundEffect("gunShot", 0.1f);
    public static final SoundEffect fuse        = new SoundEffect("fuse", 0.3f);
    public static final SoundEffect explosion   = new SoundEffect("explosion", 0.65f);
    public static final SoundEffect wood        = new SoundEffect(new String[]{"wood1", "wood2", "wood3", "wood4", "wood5", "wood6"}, 0.25f);
}
