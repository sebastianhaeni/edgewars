package ch.bfh.edgewars.logic.ai;

import java.util.ArrayList;

import ch.bfh.edgewars.logic.GameState;
import ch.bfh.edgewars.logic.ai.rules.AttackingRule;
import ch.bfh.edgewars.logic.ai.rules.ExposedNodeRule;
import ch.bfh.edgewars.logic.ai.rules.IdleRule;
import ch.bfh.edgewars.logic.ai.rules.Rule;
import ch.bfh.edgewars.logic.ai.rules.UnderAttackRule;
import ch.bfh.edgewars.logic.commands.Command;

public class RuleBasedAI extends AI {

    private final ArrayList<Rule> mRules;
    private ArrayList<Command> mCommands = new ArrayList<>();

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
