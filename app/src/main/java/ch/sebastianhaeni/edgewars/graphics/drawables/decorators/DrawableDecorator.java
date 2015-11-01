package ch.sebastianhaeni.edgewars.graphics.drawables.decorators;

import ch.sebastianhaeni.edgewars.graphics.GameRenderer;
import ch.sebastianhaeni.edgewars.graphics.drawables.Drawable;
import ch.sebastianhaeni.edgewars.graphics.drawables.shapes.Shape;

/**
 * Decorates a shape and is drawable.
 */
public abstract class DrawableDecorator extends Drawable {

    private final Shape mShape;

    /**
     * Constructor
     *
     * @param shape the shape to be decorated
     * @param layer the layer to be drawn on
     */
    DrawableDecorator(Shape shape, int layer) {
        super(layer);
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
