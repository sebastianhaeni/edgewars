package ch.sebastianhaeni.edgewars.graphics.drawables;

import java.io.Serializable;

import ch.sebastianhaeni.edgewars.graphics.GameRenderer;
import ch.sebastianhaeni.edgewars.graphics.drawables.shapes.Shape;
import ch.sebastianhaeni.edgewars.logic.Game;

/**
 * This interface describes things that can be drawn on the surface.
 */
public abstract class Drawable implements Serializable {

    private final int mLayer;
    private final int mId;
    private static int mCounter;

    /**
     * New Drawable on a specific layer. The lower the layer, the lower this
     * drawable is drawn. To draw something on top of another drawable. This drawable has to have
     * a higher layer index.
     *
     * @param layer layer this drawable is at
     */
    protected Drawable(int layer) {
        mLayer = layer;
        mId = mCounter++;
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
     * Registers the drawable to be drawn.
     */
    public void register() {
        Game.getInstance().register(this);
    }

    /**
     * Unregisters the drawable. This means it's no longer rendered.
     */
    public void unregister() {
        Game.getInstance().unregister(this);
    }

    /**
     * @return gets drawing layer
     */
    public int getLayer() {
        return mLayer;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Drawable) {
            return mId == ((Drawable) o).mId;
        }
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return mId;
    }

    /**
     * @return gets the unique id
     */
    public int getId() {
        return mId;
    }
}
