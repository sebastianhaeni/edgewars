package ch.bfh.edgewars.logic.entities.board.units.state;

import ch.bfh.edgewars.logic.entities.board.units.Unit;

public class DefendingState extends UnitState {
    private final Unit mAttacker;

    public DefendingState(Unit unit, Unit attacker) {
        super(unit);
        unit.setUpdateInterval(1000);
        mAttacker = attacker;
    }

    @Override
    public void update(long millis) {
        mAttacker.deductHealth(getUnit().getAttackDamage());
    }
}
