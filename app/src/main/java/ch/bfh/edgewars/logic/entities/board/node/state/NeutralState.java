package ch.bfh.edgewars.logic.entities.board.node.state;

import ch.bfh.edgewars.logic.entities.board.node.Node;

public class NeutralState extends NodeState {
    public NeutralState(Node node) {
        super(node);
    }

    @Override
    public void update(long millis) {
        // no op
    }
}
