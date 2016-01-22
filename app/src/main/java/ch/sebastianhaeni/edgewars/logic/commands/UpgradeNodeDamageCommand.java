package ch.sebastianhaeni.edgewars.logic.commands;

import ch.sebastianhaeni.edgewars.graphics.drawables.shapes.Text;
import ch.sebastianhaeni.edgewars.logic.Constants;
import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.NodeState;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.OwnedState;

/**
 * Upgrades the damage level of a node to the next level at a cost. The command is only executed
 * if the player has enough energy.
 */
public class UpgradeNodeDamageCommand extends Command {
    private final Node mNode;

    /**
     * Constructor
     *
     * @param node the node's damage level to be upgraded
     */
    public UpgradeNodeDamageCommand(Node node) {
        mNode = node;
    }

    @Override
    public void execute() {
        NodeState state = mNode.getState();
        if (!(state instanceof OwnedState)) {
            return;
        }

        Player owner = ((OwnedState) state).getOwner();
        if (owner.getEnergy() - Constants.NODE_DAMAGE_LEVEL_UPGRADE_COST < 0) {
            return;
        }

        if (mNode.maxDamageLevelReached()) {
            return;
        }
        owner.removeEnergy(String.valueOf(Text.DAMAGE), Constants.NODE_DAMAGE_LEVEL_UPGRADE_COST);

        mNode.upgradeDamage();
    }
}
