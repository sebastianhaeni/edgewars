package ch.sebastianhaeni.edgewars.graphics.shapes;

import ch.sebastianhaeni.edgewars.graphics.GameRenderer;
import ch.sebastianhaeni.edgewars.graphics.programs.ParticleProgram;
import ch.sebastianhaeni.edgewars.graphics.programs.ShapeProgram;

/**
 * This interface describes things that can be drawn on the surface.
 */
public interface IDrawable {

    /**
     * Draws this drawable.
     *
     * @param renderer        rendering instance
     * @param shapeProgram    GL program for shapes
     * @param particleProgram GL program for particles
     */
    void draw(GameRenderer renderer, ShapeProgram shapeProgram, ParticleProgram particleProgram);

    /**
     * @return the root shape of this sub system of decorated drawables
     */
    Shape getRootShape();
}
