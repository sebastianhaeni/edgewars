package ch.sebastianhaeni.edgewars.logic.ai.rules;

import java.util.ArrayList;

import ch.sebastianhaeni.edgewars.EUnitType;
import ch.sebastianhaeni.edgewars.logic.Game;
import ch.sebastianhaeni.edgewars.logic.GameState;
import ch.sebastianhaeni.edgewars.logic.commands.BuildUnitCommand;
import ch.sebastianhaeni.edgewars.logic.commands.Command;
import ch.sebastianhaeni.edgewars.logic.commands.MoveUnitCommand;
import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.NeutralState;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.OwnedState;

public class BuildUpRule extends Rule {
    private Node mNode;
    private Node mTarget;
    private long mTimePassed;

    public BuildUpRule(GameState state, Player player) {
        super(state, player);
    }

    @Override
    public boolean applies(Node node, long millis) {
        mTimePassed += millis;
        if (mTimePassed < 4000) {
            return false;
        }
        mTimePassed = 0;

        mNode = node;
        mTarget = null;

        // rule does not apply if node has already many units
        if (node.getMeleeCount()>20 && node.getSprinterCount()>20 && node.getTankCount()>20) {
            return false;
        }

        return true;
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
