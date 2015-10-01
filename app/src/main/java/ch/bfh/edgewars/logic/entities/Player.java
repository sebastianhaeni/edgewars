package ch.bfh.edgewars.logic.entities;

import ch.bfh.edgewars.logic.ai.AI;
import ch.bfh.edgewars.logic.ai.RuleBasedAI;

public class Player extends Entity {
    private AI mAi;
    private final float[] mColor;
    private int mEnergy;

    public Player(float[] color) {
        super(1000);
        mColor = color;
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

    public void setAI(RuleBasedAI AI) {
        mAi = AI;
    }
}
