package ch.sebastianhaeni.edgewars.logic.ai;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import ch.sebastianhaeni.edgewars.logic.GameState;
import ch.sebastianhaeni.edgewars.logic.ai.rules.AttackRule;
import ch.sebastianhaeni.edgewars.logic.ai.rules.ExposedNodeRule;
import ch.sebastianhaeni.edgewars.logic.ai.rules.BuildUpRule;
import ch.sebastianhaeni.edgewars.logic.ai.rules.Rule;
import ch.sebastianhaeni.edgewars.logic.ai.rules.UnderAttackRule;
import ch.sebastianhaeni.edgewars.logic.commands.Command;
import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.OwnedState;

public class RuleBasedAI extends AI {

    private final ArrayList<Rule> mRules;
    private final ArrayList<Command> mCommands = new ArrayList<>();


    public RuleBasedAI(GameState state, Player player) {
        super(state, player);

        Log.d("debug", "create new rule based AI");

        mRules = new ArrayList<>();
        mRules.add(new BuildUpRule(state, getPlayer()));
        mRules.add(new UnderAttackRule(state, getPlayer()));
        mRules.add(new AttackRule(state, getPlayer()));
        mRules.add(new ExposedNodeRule(state, getPlayer()));
    }

    @Override
    public void update(long millis) {
        if (!AIAwareness.isInitialized()) {
            return;
        }

        mCommands.clear();

        Log.d("debug", "rule based AI update");

        ArrayList<Node> nodes = AIAwareness.getMyNodes(getPlayer());

        for (Node node : nodes) {
            setNodeCommands(node, millis);
        }

        for (Command c : mCommands) {
            c.execute();
        }
    }

    private synchronized void setNodeCommands (Node node, long millis) {

        Log.d("debug", "setting node commands");

        for (Rule r : mRules) {

            Log.d("debug", "assessing rule " + r.toString());

            if (r.applies(node, millis)) {

                Log.d("debug", "rule "+r.toString()+" applies");

                for (Command c : r.getCommands()) {
                    mCommands.add(c);
                }
            }
        }

    }

}
