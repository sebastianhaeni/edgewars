package ch.sebastianhaeni.edgewars.ui;

/**
 * Draggable component with live updates.
 */
public interface IDraggable {
    /**
     * Start a drag action on this clickable.
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    void startDrag(float x, float y);

    /**
     * Update the dragging with the current x and y.
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    void moveDrag(float x, float y);

    /**
     * End the drag action at a coordinate.
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    void stopDrag(float x, float y);
}
