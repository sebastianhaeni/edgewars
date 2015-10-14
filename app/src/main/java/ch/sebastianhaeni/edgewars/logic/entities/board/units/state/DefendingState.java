package ch.sebastianhaeni.edgewars.logic.entities.board.units.state;

import java.util.Random;

import ch.sebastianhaeni.edgewars.logic.entities.board.units.Unit;

/**
 * The state of a unit, being in a node and defending it againt an opponent unit.
 */
public class DefendingState extends UnitState {
    private final Unit mAttacker;
    private final Random mRandom = new Random();

    /**
     * Constructor
     *
     * @param unit     the unit having this state
     * @param attacker the opponent unit attacking this unit
     */
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
