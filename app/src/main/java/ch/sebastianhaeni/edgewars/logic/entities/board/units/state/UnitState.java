package ch.sebastianhaeni.edgewars.logic.entities.board.units.state;

import ch.sebastianhaeni.edgewars.logic.entities.board.units.Unit;

/**
 * State of a unit.
 */
public abstract class UnitState {
    private final Unit mUnit;

    /**
     * Constructor
     *
     * @param unit the unit having this state
     */
    public UnitState(Unit unit) {
        mUnit = unit;
    }

    /**
     * @return gets the unit having this state
     */
    protected Unit getUnit() {
        return mUnit;
    }

    /**
     * Updates the state in a defined interval.
     *
     * @param millis time passed since last call to <code>update()</code>
     */
    public abstract void update(long millis);

    /**
     * @return gets the update interval of the state
     */
    public abstract long getUpdateInterval();
}
