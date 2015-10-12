package ch.sebastianhaeni.edgewars.logic.commands;

import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.Unit;

public class MoveUnitCommand extends Command {
    private final Unit mUnit;
    private final Node mNode;

    public MoveUnitCommand(Unit unit, Node node) {
        mUnit = unit;
        mNode = node;
    }

    @Override
    public void execute() {
        mUnit.move(mNode);
    }
}
