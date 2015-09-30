package ch.bfh.edgewars.logic.commands;

import ch.bfh.edgewars.logic.entities.board.Node;
import ch.bfh.edgewars.logic.entities.board.units.Unit;
import ch.bfh.edgewars.logic.entities.board.units.state.MovingState;

public class SendUnitCommand extends Command {
    private final Unit mUnit;
    private final Node mNode;

    public SendUnitCommand(Unit unit, Node node) {
        mUnit = unit;
        mNode = node;
    }

    @Override
    public void execute() {
        mUnit.setState(new MovingState(mUnit, mNode));
    }
}
