package ch.sebastianhaeni.edgewars.logic.entities;

import ch.sebastianhaeni.edgewars.graphics.drawables.shapes.Text;
import ch.sebastianhaeni.edgewars.util.Colors;
import ch.sebastianhaeni.edgewars.util.Position;

/**
 * Button to pause the game.
 */
public class PauseButton extends Button {

    private final Text mText;

    /**
     * Constructor
     *
     * @param position the position for this button
     */
    public PauseButton(Position position) {
        super(position);

        mText = new Text(position, Colors.PAUSE_BUTTON, String.valueOf(Text.PAUSE), 10, true);
        mText.register();
    }

    @Override
    public void unregister() {
        mText.unregister();
        super.unregister();
    }

    @Override
    public float getWidth() {
        return 1;
    }

    @Override
    public float getHeight() {
        return 1;
    }

    @Override
    public boolean isStatic() {
        return true;
    }

}
