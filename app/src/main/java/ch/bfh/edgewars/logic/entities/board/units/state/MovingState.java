package ch.bfh.edgewars.logic.entities.board.units.state;

import ch.bfh.edgewars.logic.entities.board.node.Node;
import ch.bfh.edgewars.logic.entities.board.units.Unit;

public class MovingState extends UnitState {
    private final Node mNode;

    public MovingState(Unit unit, Node node) {
        super(unit);
        mNode = node;
    }

    @Override
    public void update(long millis) {
        // TODO
    }

    @Override
    public long getUpdateInterval() {
        return 10;
    }
}
