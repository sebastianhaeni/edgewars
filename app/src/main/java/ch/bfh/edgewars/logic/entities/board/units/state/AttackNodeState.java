package ch.bfh.edgewars.logic.entities.board.units.state;

import ch.bfh.edgewars.logic.entities.board.node.Node;
import ch.bfh.edgewars.logic.entities.board.units.Unit;

public class AttackNodeState extends UnitState {
    private final Node mNode;

    public AttackNodeState(Unit unit, Node node) {
        super(unit);
        mNode = node;
    }

    @Override
    public void update(long millis) {
        mNode.deductHealth(getUnit().getAttackDamage());
    }

    @Override
    public long getUpdateInterval() {
        return 1000;
    }
}
