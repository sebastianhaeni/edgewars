package ch.bfh.edgewars.logic.entities;

public class Camera extends Entity {
    public static final float CAMERA_FRICTION = .1f;
    private static final float TOUCH_SCALE_FACTOR = -.006f;
    private static final float CAMERA_PRECISION = .001f;

    private boolean mIsPlayerControlled = false;

    private float mCameraDx;
    private float mCameraDy;
    private float mCameraX;
    private float mCameraY;
    private float mCameraScreenX;
    private float mCameraScreenY;

    public Camera() {
        super(16);
    }

    @Override
    public void updateState(long millis) {
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
     * @param dx
     * @param dy
     */
    public void moveCamera(float dx, float dy) {
        mCameraScreenX += dx;
        mCameraScreenY += dy;
        dx = dx * TOUCH_SCALE_FACTOR;
        dy = dy * TOUCH_SCALE_FACTOR;
        mCameraDx = dx;
        mCameraDy = dy;
        mCameraX -= dx;
        mCameraY -= dy;
    }

    public float getX() {
        return mCameraX;
    }

    public float getY() {
        return mCameraY;
    }

    public void freeCamera() {
        mIsPlayerControlled = false;
    }

    public void takeCamera() {
        mIsPlayerControlled = true;
    }

    public float getScreenX() {
        return mCameraScreenX;
    }

    public float getScreenY() {
        return mCameraScreenY;
    }
}
