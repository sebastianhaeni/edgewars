package ch.sebastianhaeni.edgewars.logic.ai;

import java.util.ArrayList;

import ch.sebastianhaeni.edgewars.logic.GameState;
import ch.sebastianhaeni.edgewars.logic.ai.rules.AttackingRule;
import ch.sebastianhaeni.edgewars.logic.ai.rules.ExposedNodeRule;
import ch.sebastianhaeni.edgewars.logic.ai.rules.IdleRule;
import ch.sebastianhaeni.edgewars.logic.ai.rules.Rule;
import ch.sebastianhaeni.edgewars.logic.ai.rules.UnderAttackRule;
import ch.sebastianhaeni.edgewars.logic.commands.Command;
import ch.sebastianhaeni.edgewars.logic.entities.Player;

public class RuleBasedAI extends AI {

    private final ArrayList<Rule> mRules;
    private final ArrayList<Command> mCommands = new ArrayList<>();

    public RuleBasedAI(GameState state, Player player) {
        super(state, player);

        mRules = new ArrayList<>();
        mRules.add(new IdleRule(state, getPlayer()));
        mRules.add(new UnderAttackRule(state, getPlayer()));
        mRules.add(new AttackingRule(state, getPlayer()));
        mRules.add(new ExposedNodeRule(state, getPlayer()));
    }

    @Override
    public void update(long millis) {
        mCommands.clear();

        for (Rule r : mRules) {
            if (r.applies(millis)) {
                for (Command c : r.getCommands()) {
                    mCommands.add(c);
                }
            }
        }

        for (Command c : mCommands) {
            c.execute();
        }
    }
}
