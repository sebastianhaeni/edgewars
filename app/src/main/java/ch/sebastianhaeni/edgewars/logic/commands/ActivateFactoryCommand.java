package ch.sebastianhaeni.edgewars.logic.commands;

import ch.sebastianhaeni.edgewars.logic.entities.board.factories.Factory;

/**
 * Activate a factory inside a node. All other factories of the same node will be deactivated.
 */
public class ActivateFactoryCommand extends Command {

    private final Factory mFactory;

    public ActivateFactoryCommand(Factory factory) {
        mFactory = factory;
    }

    @Override
    public void execute() {
        mFactory.getNode().getMeleeFactory().deactivate();
        mFactory.getNode().getSprinterFactory().deactivate();
        mFactory.getNode().getTankFactory().deactivate();

        mFactory.activate();
    }
}
