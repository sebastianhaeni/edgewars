package ch.sebastianhaeni.edgewars.logic.commands;

import ch.sebastianhaeni.edgewars.EUnitType;
import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.Edge;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.MeleeUnit;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.SprinterUnit;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.TankUnit;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.Unit;

/**
 * Moves a unit to another node.
 */
public class MoveUnitCommand extends Command {
    private Unit mUnit;
    private final Node mNode;
    private final Edge mEdge;

    /**
     * Constructor
     *
     * @param count  the amount of units contained
     * @param type   type of the unit
     * @param node   the target node
     * @param edge   the edge the unit moves on
     * @param player owning player
     */
    public MoveUnitCommand(int count, EUnitType type, Node node, Edge edge, Player player) {
        switch (type) {
            case MELEE:
                mUnit = new MeleeUnit(count, node, player);
                break;
            case SPRINTER:
                mUnit = new SprinterUnit(count, node, player);
                break;
            case TANK:
                mUnit = new TankUnit(count, node, player);
                break;
        }

        mUnit.register();

        mNode = node;
        mEdge = edge;
    }

    @Override
    public void execute() {
        if (mEdge.getTargetNode().equals(mNode)) {
            mEdge.getSourceNode().clearUnit(mUnit);
        } else {
            mEdge.getTargetNode().clearUnit(mUnit);
        }
        mUnit.move(mNode, mEdge);
    }
}
