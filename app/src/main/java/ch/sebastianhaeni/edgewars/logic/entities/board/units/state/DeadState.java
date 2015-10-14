package ch.sebastianhaeni.edgewars.logic.entities.board.units.state;

import ch.sebastianhaeni.edgewars.logic.entities.board.units.Unit;

/**
 * State of a unit that's dead.
 */
public class DeadState extends UnitState {

    /**
     * Constructor
     *
     * @param unit the unit having this state
     */
    public DeadState(Unit unit) {
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
