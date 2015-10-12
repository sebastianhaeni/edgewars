package ch.sebastianhaeni.edgewars.logic.commands;

import ch.sebastianhaeni.edgewars.logic.entities.board.factories.MeleeFactory;

public class BuildMeleeUnitCommand extends Command {
    private final MeleeFactory mFactory;

    public BuildMeleeUnitCommand(MeleeFactory factory) {
        mFactory = factory;
    }

    @Override
    public void execute() {
        mFactory.buildUnit();
    }
}
