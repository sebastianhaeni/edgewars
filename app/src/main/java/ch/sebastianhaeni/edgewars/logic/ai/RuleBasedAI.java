package ch.sebastianhaeni.edgewars.logic.ai;

import java.util.ArrayList;

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

        mRules = new ArrayList<>();
        mRules.add(new BuildUpRule(state, getPlayer()));
        mRules.add(new UnderAttackRule(state, getPlayer()));
        mRules.add(new AttackRule(state, getPlayer()));
        mRules.add(new ExposedNodeRule(state, getPlayer()));
    }

    @Override
    public void update(long millis) {
        mCommands.clear();

        ArrayList<Node> nodes = getMyNodes(getPlayer());

        for (Node node : nodes) {
            setNodeCommands(node, millis);
        }

        for (Command c : mCommands) {
            c.execute();
        }
    }

    private void setNodeCommands (Node node, long millis) {

        for (Rule r : mRules) {
            if (r.applies(node, millis)) {
                for (Command c : r.getCommands()) {
                    mCommands.add(c);
                }
            }
        }

    }

    private ArrayList<Node> getMyNodes (Player player) {
        ArrayList<Node> nodes = new ArrayList<>();

        // get all nodes controlled by player
        for (Node n : getState().getBoard().getNodes()) {
            if (n.getState() instanceof OwnedState) {
                OwnedState state = (OwnedState) n.getState();
                Player owner = state.getOwner();
                if (owner.equals(player)) {
                    nodes.add(n);
                }
            }
        }

        return nodes;
    }
}
