package ch.sebastianhaeni.edgewars.logic.ai.rules;

import java.util.ArrayList;

import ch.sebastianhaeni.edgewars.logic.GameState;
import ch.sebastianhaeni.edgewars.logic.commands.Command;

public abstract class Rule {
    private final GameState mState;

    Rule(GameState state) {
        mState = state;
    }

    public abstract boolean applies();

    public abstract ArrayList<Command> getCommands();

    protected GameState getState() {
        return mState;
    }
}
