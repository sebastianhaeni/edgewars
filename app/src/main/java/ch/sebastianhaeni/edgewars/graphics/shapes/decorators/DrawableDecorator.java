package ch.sebastianhaeni.edgewars.graphics.shapes.decorators;

import ch.sebastianhaeni.edgewars.graphics.GameRenderer;
import ch.sebastianhaeni.edgewars.graphics.programs.ParticleProgram;
import ch.sebastianhaeni.edgewars.graphics.programs.ShapeProgram;
import ch.sebastianhaeni.edgewars.graphics.shapes.IDrawable;
import ch.sebastianhaeni.edgewars.graphics.shapes.Shape;

/**
 * Decorates a shape and is drawable.
 */
public abstract class DrawableDecorator implements IDrawable {

    private final Shape mShape;

    /**
     * Constructor
     *
     * @param shape the shape to be decorated
     */
    public DrawableDecorator(Shape shape) {
        mShape = shape;
    }

    @Override
    public abstract void draw(GameRenderer renderer, ShapeProgram shapeProgram, ParticleProgram particleProgram);

    /**
     * @return gets the base shape
     */
    public Shape getShape() {
        return mShape;
    }
}
