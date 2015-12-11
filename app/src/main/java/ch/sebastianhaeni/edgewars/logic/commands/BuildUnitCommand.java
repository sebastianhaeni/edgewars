package ch.sebastianhaeni.edgewars.logic.commands;

import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.factories.Factory;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.NodeState;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.OwnedState;

/**
 * Build a unit.
 */
public class BuildUnitCommand extends Command {
    private final Factory mFactory;
    private final int mAmount;

    /**
     * Constructor
     *
     * @param factory the factory that builds the unit
     */
    public BuildUnitCommand(Factory factory, int amount) {
        mFactory = factory;
        mAmount = amount;
    }

    @Override
    public void execute() {
        NodeState state = mFactory.getNode().getState();
        if (!(state instanceof OwnedState)) {
            return;
        }

        Player owner = ((OwnedState) state).getOwner();
        for (int i = 0; i < mAmount; i++) {
            if (owner.getEnergy() - mFactory.getUnitCost() < 0) {
                return;
            }

            owner.removeEnergy(mFactory.getUnitCost());
            mFactory.buildUnit();
        }
    }
}
