package ch.sebastianhaeni.edgewars.logic.ai;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import ch.sebastianhaeni.edgewars.logic.GameState;
import ch.sebastianhaeni.edgewars.logic.ai.rules.NodeRules;
import ch.sebastianhaeni.edgewars.logic.commands.Command;
import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;

public class RuleBasedAI extends AI {

    private HashMap<Node, NodeRules> mNodeRules = new HashMap<>();

    public RuleBasedAI(GameState state, Player player) {
        super(state, player);
    }

    @Override
    public void update(long millis) {
        if (!AIAwareness.isInitialized()) {
            return;
        }

        Log.d("debug", "rule based AI update");

        // test if my nodes have changed (gained new ones or lost some)
        ArrayList<Node> nodes = AIAwareness.getMyNodes(getPlayer());
        if (!mNodeRules.keySet().containsAll(nodes) || !nodes.containsAll(mNodeRules.keySet())) {
            updateNodes(nodes);
        }

        ArrayList<Command> commands = new ArrayList<>();
        for (Node node : mNodeRules.keySet()) {
            commands.addAll(mNodeRules.get(node).getCommands(millis));
        }

        for (Command c : commands) {
            c.execute();
        }
    }

    private void updateNodes(ArrayList<Node> nodes) {
        // add new nodes
        for (Node node : nodes) {
            if (!mNodeRules.keySet().contains(node))
                mNodeRules.put(node, new NodeRules(getState(), getPlayer(), node));
        }

        // remove old nodes
        for (Node node : mNodeRules.keySet()) {
            if (!nodes.contains(node))
                mNodeRules.remove(node);
        }
    }

}
