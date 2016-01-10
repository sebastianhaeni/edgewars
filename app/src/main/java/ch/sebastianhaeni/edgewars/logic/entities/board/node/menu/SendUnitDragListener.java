package ch.sebastianhaeni.edgewars.logic.entities.board.node.menu;

import java.util.ArrayList;

import ch.sebastianhaeni.edgewars.EUnitType;
import ch.sebastianhaeni.edgewars.graphics.drawables.shapes.Polygon;
import ch.sebastianhaeni.edgewars.graphics.drawables.shapes.Shape;
import ch.sebastianhaeni.edgewars.logic.Game;
import ch.sebastianhaeni.edgewars.logic.commands.MoveUnitCommand;
import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.OwnedState;
import ch.sebastianhaeni.edgewars.ui.IClickable;
import ch.sebastianhaeni.edgewars.util.Colors;

/**
 * Listener for drag actions when a player wants to send units.
 */
class SendUnitDragListener implements DraggableButton.IDragListener {

    private static final ArrayList<Shape> mCoronas = new ArrayList<>();
    private static Polygon mDropCorona;
    private final EUnitType mUnitType;
    private final Node mNode;

    /**
     * Constructor
     *
     * @param node     the base node starting the drag action
     * @param unitType type of the unit to be sent
     */
    public SendUnitDragListener(Node node, EUnitType unitType) {
        mNode = node;
        mUnitType = unitType;
    }

    @Override
    public void start() {
        mCoronas.clear();

        for (Node neighbor : Game.getInstance().getConnectedNodes(mNode)) {
            Polygon corona = new Polygon(neighbor.getPosition(), Colors.CORONA, 1, 300, 0, .75f);
            corona.register();
            mCoronas.add(corona);
        }
    }

    @Override
    public void drop(float x, float y) {
        Node target = getTarget(x, y);
        if (target == null) {
            return;
        }

        Player player = ((OwnedState) mNode.getState()).getOwner();

        switch (mUnitType) {
            case TANK:
                Game.getInstance().register(new MoveUnitCommand(mNode.getTankCount(), mUnitType, target, Game.getInstance().getEdgeBetween(mNode, target), player));
                break;
            case SPRINTER:
                Game.getInstance().register(new MoveUnitCommand(mNode.getSprinterCount(), mUnitType, target, Game.getInstance().getEdgeBetween(mNode, target), player));
                break;
            case MELEE:
                Game.getInstance().register(new MoveUnitCommand(mNode.getMeleeCount(), mUnitType, target, Game.getInstance().getEdgeBetween(mNode, target), player));
                break;
        }

        for (Shape corona : mCoronas) {
            corona.destroy();
        }

        if (mDropCorona != null) {
            mDropCorona.unregister();
        }

    }

    @Override
    public void move(float x, float y) {
        if (mDropCorona != null) {
            mDropCorona.unregister();
        }
        Node target = getTarget(x, y);
        if (target == null) {
            return;
        }

        mDropCorona = new Polygon(target.getPosition(), Colors.CORONA_DROP, 1, 300, 0, .95f);
        mDropCorona.register();
    }

    /**
     * Gets the target under the finger. If there's no target, <code>null</code> will be returned.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return drop target or <code>null</code> if there's none
     */
    private Node getTarget(float x, float y) {
        IClickable clickable = Game.getInstance().getGameController().isHit(x, y);
        if (!(clickable instanceof Node)) {
            return null;
        }

        return (Node) clickable;
    }
}
