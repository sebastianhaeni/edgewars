package ch.sebastianhaeni.edgewars.graphics.drawables;

import ch.sebastianhaeni.edgewars.graphics.GameRenderer;
import ch.sebastianhaeni.edgewars.graphics.drawables.shapes.Shape;

/**
 * This interface describes things that can be drawn on the surface.
 */
public interface IDrawable {

    /**
     * Draws this drawable.
     *
     * @param renderer rendering instance
     */
    void draw(GameRenderer renderer);

    /**
     * @return the root shape of this sub system of decorated drawables
     */
    Shape getShape();
}
