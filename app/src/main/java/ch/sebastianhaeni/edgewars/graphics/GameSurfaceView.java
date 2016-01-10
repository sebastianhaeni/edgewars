package ch.sebastianhaeni.edgewars.graphics;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;

import java.io.Serializable;

import ch.sebastianhaeni.edgewars.logic.Game;
import ch.sebastianhaeni.edgewars.logic.GameState;
import ch.sebastianhaeni.edgewars.logic.GameThread;
import ch.sebastianhaeni.edgewars.logic.LevelLoader;
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
        Log.d("GameSurfaceView", "Starting level " + levelNr);
        Game.getInstance().reset();

        LevelLoader levelLoader = new LevelLoader(mContext);
        // load game state and level number
        mGameState = levelLoader.build(levelNr);

        mGameState.init();

        mThread = new GameThread();
        GameRenderer renderer = new GameRenderer(mContext, mGameState);
        mController = new GameController(renderer, mGameState);
        Game.getInstance().setGameRenderer(renderer);
        Game.getInstance().setGameController(mController);

        // set view, to be able to stop Game
        Game.getInstance().setGLView(this);

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer);

        // Render the view continuously
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        // start Thread only once (onCreate)
        mThread.setRunning(true);
        mThread.start();
    }

    public void stopLevel() {
        Game.getInstance().reset();
        mThread.setRunning(false);
        GameActivity gameActivity = (GameActivity) mContext;
        gameActivity.back(this);
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

    public GameState getState() {
        return mGameState;
    }

    public void setState(GameState state) {
        mGameState = state;
    }

}
