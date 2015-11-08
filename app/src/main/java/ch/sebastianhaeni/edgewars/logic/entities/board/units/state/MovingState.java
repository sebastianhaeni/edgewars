package ch.sebastianhaeni.edgewars.logic.entities.board.units.state;

import ch.sebastianhaeni.edgewars.logic.Constants;
import ch.sebastianhaeni.edgewars.logic.SoundEngine;
import ch.sebastianhaeni.edgewars.logic.entities.Entity;
import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.Edge;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.NeutralState;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.OwnedState;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.Unit;

/**
 * The state of a unit moving along an edge.
 */
public class MovingState extends OnEdgeState {

    /**
     * Constructor
     *
     * @param unit              the unit having this state
     * @param node              the target node it's moving towards
     * @param player            the owner of the unit
     * @param edge              the edge the unit uses to move
     * @param travelledDistance travelled distance on edge
     */
    public MovingState(Unit unit, Node node, Player player, Edge edge, float travelledDistance) {
        super(unit, node, player, edge, travelledDistance);
    }

    @Override
    public void update(long millis) {
        super.update(millis);
        move();
    }

    @Override
    protected void onEntityReached(Entity entity) {
        if (entity instanceof Node) {
            invalidate();
            capture((Node) entity);
            return;
        }

        if (entity instanceof Unit && !(((Unit) entity).getState() instanceof DeadState)) {
            fight((Unit) entity);
        }
    }

    /**
     * Changes unit to fight against the encountered unit.
     *
     * @param encountered the encountered unit that has to be fought
     */
    private void fight(Unit encountered) {
        invalidate();
        ((OnEdgeState) encountered.getState()).invalidate();

        getUnit().setState(new FightUnitState(getUnit(), getNode(), getPlayer(), getEdge(), getTravelledDistance(), encountered));
        encountered.setState(new WaitState(encountered, getNode(), getPlayer(), getEdge(), getTravelledDistance()));
    }

    /**
     * Captures a node.
     *
     * @param reached the reached node
     */
    private void capture(Node reached) {
        if (reached.getState() instanceof NeutralState) {
            reached.setState(new OwnedState(reached, getPlayer()));
            reached.addUnit(getUnit());
            if (getPlayer().isHuman()) {
                SoundEngine.getInstance().play(SoundEngine.Sounds.NODE_CAPTURED);
            }
            return;
        }

        OwnedState state = (OwnedState) reached.getState();
        if (state.getOwner().equals(getPlayer())) {
            reached.addUnit(getUnit());
            getUnit().setState(new IdleState(getUnit()));
            return;
        }

        getUnit().setState(new AttackNodeState(getUnit(), getNode()));
        if (getPlayer().isHuman()) {
            SoundEngine.getInstance().play(SoundEngine.Sounds.NODE_ATTACKED);
        }
    }

    /**
     * Moves the unit along the edge.
     */
    private void move() {
        setTravelledDistance(getTravelledDistance() + getUnit().getSpeed() / Constants.UNIT_SPEED_DIVISOR);

        getShape().getPosition().set(getPosition().getX(), getPosition().getY());
        getShape().calculateVertexBuffer();
        getText().calculateVertexBuffer();
    }

    @Override
    public long getUpdateInterval() {
        return Constants.MOVING_STATE_UPDATE_INTERVAL;
    }

}
