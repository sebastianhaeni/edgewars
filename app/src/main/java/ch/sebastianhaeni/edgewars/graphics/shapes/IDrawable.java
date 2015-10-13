package ch.sebastianhaeni.edgewars.graphics.shapes;

import ch.sebastianhaeni.edgewars.graphics.GameRenderer;
import ch.sebastianhaeni.edgewars.graphics.programs.ParticleProgram;
import ch.sebastianhaeni.edgewars.graphics.programs.ShapeProgram;

public interface IDrawable {
    void draw(GameRenderer renderer, ShapeProgram shapeProgram, ParticleProgram particleProgram);

    Shape getRootShape();
}
