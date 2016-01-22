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
        if (owner.getEnergy() <= 0) {
            return;
        }

        // deduct repair cost, or if he doesn't have enough money, what he has left
        int energyLeft = owner.getEnergy();
        int repairCost = mNode.getRepairCost();
        owner.removeEnergy(energyLeft > repairCost ? repairCost : energyLeft);
        mNode.repair(energyLeft > repairCost ? 1f : (float) energyLeft / (float) repairCost);
    }
}
