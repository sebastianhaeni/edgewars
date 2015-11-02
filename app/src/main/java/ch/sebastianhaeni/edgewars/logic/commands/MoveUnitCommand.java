package ch.sebastianhaeni.edgewars.logic.commands;

import android.util.Log;

import ch.sebastianhaeni.edgewars.logic.entities.board.Edge;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.Unit;

/**
 * Moves a unit to another node.
 */
public class MoveUnitCommand extends Command {
    private final Unit mUnit;
    private final Node mNode;
    private final Edge mEdge;

    /**
     * Constructor
     *
     * @param unit the unit to be moved
     * @param node the target node
     * @param edge the edge the unit moves on
     */
    public MoveUnitCommand(Unit unit, Node node, Edge edge) {
        mUnit = unit;
        mNode = node;
        mEdge = edge;
    }

    @Override
    public void execute() {
        Log.d("MoveUnitCommand", "Sending units");
        mUnit.move(mNode, mEdge);
        mNode.clearUnit(mUnit);
    }
}
