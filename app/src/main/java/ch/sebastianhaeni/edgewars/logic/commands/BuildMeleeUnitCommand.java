package ch.sebastianhaeni.edgewars.logic.commands;

import ch.sebastianhaeni.edgewars.logic.entities.board.factories.MeleeFactory;

/**
 * Build a melee unit.
 */
public class BuildMeleeUnitCommand extends Command {
    private final MeleeFactory mFactory;

    /**
     * Constructor
     *
     * @param factory the factory that builds the unit
     */
    public BuildMeleeUnitCommand(MeleeFactory factory) {
        mFactory = factory;
    }

    @Override
    public void execute() {
        mFactory.buildUnit();
    }
}
