package ch.sebastianhaeni.edgewars.logic.ai;

import ch.sebastianhaeni.edgewars.logic.GameState;

public abstract class AI {
    private final GameState mState;

    public AI(GameState state) {
        mState = state;
    }

    public abstract void update(long millis);

    protected GameState getState() {
        return mState;
    }
}
