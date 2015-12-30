package ch.sebastianhaeni.edgewars.logic.ai;

import ch.sebastianhaeni.edgewars.logic.entities.Player;

public abstract class AI {
    private final Player mPlayer;

    AI(Player player) {
        mPlayer = player;
    }

    public abstract void update(long millis);

    Player getPlayer() {
        return mPlayer;
    }
}
