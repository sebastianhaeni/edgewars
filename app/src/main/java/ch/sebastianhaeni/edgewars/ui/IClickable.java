package ch.sebastianhaeni.edgewars.ui;

import ch.sebastianhaeni.edgewars.util.Position;

/**
 * Something clickable that performs an action.
 */
public interface IClickable {
    /**
     * @return gets the clickables position
     */
    Position getPosition();

    /**
     * On a click on this clickable.
     */
    void onClick();

    /**
     * On a click on the screen that wasn't handled by any clickable.
     */
    void onUnhandledClick();

    /**
     * @return gets the height of the clickable element
     */
    float getWidth();

    /**
     * @return gets the width of the clickable element
     */
    float getHeight();

    /**
     * @return true if this clickable does not move with the camera but is static
     */
    boolean isStatic();

}
