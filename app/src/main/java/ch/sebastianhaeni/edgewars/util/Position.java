package ch.sebastianhaeni.edgewars.util;

/**
 * Position helper class.
 */
public class Position {
    private final float mX;
    private final float mY;

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
}
