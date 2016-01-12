package ch.sebastianhaeni.edgewars.logic.ai.rules;

import android.util.Pair;

import java.util.ArrayList;

import ch.sebastianhaeni.edgewars.EUnitType;
import ch.sebastianhaeni.edgewars.logic.Constants;
import ch.sebastianhaeni.edgewars.logic.Game;
import ch.sebastianhaeni.edgewars.logic.ai.AIAwareness;
import ch.sebastianhaeni.edgewars.logic.commands.Command;
import ch.sebastianhaeni.edgewars.logic.commands.MoveUnitCommand;
import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;

public class AttackRule extends Rule {

    private long mTimePassed;
    private Node mNode;
    private Pair<Integer, EUnitType> attacker;

    public AttackRule(Player player) {
        super(player);
    }

    @Override
    public boolean applies(Node node, long millis) {
        mTimePassed += millis;
        if (mTimePassed < Constants.ATTACK_RULE_UPDATE_INTERVAL) {
            return false;
        }
        mTimePassed = 0;
        mNode = node;

        if (AIAwareness.getDistanceToEnemy(mNode) > 1) {
            return false;
        }

        int minTankCount = 5;
        int minMeleeCount = 7;
        int minSprinterCount = 12;

        attacker = null;

        if (mNode.getTankCount() >= minTankCount) {
            attacker = new Pair<>(mNode.getTankCount(), EUnitType.TANK);
        } else if (mNode.getMeleeCount() >= minMeleeCount) {
            attacker = new Pair<>(mNode.getMeleeCount(), EUnitType.MELEE);
        } else if (mNode.getSprinterCount() >= minSprinterCount) {
            attacker = new Pair<>(mNode.getSprinterCount(), EUnitType.SPRINTER);
        }

        return attacker != null;
    }

    @Override
    public ArrayList<Command> getCommands() {

        Node targetNode = AIAwareness.getGatewayToEnemy(mNode);

        ArrayList<Command> commands = new ArrayList<>();
        commands.add(new MoveUnitCommand(attacker.first, attacker.second, targetNode, Game.getInstance().getEdgeBetween(mNode, targetNode), getPlayer()));

        return commands;
    }
}
