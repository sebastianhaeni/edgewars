package ch.bfh.edgewars.logic.entities;

import ch.bfh.edgewars.logic.ai.AI;

public class Player extends Entity {
    private final AI mAi;
    private int mEnergy;

    public Player() {
        super(1000);
        mAi = null;
    }

    public Player(AI ai) {
        super(1000);
        mAi = ai;
    }

    @Override
    public void update(long millis) {
        if (mAi != null) {
            mAi.update(millis);
        }
    }

    public int getEnergy() {
        return mEnergy;
    }

    public void addEnergy(int amount) {
        mEnergy += amount;
    }
}
