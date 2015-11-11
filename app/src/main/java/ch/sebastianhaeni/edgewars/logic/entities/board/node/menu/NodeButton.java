package ch.sebastianhaeni.edgewars.logic.entities.board.node.menu;

import ch.sebastianhaeni.edgewars.graphics.drawables.decorators.TextDecorator;
import ch.sebastianhaeni.edgewars.graphics.drawables.shapes.Line;
import ch.sebastianhaeni.edgewars.graphics.drawables.shapes.Polygon;
import ch.sebastianhaeni.edgewars.logic.Constants;
import ch.sebastianhaeni.edgewars.ui.IClickable;
import ch.sebastianhaeni.edgewars.util.Colors;
import ch.sebastianhaeni.edgewars.util.Position;

/**
 *
 */
public class NodeButton implements IClickable {
    private final Polygon mShape;
    private final TextDecorator mText;
    private final Line mLine;

    public NodeButton(Position base, float offsetX, float offsetY, String text) {
        mShape = new Polygon(
                new Position(base.getX() + offsetX, base.getY() + offsetY),
                Colors.NODE_NEUTRAL,
                Constants.MENU_BUTTON_LAYER,
                Constants.MENU_BUTTON_CORNERS,
                0,
                Constants.MENU_BUTTON_RADIUS);
        mText = new TextDecorator(
                mShape,
                text,
                Constants.MENU_BUTTON_TEXT_LAYER);
        mLine = new Line(base, mShape.getPosition(), Colors.NODE_NEUTRAL);

        mShape.register();
        mText.register();
        mLine.register();
    }

    public void hide() {
        mShape.destroy();
        mText.destroy();
        mLine.destroy();
    }

    @Override
    public Position getPosition() {
        return mShape.getPosition();
    }

    @Override
    public void onClick() {
        // TODO
    }

    @Override
    public void onUnhandledClick() {
        // no op
    }

    @Override
    public float getWidth() {
        return Constants.MENU_BUTTON_RADIUS * 2;
    }

    @Override
    public float getHeight() {
        return Constants.MENU_BUTTON_RADIUS * 2;
    }
}
