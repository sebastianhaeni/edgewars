package ch.sebastianhaeni.edgewars.graphics;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.Random;

import ch.sebastianhaeni.edgewars.logic.GameState;
import ch.sebastianhaeni.edgewars.logic.GameThread;
import ch.sebastianhaeni.edgewars.logic.LevelLoader;
import ch.sebastianhaeni.edgewars.ui.GameController;

/**
 * A view container where OpenGL ES graphics can be drawn on screen.
 * This view can also be used to capture touch events, such as a user
 * interacting with drawn objects.
 */
public class GameSurfaceView extends GLSurfaceView {

    private final GameThread mThread;
    private final GameController mController;

    /**
     * Constructor
     *
     * @param context app context
     */
    public GameSurfaceView(Context context, int levelNr) {
        super(context);

        LevelLoader levelLoader = new LevelLoader(context);

        // load game state and level number
        GameState gameState = levelLoader.build(levelNr);

        mThread = new GameThread();
        GameRenderer renderer = new GameRenderer(context, mThread, gameState);
        mController = new GameController(context, renderer, gameState);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        super.setEGLConfigChooser(8, 8, 8, 8, 16, 0);

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer);

        // Render the view continuously
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        mThread.setRunning(false);
        while (retry) {
            try {
                mThread.join();
                retry = false;
            } catch (InterruptedException e) {
                // Try again
            }
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent e) {
        mController.onTouchEvent(e);
        return true;
    }

}
