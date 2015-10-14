package ch.sebastianhaeni.edgewars.logic.commands;

import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;

/**
 * Build a melee factory in a node. This enables the player to build units.
 */
public class BuildTankFactoryCommand extends Command {
    private final Node mNode;

    /**
     * Constructor
     *
     * @param node the node where the factory should be build
     */
    public BuildTankFactoryCommand(Node node) {
        mNode = node;
    }

    @Override
    public void execute() {
        mNode.getTankFactory().build();
    }
}
