package ch.bfh.edgewars.logic.ai.rules;

import java.util.ArrayList;

import ch.bfh.edgewars.logic.GameState;
import ch.bfh.edgewars.logic.commands.Command;

public abstract class Rule {
    private final GameState mState;

    public Rule(GameState state) {
        mState = state;
    }

    public abstract boolean applies();

    public abstract ArrayList<Command> getCommands();
}
