package ch.sebastianhaeni.edgewars.logic.ai;

import java.util.ArrayList;

import ch.sebastianhaeni.edgewars.logic.GameState;
import ch.sebastianhaeni.edgewars.logic.ai.rules.AttackingRule;
import ch.sebastianhaeni.edgewars.logic.ai.rules.ExposedNodeRule;
import ch.sebastianhaeni.edgewars.logic.ai.rules.IdleRule;
import ch.sebastianhaeni.edgewars.logic.ai.rules.Rule;
import ch.sebastianhaeni.edgewars.logic.ai.rules.UnderAttackRule;
import ch.sebastianhaeni.edgewars.logic.commands.Command;

public class RuleBasedAI extends AI {

    private final ArrayList<Rule> mRules;
    private final ArrayList<Command> mCommands = new ArrayList<>();

    public RuleBasedAI(GameState state) {
        super(state);

        mRules = new ArrayList<>();
        mRules.add(new IdleRule(state));
        mRules.add(new UnderAttackRule(state));
        mRules.add(new AttackingRule(state));
        mRules.add(new ExposedNodeRule(state));
    }

    @Override
    public void update(long millis) {
        mCommands.clear();

        for (Rule r : mRules) {
            if (r.applies()) {
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
