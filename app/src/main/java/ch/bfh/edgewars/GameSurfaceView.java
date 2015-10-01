package ch.bfh.edgewars;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import ch.bfh.edgewars.graphics.GameRenderer;
import ch.bfh.edgewars.logic.GameState;
import ch.bfh.edgewars.logic.GameThread;
import ch.bfh.edgewars.logic.LevelCreator;

/**
 * A view container where OpenGL ES graphics can be drawn on screen.
 * This view can also be used to capture touch events, such as a user
 * interacting with drawn objects.
 */
public class GameSurfaceView extends GLSurfaceView {

    private GameThread mThread;
    private GameController mController;

    public GameSurfaceView(Context context) {
        super(context);

        LevelCreator creator = new LevelCreator();

        GameState mGameState = creator.build();
        mThread = new GameThread(mGameState);
        GameRenderer renderer = new GameRenderer(mThread, mGameState);
        mController = new GameController(context, renderer, mGameState);

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
