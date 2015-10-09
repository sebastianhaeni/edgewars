package ch.bfh.edgewars.logic.commands;

import ch.bfh.edgewars.logic.entities.board.factories.TankFactory;

public class BuildTankUnitCommand extends Command {
    private final TankFactory mFactory;

    public BuildTankUnitCommand(TankFactory factory) {
        mFactory = factory;
    }

    @Override
    public void execute() {
        mFactory.buildUnit();
    }
}
