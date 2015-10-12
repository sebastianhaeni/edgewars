package ch.sebastianhaeni.edgewars.logic.entities.board.units.state;

import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.NeutralState;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.OwnedState;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.Unit;

public class MovingState extends UnitState {
    private final Node mNode;
    private final Player mPlayer;

    public MovingState(Unit unit, Node node, Player player) {
        super(unit);
        mNode = node;
        mPlayer = player;
    }

    @Override
    public void update(long millis) {
        Node reached = getReachedNode();

        if (reached != null) {
            if (reached.getState() instanceof NeutralState) {
                reached.setState(new OwnedState(reached, mPlayer));
            } else {
                OwnedState state = (OwnedState) reached.getState();
                if (state.getOwner().equals(mPlayer)) {
                    reached.addUnit(getUnit());
                    getUnit().setState(new IdleState(getUnit(), mNode));
                } else {
                    getUnit().setState(new AttackNodeState(getUnit(), mNode));
                }
            }
        }

        //Unit encounteredUnit = encounteredUnit();
        //if (encounteredUnit != null) {
        //    getUnit().setState(new FightUnitState(getUnit(), encounteredUnit, mNode, mPlayer));
        //    return;
        //}
    }

    //private Unit encounteredUnit() {
    //    // TODO
    //    return null;
    //}

    public Node getReachedNode() {
        // TODO
        return null;
    }

    @Override
    public long getUpdateInterval() {
        return 10;
    }

}
