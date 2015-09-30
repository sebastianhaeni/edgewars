package ch.bfh.edgewars.logic.entities;

import ch.bfh.edgewars.logic.ai.AI;

public class Player extends Entity {
    private final AI mAi;
    private final float[] mColor;
    private int mEnergy;

    public Player(float[] color) {
        super(1000);
        mColor = color;
        mAi = null;
    }

    public Player(float[] color, AI ai) {
        super(1000);
        mColor = color;
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

    public float[] getColor() {
        return mColor;
    }
}
