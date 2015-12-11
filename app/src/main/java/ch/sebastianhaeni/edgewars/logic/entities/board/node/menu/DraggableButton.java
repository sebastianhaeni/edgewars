package ch.sebastianhaeni.edgewars.logic.entities.board.node.menu;

import java.util.ArrayList;

import ch.sebastianhaeni.edgewars.graphics.drawables.shapes.Line;
import ch.sebastianhaeni.edgewars.ui.IDraggable;
import ch.sebastianhaeni.edgewars.util.Position;

/**
 * A draggable node menu button used to send units.
 */
public class DraggableButton extends NodeButton implements IDraggable {
    private final float[] mColor;
    private Line mLine;
    private ArrayList<IDragListener> mDropHandlers = new ArrayList<>();

    /**
     * Initializes a new draggable button.
     *
     * @param base           base position
     * @param offsetX        x offset of base position
     * @param offsetY        y offset of base position
     * @param text           displayed text
     * @param polygonCorners count of corners for polygon
     */
    public DraggableButton(Position base, float offsetX, float offsetY, String text, int polygonCorners, float[] color) {
        super(base, offsetX, offsetY, text, polygonCorners);
        mColor = color;
    }

    @Override
    public void startDrag(float x, float y) {
        createLine(x, y);
    }

    private void createLine(float x, float y) {
        mLine = new Line(getPosition(), new Position(x, y), mColor, .1f);
        mLine.register();
    }

    @Override
    public void moveDrag(float x, float y) {
        mLine.unregister();
        createLine(x, y);

        for (IDragListener handler : mDropHandlers) {
            handler.move(x, y);
        }
    }

    @Override
    public void stopDrag(float x, float y) {
        mLine.unregister();

        for (IDragListener handler : mDropHandlers) {
            handler.drop(x, y);
        }
    }

    /**
     * Add a drop handler to this button.
     *
     * @param handler new handler
     */
    public void addDragListener(IDragListener handler) {
        mDropHandlers.add(handler);
    }

    /**
     * A handler that acts upon a drop event.
     */
    interface IDragListener {

        /**
         * Called when the drag action starts.
         */
        void start();

        /**
         * The bomb has been dropped!
         *
         * @param x x coordinate
         * @param y y coordinate
         */
        void drop(float x, float y);

        /**
         * Called when moving the finger around.
         *
         * @param x x coordinate
         * @param y y coordinate
         */
        void move(float x, float y);
    }
}
