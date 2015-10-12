package ch.sebastianhaeni.edgewars.logic.commands;

import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;

public class BuildTankFactoryCommand extends Command {
    private final Node mNode;

    public BuildTankFactoryCommand(Node node) {
        mNode = node;
    }

    @Override
    public void execute() {
        mNode.getTankFactory().build();
    }
}
