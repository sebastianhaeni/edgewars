package ch.sebastianhaeni.edgewars.graphics.drawables;

import ch.sebastianhaeni.edgewars.graphics.GameRenderer;
import ch.sebastianhaeni.edgewars.graphics.drawables.shapes.Shape;
import ch.sebastianhaeni.edgewars.logic.Game;

/**
 * This interface describes things that can be drawn on the surface.
 */
public abstract class Drawable {

    private final int mLayer;

    /**
     * New Drawable on a specific layer. The lower the layer, the lower this
     * drawable is drawn. To draw something on top of another drawable. This drawable has to have
     * a higher layer index.
     *
     * @param layer layer this drawable is at
     */
    public Drawable(int layer) {
        mLayer = layer;
    }

    /**
     * Destroys this drawable.
     */
    public void destroy() {
        Game.getInstance().unregister(this);
    }

    /**
     * Draws this drawable.
     *
     * @param renderer rendering instance
     */
    public abstract void draw(GameRenderer renderer);

    /**
     * @return the root shape of this sub system of decorated drawables
     */
    public abstract Shape getShape();

    /**
     * @return gets the layer
     */
    public int getLayer() {
        return mLayer;
    }

    /**
     * Registers the drawable to be drawn.
     */
    public void register() {
        Game.getInstance().register(this, mLayer);
    }
}
