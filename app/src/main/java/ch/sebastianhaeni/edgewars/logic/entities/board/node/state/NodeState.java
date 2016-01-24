package ch.sebastianhaeni.edgewars.logic.entities.board.node.state;

import java.io.Serializable;

import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;

/**
 * Represents the state of a node.
 */
public abstract class NodeState implements Serializable {
    private final Node mNode;

    /**
     * Constructor
     *
     * @param node the node that has this state
     */
    NodeState(Node node) {
        mNode = node;
    }

    /**
     * @return gets the node
     */
    Node getNode() {
        return mNode;
    }

    /**
     * Updates the state in the configured interval.
     */
    public abstract void update();

    /**
     * Defines the desired interval this state is updated. The state will not be updated in less
     * time than this interval. But it might take a little longer than that to actually call update.
     *
     * @return time in milliseconds this state should be updated in
     */
    public abstract long getUpdateInterval();
}
