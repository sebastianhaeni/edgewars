package ch.sebastianhaeni.edgewars.graphics.shapes.decorators;

import ch.sebastianhaeni.edgewars.graphics.GameRenderer;
import ch.sebastianhaeni.edgewars.graphics.shapes.Shape;
import ch.sebastianhaeni.edgewars.graphics.text.TextObject;

public class TextDecorator extends DrawableDecorator {
    private final String mText;
    private boolean mAdded;

    public TextDecorator(Shape shape, String text) {
        super(shape);
        mText = text;
    }

    @Override
    public void draw(GameRenderer renderer) {
        if (mAdded) {
            return;
        }

        mAdded = true;

        renderer.getTextManager().addText(
                new TextObject(
                        mText,
                        getShape().getPosition().getX(),
                        getShape().getPosition().getY()));
    }

}
