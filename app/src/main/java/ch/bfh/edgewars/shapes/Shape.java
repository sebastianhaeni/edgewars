package ch.bfh.edgewars.shapes;

import android.opengl.GLES20;

abstract public class Shape {

    private float mPositionX = 0;
    private float mPositionY = 0;

    public void draw(int program, int positionHandle) {
        drawInternal(program, positionHandle);
    }

    public void setPosition(float x, float y) {
        mPositionX = x;
        mPositionY = y;
    }

    protected abstract void drawInternal(int program, int positionHandle);

    public float getPositionX() {
        return mPositionX;
    }

    public float getPositionY() {
        return mPositionY;
    }
}
