package ch.bfh.edgewars.logic.commands;

import ch.bfh.edgewars.logic.entities.board.node.Node;

public class UpgradeNodeHealthCommand extends Command {
    private final Node mNode;

    public UpgradeNodeHealthCommand(Node node) {
        mNode = node;
    }

    @Override
    public void execute() {
        mNode.upgradeHealth();
    }
}
