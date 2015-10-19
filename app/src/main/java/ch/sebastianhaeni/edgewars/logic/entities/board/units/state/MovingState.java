package ch.sebastianhaeni.edgewars.logic.entities.board.units.state;

import android.util.Log;

import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.Edge;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.NeutralState;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.OwnedState;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.Unit;
import ch.sebastianhaeni.edgewars.util.Position;

/**
 * The state of a unit moving along an edge.
 */
public class MovingState extends UnitState {
    private final Node mNode;
    private final Player mPlayer;
    private final Position mStartingPosition;
    private final Position mTargetPosition;
    private float mTravelledDistance;

    /**
     * Constructor
     *
     * @param unit   the unit having this state
     * @param node   the target node it's moving towards
     * @param player the owner of the unit
     * @param edge   the edge the unit uses to move
     */
    public MovingState(Unit unit, Node node, Player player, Edge edge) {
        super(unit);
        mNode = node;
        mPlayer = player;

        if (edge.getTargetEdge().equals(mNode)) {
            mStartingPosition = edge.getSourceNode().getPosition();
            mTargetPosition = mNode.getPosition();
        } else {
            mStartingPosition = mNode.getPosition();
            mTargetPosition = edge.getSourceNode().getPosition();
        }

    }

    @Override
    public void update(long millis) {
        Node reached = getReachedNode();

        if (reached != null) {
            Log.d("MovingState", "Node reached");
            capture(reached);
            return;
        }

        move();
    }

    /**
     * Captures a node.
     *
     * @param reached the reached node
     */
    private void capture(Node reached) {
        if (reached.getState() instanceof NeutralState) {
            reached.setState(new OwnedState(reached, mPlayer));
        } else {
            OwnedState state = (OwnedState) reached.getState();
            if (state.getOwner().equals(mPlayer)) {
                reached.addUnit(getUnit());
                getUnit().setState(new IdleState(getUnit()));
            } else {
                getUnit().setState(new AttackNodeState(getUnit(), mNode));
            }
        }
    }

    /**
     * Moves the unit along the edge.
     */
    private void move() {
        mTravelledDistance += getUnit().getSpeed() / 2000;

        double dx = mTargetPosition.getX() - mStartingPosition.getX();
        double dy = mTargetPosition.getY() - mStartingPosition.getY();

        double distance = Math.sqrt(Math.pow(dx, 2.0) + Math.pow(dy, 2.0));

        dx /= distance;
        dy /= distance;

        float x = (float) (mStartingPosition.getX() + (mTravelledDistance * dx));
        float y = (float) (mStartingPosition.getY() + (mTravelledDistance * dy));

        getUnit().getPosition().set(x, y);
    }

    /**
     * Checks if the target node is reached and returns it.
     *
     * @return the reached node or <code>null</code> if nothing reached yet
     */
    private Node getReachedNode() {
        return mNode.getPosition().equals(getUnit().getPosition()) ? mNode : null;
    }

    @Override
    public long getUpdateInterval() {
        return 10;
    }

}
