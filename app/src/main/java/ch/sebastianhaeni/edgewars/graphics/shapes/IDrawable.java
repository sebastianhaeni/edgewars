package ch.sebastianhaeni.edgewars.graphics.shapes;

import ch.sebastianhaeni.edgewars.graphics.GameRenderer;

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
