package ch.sebastianhaeni.edgewars.logic.ai.rules;

import java.util.ArrayList;

import ch.sebastianhaeni.edgewars.logic.commands.BuildUnitCommand;
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
        return !(node.getMeleeCount() > 20 && node.getSprinterCount() > 20 && node.getTankCount() > 20);
    }

    @Override
    public ArrayList<Command> getCommands() {
        ArrayList<Command> commands = new ArrayList<>();

        commands.add(new BuildUnitCommand(mNode.getMeleeFactory()));
        commands.add(new BuildUnitCommand(mNode.getSprinterFactory()));
        commands.add(new BuildUnitCommand(mNode.getTankFactory()));

        return commands;
    }
}
