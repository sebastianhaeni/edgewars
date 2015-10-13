package ch.sebastianhaeni.edgewars.graphics.shapes.decorators;

import ch.sebastianhaeni.edgewars.graphics.programs.ParticleProgram;
import ch.sebastianhaeni.edgewars.graphics.programs.ShapeProgram;
import ch.sebastianhaeni.edgewars.graphics.shapes.IDrawable;
import ch.sebastianhaeni.edgewars.graphics.shapes.Shape;

public abstract class DrawableDecorator implements IDrawable {

    private final Shape mShape;

    public DrawableDecorator(Shape shape) {
        mShape = shape;
    }

    @Override
    public abstract void draw(ShapeProgram shapeProgram, ParticleProgram particleProgram);

    public Shape getRootShape() {
        return mShape;
    }
}
