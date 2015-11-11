package ch.sebastianhaeni.edgewars.ui;

import android.view.MotionEvent;

import ch.sebastianhaeni.edgewars.graphics.GameRenderer;
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
                mGameState.getCamera().takeCamera();
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                mGameState.getCamera().moveCamera(dx, dy);
                break;
            case MotionEvent.ACTION_UP:
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

        // get camera position and multiply it by factor 2/3 (why? I dunno..)
        float cameraX = mGameState.getCamera().getScreenX() * (2f / 3f);
        float cameraY = mGameState.getCamera().getScreenY() * (2f / 3f);

        // loop through all nodes and test if one is positioned at the coordinates of the user touch
        for (IClickable clickable : Game.getInstance().getClickables()) {
            float width = clickable.getWidth() * .5f;
            float height = clickable.getHeight() * .5f;

            // convert node coordinates to Android coordinates
            float nodeX = mRenderer.getAndroidCoordinateX(clickable.getPosition().getX());
            float nodeY = mRenderer.getAndroidCoordinateY(clickable.getPosition().getY());

            if (!(Math.abs(nodeX + cameraX - touchX) < width &&
                    Math.abs(nodeY + cameraY - touchY) < height)) {
                continue;
            }

            clickable.onClick();
            return;
        }

        // nothing was clicked, sent that info to all clickables
        for (IClickable clickable : Game.getInstance().getClickables()) {
            clickable.onUnhandledClick();
        }
    }

}
