package ch.sebastianhaeni.edgewars.graphics.shapes.decorators;

import android.util.Log;

import ch.sebastianhaeni.edgewars.graphics.GameRenderer;
import ch.sebastianhaeni.edgewars.graphics.shapes.Shape;

public class TextDecorator extends DrawableDecorator {
    private final String mText;

    public TextDecorator(Shape shape, String text) {
        super(shape);
        mText = text;
    }

    @Override
    public void draw(GameRenderer renderer) {
        Log.d("TextDecorator", "Drawing text: " + mText);
    }

}
