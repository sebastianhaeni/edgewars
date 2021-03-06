package ch.sebastianhaeni.edgewars.logic.ai.rules;

import java.util.ArrayList;

import ch.sebastianhaeni.edgewars.logic.Constants;
import ch.sebastianhaeni.edgewars.logic.commands.Command;
import ch.sebastianhaeni.edgewars.logic.commands.RepairNodeCommand;
import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;

class RepairRule extends Rule {

    private static final float LOW_HEALTH_LIMIT = 0.25f;

    private long mTimePassed;
    private Node mNode;

    public RepairRule(Player player) {
        super(player);
    }

    @Override
    public boolean applies(Node node, long millis) {
        mTimePassed += millis;
        if (mTimePassed < Constants.REPAIR_RULE_UPDATE_INTERVAL) {
            return false;
        }
        mTimePassed = 0;
        mNode = node;

        return node.getHealth() < LOW_HEALTH_LIMIT * node.getMaxHealth() && node.getRepairCost() <= getPlayer().getEnergy();
    }

    @Override
    public ArrayList<Command> getCommands() {
        ArrayList<Command> commands = new ArrayList<>();
        commands.add(new RepairNodeCommand(mNode));
        return commands;
    }
}
