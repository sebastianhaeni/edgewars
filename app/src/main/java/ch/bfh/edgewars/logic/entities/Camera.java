package ch.bfh.edgewars.logic.entities;

public class Camera extends Entity {
    public static final float CAMERA_FRICTION = .1f;

    private boolean mIsPlayerControlled = false;

    private float mCameraDx;
    private float mCameraDy;
    private float mCameraX;
    private float mCameraY;

    @Override
    public void update(long millis) {
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
}
