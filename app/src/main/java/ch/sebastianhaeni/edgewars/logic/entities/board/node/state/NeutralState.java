package ch.sebastianhaeni.edgewars.logic.entities.board.node.state;

import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.util.Colors;

/**
 * The state of a node if it is not owned by anyone.
 */
public class NeutralState extends NodeState {

    /**
     * Constructor
     *
     * @param node the node that has this state
     */
    public NeutralState(Node node) {
        super(node);
        node.setColor(Colors.NODE_NEUTRAL);

        node.hideNodeMenu();

        node.getTankFactory().deactivate();
        node.getMeleeFactory().deactivate();
        node.getSprinterFactory().deactivate();
    }

    @Override
    public void update(long millis) {
        // no op
    }

    @Override
    public long getUpdateInterval() {
        return -1;
    }
}
