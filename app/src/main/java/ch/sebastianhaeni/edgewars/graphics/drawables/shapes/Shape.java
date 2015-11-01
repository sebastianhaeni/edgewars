package ch.sebastianhaeni.edgewars.graphics.drawables.shapes;

import ch.sebastianhaeni.edgewars.graphics.drawables.Drawable;
import ch.sebastianhaeni.edgewars.util.Position;

/**
 * A basic shape with a position and a uni color.
 */
abstract public class Shape extends Drawable {

    private final Position mPosition;
    private final int mLayer;
    private float mColor[];

    /**
     * Constructor
     *
     * @param position the position this shape has it's starting point
     * @param color    the color of this shape
     * @param layer    the layer of this shape to be drawn at
     */
    Shape(Position position, float[] color, int layer) {
        super(layer);
        mPosition = position;
        mColor = color;
        mLayer = layer;
    }

    @Override
    public int getLayer() {
        return mLayer;
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
