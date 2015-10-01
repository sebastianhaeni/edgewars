package ch.bfh.edgewars.logic.entities;

public class Camera extends Entity {
    public static final float CAMERA_FRICTION = .1f;
    private static final float TOUCH_SCALE_FACTOR = -.003f;

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
        if (!mIsPlayerControlled && Math.abs(mCameraDx) < .0001 && Math.abs(mCameraDy) < .0001) {
            return;
        }

        if (Math.abs(mCameraDx) > 0.0001) {
            mCameraDx *= 1f - CAMERA_FRICTION;
        }
        if (Math.abs(mCameraDy) > 0.0001) {
            mCameraDy *= 1f - CAMERA_FRICTION;
        }

        moveCamera(mCameraDx, mCameraDy);
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
        mIsPlayerControlled = true;
    }

    public void takeCamera() {
        mIsPlayerControlled = false;
    }

    public float getScreenX() {
        return mCameraScreenX;
    }

    public float getScreenY() {
        return mCameraScreenY;
    }
}
