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

    /**
     * Constructor
     *
     * @param factory the factory that builds the unit
     */
    public BuildUnitCommand(Factory factory) {
        mFactory = factory;
    }

    @Override
    public void execute() {
        NodeState state = mFactory.getNode().getState();
        if (!(state instanceof OwnedState)) {
            return;
        }

        Player owner = ((OwnedState) state).getOwner();
        if (owner.getEnergy() - mFactory.getUnitCost() < 0) {
            return;
        }

        owner.removeEnergy(mFactory.getUnitCost());
        mFactory.buildUnit();
    }
}
