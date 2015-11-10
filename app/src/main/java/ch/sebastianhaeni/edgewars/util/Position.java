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
     * Sets a new position
     *
     * @param other the new position
     */
    public void set(Position other) {
        mX = other.getX();
        mY = other.getY();
    }

    @Override
    public String toString() {
        return "{x: " + mX + ", y: " + mY + "}";
    }

    /**
     * Determines if the positions are about the same with a given precision.
     *
     * @param position  the other position to compare with
     * @param positiveX if the position is about the same in positive x direction
     * @param positiveY if the position is about the same in positive y direction
     * @param precision how close the positions have to be
     * @return true if this is about the same
     */
    public boolean isAboutTheSame(Position position, boolean positiveX, boolean positiveY, float precision) {
        float x = position.getX() - mX;
        float y = position.getY() - mY;

        boolean xIsClose;

        if (positiveX) {
            xIsClose = x >= 0 && x < precision;
        } else {
            xIsClose = x <= 0 && x > -precision;
        }

        if (!xIsClose) {
            return false;
        }

        if (positiveY) {
            return y >= 0 && y < precision;
        }

        return y <= 0 && y > -precision;
    }
}
