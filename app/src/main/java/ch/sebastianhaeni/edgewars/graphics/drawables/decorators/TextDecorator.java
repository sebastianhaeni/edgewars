package ch.sebastianhaeni.edgewars.graphics.drawables.decorators;

import ch.sebastianhaeni.edgewars.graphics.GameRenderer;
import ch.sebastianhaeni.edgewars.graphics.drawables.shapes.Shape;
import ch.sebastianhaeni.edgewars.graphics.drawables.shapes.Text;
import ch.sebastianhaeni.edgewars.util.Colors;

public class TextDecorator extends DrawableDecorator {
    private final Text mText;

    public TextDecorator(Shape shape, String text) {
        super(shape);
        mText = new Text(shape.getPosition(), Colors.TEXT, text);
    }

    @Override
    public void draw(GameRenderer renderer) {
        mText.draw(renderer);
    }

}
