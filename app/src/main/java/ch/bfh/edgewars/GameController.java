package ch.bfh.edgewars;

import android.view.MotionEvent;

import ch.bfh.edgewars.logic.GameState;

public class GameController {

    private static final float TOUCH_SCALE_FACTOR = -.003f;
    private final GameState mGameState;
    private float mPreviousX;
    private float mPreviousY;

    public GameController(GameState gameState) {
        mGameState = gameState;
    }

    public void onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls.

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mGameState.getCamera().freeCamera();
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                mGameState.getCamera().moveCamera(dx * TOUCH_SCALE_FACTOR, dy * TOUCH_SCALE_FACTOR);
                break;
            case MotionEvent.ACTION_UP:
                mGameState.getCamera().takeCamera();
                break;
        }

        mPreviousX = x;
        mPreviousY = y;
    }
}
