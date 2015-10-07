package ch.bfh.edgewars.logic.commands;

import ch.bfh.edgewars.logic.entities.board.node.Node;

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
