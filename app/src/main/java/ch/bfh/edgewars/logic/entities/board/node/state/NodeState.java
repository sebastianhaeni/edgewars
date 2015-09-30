package ch.bfh.edgewars.logic.entities.board.node.state;

import ch.bfh.edgewars.logic.entities.board.node.Node;

public abstract class NodeState {
    private final Node mNode;

    public NodeState(Node node) {
        mNode = node;
    }

    protected Node getNode() {
        return mNode;
    }

    public abstract void update(long millis);

    public abstract long getUpdateInterval();
}
