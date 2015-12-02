package ch.sebastianhaeni.edgewars.logic.entities.board.node.menu;

import java.util.ArrayList;

import ch.sebastianhaeni.edgewars.graphics.drawables.shapes.Line;
import ch.sebastianhaeni.edgewars.ui.IDraggable;
import ch.sebastianhaeni.edgewars.util.Colors;
import ch.sebastianhaeni.edgewars.util.Position;

/**
 * A draggable node menu button used to send units.
 */
public class DraggableButton extends NodeButton implements IDraggable {
    private Line mLine;
    private ArrayList<IDropListener> mDropHandlers = new ArrayList<>();

    /**
     * Initializes a new draggable button.
     *
     * @param base           base position
     * @param offsetX        x offset of base position
     * @param offsetY        y offset of base position
     * @param text           displayed text
     * @param polygonCorners count of corners for polygon
     */
    public DraggableButton(Position base, float offsetX, float offsetY, String text, int polygonCorners) {
        super(base, offsetX, offsetY, text, polygonCorners);
    }

    @Override
    public void startDrag(float x, float y) {
        createLine(x, y);
    }

    private void createLine(float x, float y) {
        mLine = new Line(getPosition(), new Position(x, y), Colors.EDGE, .4f);
        mLine.register();
    }

    @Override
    public void moveDrag(float x, float y) {
        mLine.unregister();
        createLine(x, y);
    }

    @Override
    public void stopDrag(float x, float y) {
        mLine.unregister();
        for (IDropListener handler : mDropHandlers) {
            handler.drop(x, y);
        }
    }

    /**
     * Add a drop handler to this button.
     *
     * @param handler new handler
     */
    public void addDropListener(IDropListener handler) {
        mDropHandlers.add(handler);
    }

    /**
     * A handler that acts upon a drop event.
     */
    interface IDropListener {

        /**
         * The bomb has been dropped!
         *
         * @param x x coordinate
         * @param y y coordinate
         */
        void drop(float x, float y);
    }
}
