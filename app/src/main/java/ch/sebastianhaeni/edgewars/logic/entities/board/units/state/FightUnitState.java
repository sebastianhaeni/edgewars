package ch.sebastianhaeni.edgewars.logic.entities.board.units.state;

import java.util.Random;

import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.Unit;

public class FightUnitState extends UnitState {
    private final Unit mFightingUnit;
    private final Random mRandom = new Random();
    private final Player mPlayer;
    private Node mTargetNode;

    public FightUnitState(Unit unit, Unit fightingUnit, Node node, Player player) {
        super(unit);
        mFightingUnit = fightingUnit;
        mTargetNode = node;
        mPlayer = player;
    }

    @Override
    public void update(long millis) {
        if (getUnit().getState() instanceof DeadState) {
            getUnit().setState(new MovingState(getUnit(), mTargetNode, mPlayer));
            return;
        }

        if (mRandom.nextFloat() > getUnit().getAccuracy()) {
            mFightingUnit.deductHealth(getUnit().getAttackDamage());
        }
    }

    @Override
    public long getUpdateInterval() {
        return 1000;
    }
}
