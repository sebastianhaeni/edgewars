package ch.sebastianhaeni.edgewars.logic.entities.board.node.menu;

import java.util.ArrayList;
import java.util.List;

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
    private final List<Node> mNeighbors = new ArrayList<>();

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
        mNeighbors.clear();

        for (Node neighbor : Game.getInstance().getConnectedNodes(mNode)) {
            Polygon corona = new Polygon(neighbor.getPosition(), Colors.CORONA, 1, 300, 0, .75f);
            corona.register();
            mNeighbors.add(neighbor);
            mCoronas.add(corona);
        }
    }

    @Override
    public void drop(float x, float y) {
        for (Shape corona : mCoronas) {
            corona.unregister();
        }
        mCoronas.clear();

        Node target = getTarget(x, y);
        if (target == null) {
            return;
        }

        Player player = ((OwnedState) mNode.getState()).getOwner();

        switch (mUnitType) {
            case TANK:
                if (mNode.getTankCount() > 0) {
                    Game.getInstance().register(new MoveUnitCommand(mNode.getTankCount(), mUnitType, target, Game.getInstance().getEdgeBetween(mNode, target), player));
                }
                break;
            case SPRINTER:
                if (mNode.getSprinterCount() > 0) {
                    Game.getInstance().register(new MoveUnitCommand(mNode.getSprinterCount(), mUnitType, target, Game.getInstance().getEdgeBetween(mNode, target), player));
                }
                break;
            case MELEE:
                if (mNode.getMeleeCount() > 0) {
                    Game.getInstance().register(new MoveUnitCommand(mNode.getMeleeCount(), mUnitType, target, Game.getInstance().getEdgeBetween(mNode, target), player));
                }
                break;
        }

        if (mDropCorona != null) {
            mDropCorona.unregister();
            mDropCorona = null;
        }

    }

    @Override
    public void move(float x, float y) {
        Node target = getTarget(x, y);
        if (target == null) {
            if (mDropCorona != null) {
                mDropCorona.unregister();
                mDropCorona = null;
            }
            return;
        } else if (mDropCorona != null && target.getPosition().equals(mDropCorona.getPosition())) {
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
        if (!(clickable instanceof Node) || !mNeighbors.contains(clickable)) {
            return null;
        }

        return (Node) clickable;
    }
}
