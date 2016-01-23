package ch.sebastianhaeni.edgewars.ui;

import android.view.MotionEvent;

import ch.sebastianhaeni.edgewars.graphics.GameRenderer;
import ch.sebastianhaeni.edgewars.logic.Constants;
import ch.sebastianhaeni.edgewars.logic.Game;
import ch.sebastianhaeni.edgewars.logic.GameState;
import ch.sebastianhaeni.edgewars.logic.SoundEngine;

/**
 * The game controller handles inputs from the user and delegates them to the according action.
 */
public class GameController {

    private final GameState mGameState;
    private final GameRenderer mRenderer;

    private float mPreviousX;
    private float mPreviousY;
    private float mStartX;
    private float mStartY;
    private IDraggable _dragging;

    /**
     * Constructor
     *
     * @param renderer  game renderer
     * @param gameState game state
     */
    public GameController(GameRenderer renderer, GameState gameState) {
        mGameState = gameState;
        mRenderer = renderer;
    }

    /**
     * Returns the current GameRenderer of the Game
     *
     * @return The current GameRenderer object
     */
    public GameRenderer getRenderer() {
        return mRenderer;
    }

    /**
     * Handles touch events on the OpenGL ES surface.
     *
     * @param e the motion event
     */
    public void onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls.

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = x;
                mStartY = y;

                IClickable clicked = isHit(x, y);
                if (clicked != null && clicked instanceof IDraggable) {
                    // start drag
                    _dragging = (IDraggable) clicked;
                    _dragging.startDrag(x, y);
                    return;
                }

                mGameState.getCamera().takeCamera();
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                if (_dragging != null) {
                    _dragging.moveDrag(x, y);
                    return;
                }
                if (Math.abs(dx) > Constants.CAMERA_SENSITIVITY || Math.abs(dy) > Constants.CAMERA_SENSITIVITY) {
                    mGameState.getCamera().moveCamera(dx, dy);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (_dragging != null) {
                    _dragging.stopDrag(x, y);
                    _dragging = null;
                }

                // detect single click (not moving the camera)
                if (e.getEventTime() - e.getDownTime() < 200
                        && Math.abs(x - mStartX) < 5
                        && Math.abs(y - mStartY) < 5) {
                    SoundEngine.getInstance().play(SoundEngine.Sounds.CLICK);
                    click(x, y);
                }
                mGameState.getCamera().freeCamera();
                break;
        }

        mPreviousX = x;
        mPreviousY = y;
    }

    /**
     * Figures out if a clickable is clicked at that coordinate and what to do after.
     *
     * @param touchX x coordinate
     * @param touchY y coordinate
     */
    private void click(float touchX, float touchY) {
        IClickable clicked = isHit(touchX, touchY);
        if (clicked != null) {
            clicked.onClick();
            return;
        }

        // nothing was clicked, send that info to all clickables
        for (IClickable clickable : Game.getInstance().getClickables()) {
            clickable.onUnhandledClick();
        }
    }

    /**
     * Detects if a clickable is hit and returns it.
     *
     * @param touchX x coordinate
     * @param touchY y coordinate
     * @return the clicked clickable, or <code>null</code> if nothing hit
     */
    public IClickable isHit(float touchX, float touchY) {
        // get camera position
        float cameraX = mGameState.getCamera().getScreenX() * .87f;
        float cameraY = mGameState.getCamera().getScreenY() * .87f;

        float x = mRenderer.getGameCoordinateX(touchX - cameraX);
        float y = mRenderer.getGameCoordinateY(touchY - cameraY);

        float staticX = mRenderer.getGameCoordinateX(touchX);
        float staticY = mRenderer.getGameCoordinateY(touchY);

        // loop through all nodes and test if one is positioned at the coordinates of the user touch
        for (IClickable clickable : Game.getInstance().getClickables()) {
            float glX = clickable.isStatic() ? staticX : x;
            float glY = clickable.isStatic() ? staticY : y;
            float width = clickable.getWidth() * .5f;
            float height = clickable.getHeight() * .5f;

            if (!(Math.abs(clickable.getPosition().getX() - glX) < width &&
                    Math.abs(clickable.getPosition().getY() - glY) < height)) {
                continue;
            }

            return clickable;
        }

        return null;
    }
}
