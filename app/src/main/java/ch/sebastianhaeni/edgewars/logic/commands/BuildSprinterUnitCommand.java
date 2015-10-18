package ch.sebastianhaeni.edgewars.logic.commands;

import ch.sebastianhaeni.edgewars.logic.entities.board.factories.SprinterFactory;

/**
 * Build a sprinter unit.
 */
public class BuildSprinterUnitCommand extends Command {
    private final SprinterFactory mFactory;

    /**
     * Constructor
     *
     * @param factory the factory that builds the unit
     */
    public BuildSprinterUnitCommand(SprinterFactory factory) {
        mFactory = factory;
    }

    @Override
    public void execute() {
        mFactory.buildUnit();
    }
}
