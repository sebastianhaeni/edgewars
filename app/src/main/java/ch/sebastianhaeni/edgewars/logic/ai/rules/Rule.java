package ch.sebastianhaeni.edgewars.logic.ai.rules;

import java.util.ArrayList;

import ch.sebastianhaeni.edgewars.logic.GameState;
import ch.sebastianhaeni.edgewars.logic.commands.Command;
import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;

public abstract class Rule {
    private final GameState mState;
    private Player mPlayer;

    Rule(GameState state, Player player) {
        mState = state;
        mPlayer = player;
    }

    public abstract boolean applies(Node node, long millis);

    public abstract ArrayList<Command> getCommands();

    protected GameState getState() {
        return mState;
    }

    protected Player getPlayer() {
        return mPlayer;
    }
}
