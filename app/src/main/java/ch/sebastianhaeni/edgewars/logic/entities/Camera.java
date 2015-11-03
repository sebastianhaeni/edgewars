package ch.sebastianhaeni.edgewars.logic.entities;

/**
 * The camera entity updates the view that the human player sees.
 */
public class Camera extends Entity {
    private static final float CAMERA_FRICTION = .1f;
    private static final float TOUCH_SCALE_FACTOR = -.006f;
    private static final float CAMERA_PRECISION = .001f;

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
        if (mIsPlayerControlled || (Math.abs(mCameraDx) < CAMERA_PRECISION && Math.abs(mCameraDy) < CAMERA_PRECISION)) {
            return;
        }

        if (Math.abs(mCameraDx) > CAMERA_PRECISION) {
            mCameraDx *= 1f - CAMERA_FRICTION;
        }
        if (Math.abs(mCameraDy) > CAMERA_PRECISION) {
            mCameraDy *= 1f - CAMERA_FRICTION;
        }

        moveCamera(mCameraDx / TOUCH_SCALE_FACTOR, mCameraDy / TOUCH_SCALE_FACTOR);
    }

    /**
     * Updates the position of the camera with the delta value.
     *
     * @param dx delta x
     * @param dy delta y
     */
    public void moveCamera(float dx, float dy) {
        mCameraScreenX += dx;
        mCameraScreenY += dy;
        dx = dx * TOUCH_SCALE_FACTOR;
        dy = dy * TOUCH_SCALE_FACTOR;
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
}
