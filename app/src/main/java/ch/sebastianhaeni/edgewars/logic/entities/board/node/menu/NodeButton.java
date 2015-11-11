package ch.sebastianhaeni.edgewars.logic.entities.board.node.menu;

import java.util.Observable;
import java.util.Observer;

import ch.sebastianhaeni.edgewars.graphics.drawables.decorators.TextDecorator;
import ch.sebastianhaeni.edgewars.graphics.drawables.shapes.Line;
import ch.sebastianhaeni.edgewars.graphics.drawables.shapes.Polygon;
import ch.sebastianhaeni.edgewars.logic.Constants;
import ch.sebastianhaeni.edgewars.logic.entities.Button;
import ch.sebastianhaeni.edgewars.util.Colors;
import ch.sebastianhaeni.edgewars.util.Position;

/**
 * A button that hangs around a node.
 */
public class NodeButton extends Button implements Observer {
    private final Polygon mShape;
    private final TextDecorator mText;
    private final Line mLine;
    private final int mPolygonCorners;
    private final ButtonTextResolver mResolver;

    /**
     * Constructor
     *
     * @param base           the base position of the button
     * @param offsetX        the x offset of the button to the base position
     * @param offsetY        the y offset of the button to the base position
     * @param resolver       resolves the text for this button given the node object
     * @param polygonCorners corner count of polygon
     */
    public NodeButton(Position base, float offsetX, float offsetY, ButtonTextResolver resolver, int polygonCorners) {
        super(new Position(base.getX() + offsetX, base.getY() + offsetY));

        mPolygonCorners = polygonCorners;
        mResolver = resolver;

        mShape = new Polygon(
                getPosition(),
                Colors.NODE_NEUTRAL,
                Constants.MENU_BUTTON_LAYER,
                polygonCorners,
                0,
                Constants.MENU_BUTTON_RADIUS);

        mText = new TextDecorator(
                mShape,
                resolver.getText(),
                Constants.MENU_BUTTON_TEXT_LAYER);

        mLine = new Line(base, getPosition(), Colors.NODE_NEUTRAL, Constants.MENU_BUTTON_LINE_WIDTH);

        mShape.register();
        mText.register();
        mLine.register();
    }

    /**
     * Hides the button with it's components.
     */
    public void hide() {
        mShape.destroy();
        mText.destroy();
        mLine.destroy();
    }

    @Override
    public float getWidth() {
        return Constants.MENU_BUTTON_RADIUS * 2;
    }

    @Override
    public float getHeight() {
        return Constants.MENU_BUTTON_RADIUS * 2;
    }

    /**
     * @return gets polygon corners
     */
    public int getPolygonCorners() {
        return mPolygonCorners;
    }

    @Override
    public void update(Observable observable, Object data) {
        mText.setText(mResolver.getText());
    }

    public interface ButtonTextResolver {
        String getText();
    }
}
