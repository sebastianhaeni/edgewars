package ch.sebastianhaeni.edgewars.logic.commands;

import ch.sebastianhaeni.edgewars.logic.entities.board.factories.SprinterFactory;

public class BuildSprinterUnitCommand extends Command {
    private final SprinterFactory mFactory;

    public BuildSprinterUnitCommand(SprinterFactory factory) {
        mFactory = factory;
    }

    @Override
    public void execute() {
        mFactory.buildUnit();
    }
}
