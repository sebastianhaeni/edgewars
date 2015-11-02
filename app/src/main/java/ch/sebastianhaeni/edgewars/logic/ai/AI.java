package ch.sebastianhaeni.edgewars.logic.ai;

import ch.sebastianhaeni.edgewars.logic.GameState;
import ch.sebastianhaeni.edgewars.logic.entities.Player;

public abstract class AI {
    private final GameState mState;
    private final Player mPlayer;

    AI(GameState state, Player player) {
        mState = state;
        mPlayer = player;
    }

    public abstract void update(long millis);

    protected GameState getState() {
        return mState;
    }

    protected Player getPlayer() {
        return mPlayer;
    }
}
