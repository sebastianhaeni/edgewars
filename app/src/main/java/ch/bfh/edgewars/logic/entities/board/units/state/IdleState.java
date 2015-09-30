package ch.bfh.edgewars.logic.entities.board.units.state;

import ch.bfh.edgewars.logic.entities.board.node.Node;
import ch.bfh.edgewars.logic.entities.board.units.Unit;

public class IdleState extends UnitState {
    private final Node mNode;

    public IdleState(Unit unit, Node node) {
        super(unit);
        mNode = node;
    }

    @Override
    public void update(long millis) {

    }
}
