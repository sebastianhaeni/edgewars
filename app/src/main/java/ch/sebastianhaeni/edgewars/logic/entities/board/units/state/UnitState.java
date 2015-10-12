package ch.sebastianhaeni.edgewars.logic.entities.board.units.state;

import ch.sebastianhaeni.edgewars.logic.entities.board.units.Unit;

public abstract class UnitState {
    private final Unit mUnit;

    public UnitState(Unit unit) {
        mUnit = unit;
    }

    protected Unit getUnit() {
        return mUnit;
    }

    public abstract void update(long millis);

    public abstract long getUpdateInterval();
}
