package ch.sebastianhaeni.edgewars.logic.commands;

import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;

public class BuildSprinterFactoryCommand extends Command {
    private final Node mNode;

    public BuildSprinterFactoryCommand(Node node) {
        mNode = node;
    }

    @Override
    public void execute() {
        mNode.getSprinterFactory().build();
    }
}
