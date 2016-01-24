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
    private final Position mBase;
    private final int mPolygonCorners;
    private final ButtonTextResolver mTextResolver;
    private final VisibleResolver mVisibleResolver;
    private Polygon mShape;
    private TextDecorator mText;
    private Line mLine;
    private boolean mIsVisible;

    /**
     * Constructor
     *
     * @param base            the base position of the button
     * @param offsetX         the x offset of the button to the base position
     * @param offsetY         the y offset of the button to the base position
     * @param visibleResolver resolves if the button is visible
     * @param textResolver    resolves the text for this button given the node object
     * @param polygonCorners  corner count of polygon
     */
    public NodeButton(Position base, float offsetX, float offsetY, VisibleResolver visibleResolver, ButtonTextResolver textResolver, int polygonCorners) {
        super(new Position(base.getX() + offsetX, base.getY() + offsetY));

        mBase = base;
        mPolygonCorners = polygonCorners;
        mTextResolver = textResolver;
        mVisibleResolver = visibleResolver;

        if (mVisibleResolver.isVisible()) {
            show();
        }
    }

    /**
     * Shows the button.
     */
    private void show() {
        if (mIsVisible) {
            return;
        }

        mIsVisible = true;

        mShape = new Polygon(
                getPosition(),
                Colors.NODE_NEUTRAL,
                Constants.MENU_BUTTON_LAYER,
                mPolygonCorners,
                0,
                Constants.MENU_BUTTON_RADIUS);

        mText = new TextDecorator(
                mShape,
                mTextResolver.getText(),
                Constants.MENU_BUTTON_TEXT_LAYER);

        mLine = new Line(mBase, getPosition(), Colors.NODE_NEUTRAL, Constants.MENU_BUTTON_LINE_WIDTH);

        mShape.register();
        mText.register();
        mLine.register();

        register();
    }

    /**
     * Hides the button with its components.
     */
    private void hide() {
        if (mShape != null) mShape.unregister();
        if (mText != null) mText.unregister();
        if (mLine != null) mLine.unregister();

        mShape = null;
        mText = null;
        mLine = null;

        unregister();

        mIsVisible = false;
    }

    @Override
    public float getWidth() {
        return Constants.MENU_BUTTON_RADIUS * 2;
    }

    @Override
    public float getHeight() {
        return Constants.MENU_BUTTON_RADIUS * 2;
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    /**
     * @return gets polygon corners
     */
    public int getPolygonCorners() {
        return mPolygonCorners;
    }

    @Override
    public void update(Observable observable, Object data) {
        if (mText != null) {
            mText.setText(mTextResolver.getText());
        }
        if (mVisibleResolver.isVisible()) {
            show();
        } else {
            hide();
        }
    }

    /**
     * Resolves the text for the button.
     */
    public interface ButtonTextResolver {
        String getText();
    }

    /**
     * Resolves if this button should be visible or not.
     */
    public interface VisibleResolver {
        boolean isVisible();
    }
}
