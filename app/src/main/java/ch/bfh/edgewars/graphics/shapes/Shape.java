package ch.bfh.edgewars.graphics.shapes;

import ch.bfh.edgewars.util.Colors;
import ch.bfh.edgewars.util.Position;

abstract public class Shape {

    private final Position mPosition;
    private float mColor[] = Colors.NEUTRAL;

    public Shape(Position position) {
        mPosition = position;
    }

    public abstract void draw(int program, int positionHandle);

    public Position getPosition() {
        return mPosition;
    }

    public void setColor(float[] color) {
        mColor = color;
    }

    protected float[] getColor() {
        return mColor;
    }
}
