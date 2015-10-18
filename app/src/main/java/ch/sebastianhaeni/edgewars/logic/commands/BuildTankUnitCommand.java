package ch.sebastianhaeni.edgewars.logic.commands;

import ch.sebastianhaeni.edgewars.logic.entities.board.factories.TankFactory;

/**
 * Build a tank unit.
 */
public class BuildTankUnitCommand extends Command {
    private final TankFactory mFactory;

    /**
     * Constructor
     *
     * @param factory the factory that builds the unit
     */
    public BuildTankUnitCommand(TankFactory factory) {
        mFactory = factory;
    }

    @Override
    public void execute() {
        mFactory.buildUnit();
    }
}
