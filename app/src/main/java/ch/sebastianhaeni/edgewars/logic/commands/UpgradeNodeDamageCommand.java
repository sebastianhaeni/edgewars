package ch.sebastianhaeni.edgewars.logic.commands;

import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;

public class UpgradeNodeDamageCommand extends Command {
    private final Node mNode;

    public UpgradeNodeDamageCommand(Node node) {
        mNode = node;
    }

    @Override
    public void execute() {
        mNode.upgradeDamage();
    }
}
