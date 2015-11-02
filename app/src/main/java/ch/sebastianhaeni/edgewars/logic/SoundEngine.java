package ch.sebastianhaeni.edgewars.logic;

import android.media.AudioAttributes;
import android.media.SoundPool;

import java.util.ArrayList;

import ch.sebastianhaeni.edgewars.GameApplication;
import ch.sebastianhaeni.edgewars.R;

/**
 * Sound engine that plays sounds. This is a singleton.
 */
public class SoundEngine {

    private final SoundPool mSoundPool;
    private final ArrayList<Integer> mLoaded = new ArrayList<>(5);

    private static SoundEngine mInstance;

    /**
     * Private constructor.
     */
    private SoundEngine() {
        AudioAttributes attr = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        mSoundPool = new SoundPool.Builder()
                .setAudioAttributes(attr)
                .setMaxStreams(10)
                .build();

        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                mLoaded.add(sampleId);
            }
        });

        Sounds.CLICK = mSoundPool.load(GameApplication.getAppContext(), R.raw.sound_click, 1);
        Sounds.UNIT_SENT = mSoundPool.load(GameApplication.getAppContext(), R.raw.sound_unit_sent, 1);
        Sounds.NODE_CAPTURED = mSoundPool.load(GameApplication.getAppContext(), R.raw.sound_node_captured, 1);
        Sounds.NODE_LOST = mSoundPool.load(GameApplication.getAppContext(), R.raw.sound_node_lost, 1);
        Sounds.NODE_ATTACKED = mSoundPool.load(GameApplication.getAppContext(), R.raw.sound_node_attacked, 1);
    }

    /**
     * @return gets the singleton instance
     */
    public static SoundEngine getInstance() {
        if (mInstance == null) {
            mInstance = new SoundEngine();
        }
        return mInstance;
    }

    /**
     * Plays a sound once and then stops it automatically. If the sound pool is not yet fully loaded
     * the sound will not be played.
     *
     * @param sound the sound to be played
     */
    public void play(int sound) {
        if (!mLoaded.contains(sound)) {
            return;
        }
        mSoundPool.play(sound, 1, 1, 1, 0, 1);
    }

    public static class Sounds {
        public static int CLICK;
        public static int UNIT_SENT;
        public static int NODE_CAPTURED;
        public static int NODE_LOST;
        public static int NODE_ATTACKED;
    }
}
