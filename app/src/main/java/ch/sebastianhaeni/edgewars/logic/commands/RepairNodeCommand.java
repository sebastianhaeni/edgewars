package ch.sebastianhaeni.edgewars.logic.commands;

import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.NodeState;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.OwnedState;

/**
 * Repairs a node to full health with a cost. The command is only executed if the player has enough
 * energy.
 */
public class RepairNodeCommand extends Command {
    private final Node mNode;

    /**
     * Constructor
     *
     * @param node the node to be repaired
     */
    public RepairNodeCommand(Node node) {
        mNode = node;
    }

    @Override
    public void execute() {
        NodeState state = mNode.getState();
        if (!(state instanceof OwnedState)) {
            return;
        }

        Player owner = ((OwnedState) state).getOwner();
        if (owner.getEnergy() - mNode.getRepairCost() < 0) {
            return;
        }

        owner.removeEnergy(mNode.getRepairCost());
        mNode.repair();
    }
}
