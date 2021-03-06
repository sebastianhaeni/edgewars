package ch.sebastianhaeni.edgewars.graphics;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import java.io.Serializable;
import java.util.List;

import ch.sebastianhaeni.edgewars.logic.Game;
import ch.sebastianhaeni.edgewars.logic.GameState;
import ch.sebastianhaeni.edgewars.logic.GameThread;
import ch.sebastianhaeni.edgewars.logic.LevelLoader;
import ch.sebastianhaeni.edgewars.logic.SoundEngine;
import ch.sebastianhaeni.edgewars.model.LevelRecord;
import ch.sebastianhaeni.edgewars.ui.GameController;
import ch.sebastianhaeni.edgewars.ui.activities.GameActivity;

/**
 * A view container where OpenGL ES graphics can be drawn on screen.
 * This view can also be used to capture touch events, such as a user
 * interacting with drawn objects.
 */
public class GameSurfaceView extends GLSurfaceView implements Serializable {

    private GameThread mThread;
    private GameController mController;
    private final Context mContext;
    private GameState mGameState;
    private LevelRecord mLevelRecord;

    /**
     * Constructor
     *
     * @param context app context
     */
    public GameSurfaceView(Context context) {
        super(context);
        mContext = context;
        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        super.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
    }

    /**
     * Loads and starts the level.
     *
     * @param levelNr level number
     */
    public void startLevel(int levelNr) {
        Game.getInstance().reset();

        LevelLoader levelLoader = new LevelLoader(mContext);
        // load game state and level number
        mGameState = levelLoader.build(levelNr);

        mGameState.init();

        mThread = new GameThread();
        GameRenderer mRenderer = new GameRenderer(mContext, mGameState, mThread);
        mController = new GameController(mRenderer, mGameState);
        Game.getInstance().setGameRenderer(mRenderer);
        Game.getInstance().setGameThread(mThread);
        Game.getInstance().setGameController(mController);

        // set view, to be able to stop Game
        Game.getInstance().setGLView(this);

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);

        // Render the view continuously
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        List<LevelRecord> records = LevelRecord.find(LevelRecord.class, "level_nr = ?", Integer.toString(levelNr));
        if (records.size() > 0) {
            mLevelRecord = records.get(0);
        }
        mLevelRecord.setStartTime(System.currentTimeMillis());

        // start Thread only once (onCreate)
        mThread.setRunning(true);
        mThread.start();

        // report that game has started
        mGameState.setGameIsRunning(true);
    }

    /**
     * Stops the level and saves statistics to LevelRecord
     *
     * @param won boolean, true if game was won
     */
    public void stopLevel(boolean won) {
        SoundEngine.getInstance().play(won ? SoundEngine.Sounds.GAME_WON : SoundEngine.Sounds.GAME_LOST);

        mLevelRecord.setEndTime(mThread.getGameTime());
        mLevelRecord.setWon(won);
        mLevelRecord.save();

        // report that game has stopped
        mGameState.setGameIsRunning(false);

        mThread.setRunning(false);
        Game.getInstance().reset();
        ((GameActivity) mContext).back(this);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent e) {
        mController.onTouchEvent(e);
        return true;
    }

    @Override
    public void onPause() {
        mThread.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mThread.onResume();
        super.onResume();
    }

}
