package ch.sebastianhaeni.edgewars.util;

/**
 * Position helper class.
 */
public class Position {
    private float mX;
    private float mY;

    /**
     * Constructor
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    public Position(float x, float y) {
        this.mX = x;
        this.mY = y;
    }

    /**
     * @return gets x
     */
    public float getX() {
        return mX;
    }

    /**
     * @return gets y
     */
    public float getY() {
        return mY;
    }

    /**
     * Sets new coordinates.
     *
     * @param x coordinate
     * @param y coordinate
     */
    public void set(float x, float y) {
        mX = x;
        mY = y;
    }

    @Override
    public String toString() {
        return "{x: " + mX + ", y: " + mY + "}";
    }
}
