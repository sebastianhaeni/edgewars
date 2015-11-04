package ch.sebastianhaeni.edgewars.logic.entities.board.units.state;

import java.util.Random;

import ch.sebastianhaeni.edgewars.logic.Constants;
import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.Unit;

/**
 * The state of a unit fighting another unit at an opponent node.
 */
public class FightUnitState extends UnitState {
    private final Unit mFightingUnit;
    private final Random mRandom = new Random();
    private final Player mPlayer;
    private final Node mTargetNode;

    /**
     * Constructor
     *
     * @param unit         the unit having this state
     * @param fightingUnit the opponent unit to be fought by this unit
     * @param node         the node the fight happens
     * @param player       the player this unit belongs to
     */
    public FightUnitState(Unit unit, Unit fightingUnit, Node node, Player player) {
        super(unit);
        mFightingUnit = fightingUnit;
        mTargetNode = node;
        mPlayer = player;
    }

    @Override
    public void update(long millis) {
        if (getUnit().getState() instanceof DeadState) {
            return;
        }

        if (mRandom.nextFloat() > getUnit().getAccuracy()) {
            mFightingUnit.deductHealth(getUnit().getAttackDamage());
        }
    }

    @Override
    public long getUpdateInterval() {
        return Constants.FIGHT_UNIT_STATE_UPDATE_INTERVAL;
    }
}
