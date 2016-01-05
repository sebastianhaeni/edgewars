package ch.sebastianhaeni.edgewars.logic.commands;

import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;

/**
 * Deactivates all factories to stop building units.
 */
public class DeactivateFactoriesCommand extends Command {
    private final Node mNode;

    /**
     * Constructor
     *
     * @param node the node
     */
    public DeactivateFactoriesCommand(Node node) {
        mNode = node;
    }

    @Override
    public void execute() {
        mNode.getMeleeFactory().deactivate();
        mNode.getTankFactory().deactivate();
        mNode.getSprinterFactory().deactivate();
    }
}
