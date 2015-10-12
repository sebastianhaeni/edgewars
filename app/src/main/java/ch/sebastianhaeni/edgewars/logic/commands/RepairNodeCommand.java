package ch.sebastianhaeni.edgewars.logic.commands;

import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;

public class RepairNodeCommand extends Command {
    private final Node mNode;

    public RepairNodeCommand(Node node) {
        mNode = node;
    }

    @Override
    public void execute() {
        mNode.repair();
    }
}
