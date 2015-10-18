package ch.sebastianhaeni.edgewars.logic.entities.board.units.state;

import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.Unit;

/**
 * The state of a unit idling at one of the owner's node.
 */
public class IdleState extends UnitState {

    /**
     * Constructor
     *
     * @param unit the unit having this state
     */
    public IdleState(Unit unit) {
        super(unit);
    }

    @Override
    public void update(long millis) {
        // no op
    }

    @Override
    public long getUpdateInterval() {
        return -1;
    }
}
