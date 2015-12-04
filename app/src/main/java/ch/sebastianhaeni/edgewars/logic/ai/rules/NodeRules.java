package ch.sebastianhaeni.edgewars.logic.ai.rules;

import java.util.ArrayList;

import ch.sebastianhaeni.edgewars.logic.GameState;
import ch.sebastianhaeni.edgewars.logic.commands.Command;
import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;

public class NodeRules {

    private final ArrayList<Rule> mRules = new ArrayList<>();
    private final Node mNode;

    public NodeRules(GameState state, Player player, Node node) {
        mNode = node;

        mRules.add(new BuildUpRule(state, player));
        mRules.add(new BackupRule(state, player));
        mRules.add(new AttackRule(state, player));
        mRules.add(new DefenseRule(state, player));
        mRules.add(new ConquerRule(state, player));
        mRules.add(new RepairRule(state, player));
    }

    public ArrayList<Command> getCommands(long millis) {

        ArrayList<Command> commands = new ArrayList<>();
        for (Rule r : mRules) {

            if (r.applies(mNode, millis)) {

                for (Command c : r.getCommands()) {
                    commands.add(c);
                }
            }
        }

        return commands;

    }
}
