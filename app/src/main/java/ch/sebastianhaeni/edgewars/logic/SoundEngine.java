package ch.sebastianhaeni.edgewars.logic;

import android.media.AudioAttributes;
import android.media.SoundPool;

import ch.sebastianhaeni.edgewars.GameApplication;
import ch.sebastianhaeni.edgewars.R;

public class SoundEngine {

    private final SoundPool mSoundPool;
    private static SoundEngine mInstance;

    private SoundEngine() {
        AudioAttributes attr = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        mSoundPool = new SoundPool.Builder()
                .setAudioAttributes(attr)
                .setMaxStreams(10)
                .build();

        Sounds.TICK = mSoundPool.load(GameApplication.getAppContext(), R.raw.sound_tick, 1);
    }

    public static SoundEngine getInstance() {
        if (mInstance == null) {
            mInstance = new SoundEngine();
        }
        return mInstance;
    }

    public void play(int sound) {
        mSoundPool.play(sound, 1, 1, 1, 0, 1);
    }

    public static class Sounds {
        public static int TICK;
    }
}
