package ch.bfh.edgewars.logic.entities.board.units.state;

import ch.bfh.edgewars.logic.entities.board.units.Unit;

public class DefendingState extends UnitState {
    private final Unit mAttacker;

    public DefendingState(Unit unit, Unit attacker) {
        super(unit);
        mAttacker = attacker;
    }

    @Override
    public void update(long millis) {
        mAttacker.deductHealth(getUnit().getAttackDamage());
    }

    @Override
    public long getUpdateInterval() {
        return 1000;
    }
}
