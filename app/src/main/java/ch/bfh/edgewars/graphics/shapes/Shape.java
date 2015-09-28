package ch.bfh.edgewars.graphics.shapes;

import ch.bfh.edgewars.util.Position;

abstract public class Shape {

    private final Position mPosition;

    public Shape(Position position) {
        mPosition = position;
    }

    public abstract void draw(int program, int positionHandle);

    public Position getPosition() {
        return mPosition;
    }
}
