package ch.sebastianhaeni.edgewars.logic.ai.rules;

import java.util.ArrayList;

import ch.sebastianhaeni.edgewars.EUnitType;
import ch.sebastianhaeni.edgewars.logic.Constants;
import ch.sebastianhaeni.edgewars.logic.Game;
import ch.sebastianhaeni.edgewars.logic.ai.AIAwareness;
import ch.sebastianhaeni.edgewars.logic.commands.Command;
import ch.sebastianhaeni.edgewars.logic.commands.MoveUnitCommand;
import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;

class ConquerRule extends Rule {

    private long mTimePassed;
    private Node mNode;
    private Node mNeutralNeighbor;

    private final int minUnitCount = 3;

    public ConquerRule(Player player) {
        super(player);
    }

    @Override
    public boolean applies(Node node, long millis) {
        mTimePassed += millis;
        if (mTimePassed < Constants.CONQUER_RULE_UPDATE_INTERVAL) {
            return false;
        }
        mTimePassed = 0;
        mNode = node;

        mNeutralNeighbor = AIAwareness.getNeutralNeighbor(mNode);

        return mNeutralNeighbor != null && (mNode.getTankCount() >= minUnitCount || mNode.getSprinterCount() >= minUnitCount || mNode.getMeleeCount() >= minUnitCount);
    }

    @Override
    public ArrayList<Command> getCommands() {
        ArrayList<Command> commands = new ArrayList<>();

        int sprinterCount = mNode.getSprinterCount();
        int meleeCount = mNode.getMeleeCount();
        int tankCount = mNode.getTankCount();

        if (sprinterCount >= meleeCount && sprinterCount >= tankCount) {
            commands.add(new MoveUnitCommand(mNode.getSprinterCount(), EUnitType.SPRINTER, mNeutralNeighbor, Game.getInstance().getEdgeBetween(mNode, mNeutralNeighbor), getPlayer()));
        } else if (meleeCount >= sprinterCount && meleeCount >= tankCount) {
            commands.add(new MoveUnitCommand(mNode.getMeleeCount(), EUnitType.MELEE, mNeutralNeighbor, Game.getInstance().getEdgeBetween(mNode, mNeutralNeighbor), getPlayer()));
        } else {
            commands.add(new MoveUnitCommand(mNode.getTankCount(), EUnitType.TANK, mNeutralNeighbor, Game.getInstance().getEdgeBetween(mNode, mNeutralNeighbor), getPlayer()));
        }

        return commands;
    }
}
