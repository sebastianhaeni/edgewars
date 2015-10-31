package ch.sebastianhaeni.edgewars.graphics.drawables.decorators;

import ch.sebastianhaeni.edgewars.graphics.GameRenderer;
import ch.sebastianhaeni.edgewars.graphics.drawables.IDrawable;
import ch.sebastianhaeni.edgewars.graphics.drawables.shapes.Shape;

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
    public abstract void draw(GameRenderer renderer);

    /**
     * @return gets the base shape
     */
    public Shape getShape() {
        return mShape;
    }
}
