package ch.sebastianhaeni.edgewars.logic.ai.rules;

import java.util.ArrayList;

import ch.sebastianhaeni.edgewars.logic.GameState;
import ch.sebastianhaeni.edgewars.logic.commands.Command;
import ch.sebastianhaeni.edgewars.logic.entities.Player;

public abstract class Rule {
    private final GameState mState;
    private Player mPlayer;

    Rule(GameState state, Player player) {
        mState = state;
        mPlayer = player;
    }

    public abstract boolean applies(long millis);

    public abstract ArrayList<Command> getCommands();

    protected GameState getState() {
        return mState;
    }

    protected Player getPlayer() {
        return mPlayer;
    }
}
