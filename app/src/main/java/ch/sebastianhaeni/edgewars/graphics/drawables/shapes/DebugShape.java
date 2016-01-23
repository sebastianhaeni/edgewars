package ch.sebastianhaeni.edgewars.graphics.drawables.shapes;

import ch.sebastianhaeni.edgewars.graphics.GameRenderer;
import ch.sebastianhaeni.edgewars.util.Position;

/**
 * Debug shape to debug touches.
 */
public class DebugShape extends Shape {
    /**
     * Constructor
     *
     * @param position the position this shape has it's starting point
     * @param color    the color of this shape
     * @param layer    the layer of this shape to be drawn at
     */
    public DebugShape(Position position, float[] color, int layer) {
        super(position, color, layer);
    }

    @Override
    public void draw(GameRenderer renderer) {

    }
}
