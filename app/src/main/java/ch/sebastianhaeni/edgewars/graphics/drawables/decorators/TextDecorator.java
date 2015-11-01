package ch.sebastianhaeni.edgewars.graphics.drawables.decorators;

import ch.sebastianhaeni.edgewars.graphics.GameRenderer;
import ch.sebastianhaeni.edgewars.graphics.drawables.shapes.Shape;
import ch.sebastianhaeni.edgewars.graphics.drawables.shapes.Text;
import ch.sebastianhaeni.edgewars.util.Colors;

/**
 * A text that follows a shape and decorates it.
 */
public class TextDecorator extends DrawableDecorator {
    private final Text mText;

    /**
     * Constructor
     *
     * @param shape the text should attach to this shape
     * @param text  the text to be displayed (only basic letters supported)
     * @param layer draw layer
     */
    public TextDecorator(Shape shape, String text, int layer) {
        super(shape, layer);
        mText = new Text(shape.getPosition(), Colors.TEXT, text, layer);
    }

    @Override
    public void draw(GameRenderer renderer) {
        // no op
    }

    /**
     * Set new text to text object.
     *
     * @param text new text
     */
    public void setText(String text) {
        mText.setText(text);
    }

    @Override
    public void register() {
        super.register();
        mText.register();
    }

    @Override
    public void destroy() {
        super.destroy();
        mText.destroy();
    }
}
