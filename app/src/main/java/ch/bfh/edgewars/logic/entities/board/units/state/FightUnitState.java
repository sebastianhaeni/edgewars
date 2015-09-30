package ch.bfh.edgewars.logic.entities.board.units.state;

import ch.bfh.edgewars.logic.entities.board.units.Unit;

public class FightUnitState extends UnitState {
    private final Unit mFightingUnit;

    public FightUnitState(Unit unit, Unit fightingUnit) {
        super(unit);
        mFightingUnit = fightingUnit;
    }

    @Override
    public void update(long millis) {
        mFightingUnit.deductHealth(getUnit().getAttackDamage());
    }

    @Override
    public long getUpdateInterval() {
        return 1000;
    }
}
