package ch.sebastianhaeni.edgewars.logic.entities.board.units.state;

import android.util.Log;

import ch.sebastianhaeni.edgewars.graphics.drawables.decorators.TextDecorator;
import ch.sebastianhaeni.edgewars.graphics.drawables.shapes.Polygon;
import ch.sebastianhaeni.edgewars.logic.SoundEngine;
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
    private final Polygon mShape;
    private final TextDecorator mText;
    private float mTravelledDistance;
    private boolean mIsStateInvalid;

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

        Node startingNode = edge.getTargetNode().equals(mNode)
                ? edge.getSourceNode()
                : edge.getTargetNode();

        mTargetPosition = mNode.getPosition();
        mStartingPosition = startingNode.getPosition();

        mShape = new Polygon(new Position(startingNode.getPosition()),
                startingNode.getCircle().getColor(), UNIT_SHAPE_LAYER, unit.getPolygonCorners(), 0, UNIT_RADIUS);
        mText = new TextDecorator(mShape, String.valueOf(unit.getCount()), UNIT_TEXT_LAYER);

        mShape.register();
        mText.register();
    }

    @Override
    public void update(long millis) {
        if (mIsStateInvalid) {
            return;
        }
        Node reached = getReachedNode();

        if (reached != null) {
            Log.d("MovingState", "Node reached");
            mIsStateInvalid = true;
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
        mShape.destroy();
        mText.destroy();

        if (reached.getState() instanceof NeutralState) {
            reached.setState(new OwnedState(reached, mPlayer));
            reached.addUnit(getUnit());
            if (mPlayer.isHuman()) {
                SoundEngine.getInstance().play(SoundEngine.Sounds.NODE_CAPTURED);
            }
            return;
        }

        OwnedState state = (OwnedState) reached.getState();
        if (state.getOwner().equals(mPlayer)) {
            reached.addUnit(getUnit());
            getUnit().setState(new IdleState(getUnit()));
            return;
        }

        getUnit().setState(new AttackNodeState(getUnit(), mNode));
        if (mPlayer.isHuman()) {
            SoundEngine.getInstance().play(SoundEngine.Sounds.NODE_ATTACKED);
        }
    }

    /**
     * Moves the unit along the edge.
     */
    private void move() {
        mTravelledDistance += getUnit().getSpeed() / UNIT_SPEED_DIVISOR;

        double dx = mTargetPosition.getX() - mStartingPosition.getX();
        double dy = mTargetPosition.getY() - mStartingPosition.getY();

        double distance = Math.sqrt(Math.pow(dx, 2.0) + Math.pow(dy, 2.0));

        dx /= distance;
        dy /= distance;

        float x = (float) (mStartingPosition.getX() + (mTravelledDistance * dx));
        float y = (float) (mStartingPosition.getY() + (mTravelledDistance * dy));

        mShape.getPosition().set(x, y);
        mShape.calculateVertexBuffer();
        mText.calculateVertexBuffer();
    }

    /**
     * Checks if the target node is reached and returns it.
     *
     * @return the reached node or <code>null</code> if nothing reached yet
     */
    private Node getReachedNode() {
        return mShape.getPosition().isAboutTheSame(mTargetPosition) ? mNode : null;
    }

    @Override
    public long getUpdateInterval() {
        return MOVING_STATE_UPDATE_INTERVAL;
    }

}
