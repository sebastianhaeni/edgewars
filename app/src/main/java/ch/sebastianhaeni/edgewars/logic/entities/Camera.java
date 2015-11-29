package ch.sebastianhaeni.edgewars.logic.entities;

import ch.sebastianhaeni.edgewars.graphics.GameRenderer;
import ch.sebastianhaeni.edgewars.logic.Constants;
import ch.sebastianhaeni.edgewars.logic.Game;
import ch.sebastianhaeni.edgewars.logic.entities.board.Board;

/**
 * The camera entity updates the view that the human player sees.
 */
public class Camera extends Entity {

    private boolean mIsPlayerControlled = false;

    private float mCameraDx;
    private float mCameraDy;
    private float mCameraX;
    private float mCameraY;
    private float mCameraScreenX;
    private float mCameraScreenY;

    /**
     * Constructor
     */
    public Camera() {
        super(16);
    }

    @Override
    public void update(long millis) {
        if (mIsPlayerControlled || (Math.abs(mCameraDx) < Constants.CAMERA_PRECISION && Math.abs(mCameraDy) < Constants.CAMERA_PRECISION)) {
            return;
        }

        if (Math.abs(mCameraDx) > Constants.CAMERA_PRECISION) {
            mCameraDx *= 1f - Constants.CAMERA_FRICTION;
        }
        if (Math.abs(mCameraDy) > Constants.CAMERA_PRECISION) {
            mCameraDy *= 1f - Constants.CAMERA_FRICTION;
        }

        moveCamera(mCameraDx / Constants.CAMERA_TOUCH_SCALE_FACTOR, mCameraDy / Constants.CAMERA_TOUCH_SCALE_FACTOR);
    }

    /**
     * Updates the position of the camera with the delta value.
     *
     * @param dx delta x
     * @param dy delta y
     */
    public void moveCamera(float dx, float dy) {
        dx = (boundaryIsReached('x', dx)) ? 0 : dx;
        dy = (boundaryIsReached('y', dy)) ? 0 : dy;

        mCameraScreenX += dx;
        mCameraScreenY += dy;
        dx = dx * Constants.CAMERA_TOUCH_SCALE_FACTOR;
        dy = dy * Constants.CAMERA_TOUCH_SCALE_FACTOR;
        mCameraDx = dx;
        mCameraDy = dy;
        mCameraX += dx;
        mCameraY += dy;
    }

    /**
     * @return gets x coordinate of camera
     */
    public float getX() {
        return mCameraX;
    }

    /**
     * @return gets y coordinate of camera
     */
    public float getY() {
        return mCameraY;
    }

    /**
     * When the player is not moving the camera himself, this method is called to let the
     * entity figure out friction and momentum to make it a smoother experience.
     */
    public void freeCamera() {
        mIsPlayerControlled = false;
    }

    /**
     * As soon as the human player wants to move the camera, he takes control. The camera entity
     * no longer tries to figure out friction and momentum and let's the human do what he wants.
     */
    public void takeCamera() {
        mIsPlayerControlled = true;
    }

    /**
     * @return gets the x offset of the camera to the screen
     */
    public float getScreenX() {
        return mCameraScreenX;
    }

    /**
     * @return gets the y offset of the camera to the screen
     */
    public float getScreenY() {
        return mCameraScreenY;
    }

    private boolean boundaryIsReached(char xy, float delta) {
        Board board = Game.getInstance().getGameState().getBoard();
        GameRenderer renderer = Game.getInstance().getGameController().getRenderer();

        float topMostNodeY = renderer.getAndroidCoordinateY(board.getOuterNode(Board.TOP).getPosition().getY());
        float bottomMostNodeY = renderer.getAndroidCoordinateY(board.getOuterNode(Board.BOTTOM).getPosition().getY());
        float rightMostNodeX = renderer.getAndroidCoordinateX(board.getOuterNode(Board.RIGHT).getPosition().getX());
        float leftMostNodeX = renderer.getAndroidCoordinateX(board.getOuterNode(Board.LEFT).getPosition().getX());

        topMostNodeY += mCameraScreenY * (2f / 3f);
        bottomMostNodeY += mCameraScreenY * (2f / 3f);
        rightMostNodeX += mCameraScreenX * (2f / 3f);
        leftMostNodeX += mCameraScreenX * (2f / 3f);

        switch (xy) {
            case 'x':
                return leftMostNodeX + delta > (renderer.getMaxScreenX() * .6f) || rightMostNodeX + delta < (renderer.getMaxScreenX() * .4f);

            case 'y':
                return bottomMostNodeY + delta < (renderer.getMaxScreenY() * .4f) || topMostNodeY + delta > (renderer.getMaxScreenY() * .6f);

            default:
                throw new IllegalArgumentException("I do not know this coordinate!");
        }
    }
}
