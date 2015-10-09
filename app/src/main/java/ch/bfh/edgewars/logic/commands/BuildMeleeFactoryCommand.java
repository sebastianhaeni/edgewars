package ch.bfh.edgewars.logic.commands;

import ch.bfh.edgewars.logic.entities.board.node.Node;

public class BuildMeleeFactoryCommand extends Command {
    private final Node mNode;

    public BuildMeleeFactoryCommand(Node node) {
        mNode = node;
    }

    @Override
    public void execute() {
        mNode.getMeleeFactory().build();
    }
}
