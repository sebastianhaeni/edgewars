package ch.sebastianhaeni.edgewars.graphics.shapes;

import ch.sebastianhaeni.edgewars.util.Colors;
import ch.sebastianhaeni.edgewars.util.Position;

abstract public class Shape implements IDrawable {

    private final Position mPosition;
    private float mColor[] = Colors.NEUTRAL;

    public Shape(Position position) {
        mPosition = position;
    }

    public Position getPosition() {
        return mPosition;
    }

    public void setColor(float[] color) {
        mColor = color;
    }

    protected float[] getColor() {
        return mColor;
    }

    public Shape getRootShape() {
        return this;
    }
}
