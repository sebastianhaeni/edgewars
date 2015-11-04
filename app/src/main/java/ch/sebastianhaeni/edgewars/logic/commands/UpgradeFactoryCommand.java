package ch.sebastianhaeni.edgewars.logic.commands;

import ch.sebastianhaeni.edgewars.logic.Constants;
import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.factories.Factory;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.NodeState;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.OwnedState;

/**
 * Upgrades a factory to the next level at a cost. The command is only executed if the player
 * has enough energy.
 */
public class UpgradeFactoryCommand extends Command {
    private final Factory mFactory;

    /**
     * Constructor
     *
     * @param factory the factory to be upgraded.
     */
    public UpgradeFactoryCommand(Factory factory) {
        mFactory = factory;
    }

    @Override
    public void execute() {
        NodeState state = mFactory.getNode().getState();
        if (!(state instanceof OwnedState)) {
            return;
        }

        Player owner = ((OwnedState) state).getOwner();
        if (owner.getEnergy() - mFactory.getUpgradeCost() < 0) {
            return;
        }

        if (mFactory.getLevel() >= Constants.FACTORY_MAX_LEVEL) {
            return;
        }

        owner.removeEnergy(mFactory.getUpgradeCost());
        mFactory.upgrade();
    }
}
