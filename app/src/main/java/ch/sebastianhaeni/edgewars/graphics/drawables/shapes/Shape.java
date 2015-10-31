package ch.sebastianhaeni.edgewars.graphics.drawables.shapes;

import ch.sebastianhaeni.edgewars.graphics.drawables.IDrawable;
import ch.sebastianhaeni.edgewars.util.Position;

/**
 * A basic shape with a position and a uni color.
 */
abstract public class Shape implements IDrawable {

    private final Position mPosition;
    private float mColor[];

    /**
     * Constructor
     *
     * @param position the position this shape has it's starting point
     * @param color    the color of this shape
     */
    public Shape(Position position, float[] color) {
        mPosition = position;
        mColor = color;
    }

    /**
     * @return gets the position
     */
    public Position getPosition() {
        return mPosition;
    }

    /**
     * Sets the color.
     *
     * @param color new rgb color
     */
    public void setColor(float[] color) {
        mColor = color;
    }

    /**
     * @return gets the color
     */
    public float[] getColor() {
        return mColor;
    }

    /**
     * @return gets the shape
     */
    public Shape getShape() {
        return this;
    }
}
