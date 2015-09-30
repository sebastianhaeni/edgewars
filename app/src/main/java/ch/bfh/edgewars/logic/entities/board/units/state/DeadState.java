package ch.bfh.edgewars.logic.entities.board.units.state;

import ch.bfh.edgewars.logic.entities.board.units.Unit;

public class DeadState extends UnitState {
    public DeadState(Unit unit) {
        super(unit);
        unit.setUpdateInterval(-1);
    }

    @Override
    public void update(long millis) {
        // no op
    }
}
