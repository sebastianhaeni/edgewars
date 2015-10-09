package ch.bfh.edgewars.logic.entities.board.units.state;

import java.util.Random;

import ch.bfh.edgewars.logic.entities.board.node.Node;
import ch.bfh.edgewars.logic.entities.board.units.Unit;

public class AttackNodeState extends UnitState {
    private final Node mNode;
    private final Random mRandom = new Random();

    public AttackNodeState(Unit unit, Node node) {
        super(unit);
        mNode = node;
    }

    @Override
    public void update(long millis) {
        if (mRandom.nextFloat() > getUnit().getAccuracy()) {
            mNode.deductHealth(getUnit().getAttackDamage());
        }
    }

    @Override
    public long getUpdateInterval() {
        return 1000;
    }
}
