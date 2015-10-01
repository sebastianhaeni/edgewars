package ch.bfh.edgewars.logic.entities.board.units.state;

import java.util.Random;

import ch.bfh.edgewars.logic.entities.board.units.Unit;

public class DefendingState extends UnitState {
    private final Unit mAttacker;
    private final Random mRandom = new Random();

    public DefendingState(Unit unit, Unit attacker) {
        super(unit);
        mAttacker = attacker;
    }

    @Override
    public void update(long millis) {
        if (mRandom.nextFloat() > getUnit().getAccuracy()) {
            mAttacker.deductHealth(getUnit().getAttackDamage());
        }
    }

    @Override
    public long getUpdateInterval() {
        return 1000;
    }
}
