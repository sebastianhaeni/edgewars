package ch.sebastianhaeni.edgewars.logic.ai.rules;

import java.util.ArrayList;

import ch.sebastianhaeni.edgewars.logic.Constants;
import ch.sebastianhaeni.edgewars.logic.commands.ActivateFactoryCommand;
import ch.sebastianhaeni.edgewars.logic.commands.Command;
import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;

public class BuildUpRule extends Rule {
    private Node mNode;
    private long mTimePassed;

    private final int maxMeleeCount = 30;
    private final int maxSprinterCount = 30;
    private final int maxTankCount = 30;

    public BuildUpRule(Player player) {
        super(player);
    }

    @Override
    public boolean applies(Node node, long millis) {
        mTimePassed += millis;
        if (mTimePassed < Constants.BUILDUP_RULE_UPDATE_INTERVAL) {
            return false;
        }
        mTimePassed = 0;

        mNode = node;

        // rule does not apply if node has already "too many" units
        return !(node.getMeleeCount() > maxMeleeCount && node.getSprinterCount() > maxSprinterCount && node.getTankCount() > maxTankCount);
    }

    @Override
    public ArrayList<Command> getCommands() {
        ArrayList<Command> commands = new ArrayList<>();

        if (mNode.getMeleeCount() < maxMeleeCount) {
            commands.add(new ActivateFactoryCommand(mNode.getMeleeFactory()));
        } else if (mNode.getSprinterCount() < maxSprinterCount) {
            commands.add(new ActivateFactoryCommand(mNode.getSprinterFactory()));
        } else if (mNode.getTankCount() < maxTankCount) {
            commands.add(new ActivateFactoryCommand(mNode.getTankFactory()));
        }

        return commands;
    }
}
