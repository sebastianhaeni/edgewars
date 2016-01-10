package ch.sebastianhaeni.edgewars.logic.ai.rules;

import java.util.ArrayList;

import ch.sebastianhaeni.edgewars.logic.GameState;
import ch.sebastianhaeni.edgewars.logic.commands.ActivateFactoryCommand;
import ch.sebastianhaeni.edgewars.logic.commands.Command;
import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;

public class BuildUpRule extends Rule {
    private Node mNode;
    private long mTimePassed;

    public BuildUpRule(Player player) {
        super(player);
    }

    @Override
    public boolean applies(Node node, long millis) {
        mTimePassed += millis;
        if (mTimePassed < 4000) {
            return false;
        }
        mTimePassed = 0;

        mNode = node;

        // rule does not apply if node has already many units
        return !(node.getMeleeCount() > 30 && node.getSprinterCount() > 30 && node.getTankCount() > 30);
    }

    @Override
    public ArrayList<Command> getCommands() {
        ArrayList<Command> commands = new ArrayList<>();

        if (mNode.getMeleeCount() < 30) {
            commands.add(new ActivateFactoryCommand(mNode.getMeleeFactory()));
        } else if (mNode.getSprinterCount() < 30) {
            commands.add(new ActivateFactoryCommand(mNode.getSprinterFactory()));
        } else if (mNode.getTankCount() < 30) {
            commands.add(new ActivateFactoryCommand(mNode.getTankFactory()));
        }

        return commands;
    }
}
