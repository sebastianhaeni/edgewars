package ch.bfh.edgewars.logic.commands;

import ch.bfh.edgewars.logic.entities.board.factories.Factory;

public class UpgradeFactoryCommand extends Command {
    private final Factory mFactory;

    public UpgradeFactoryCommand(Factory factory) {
        mFactory = factory;
    }

    @Override
    public void execute() {
        mFactory.upgrade();
    }
}
