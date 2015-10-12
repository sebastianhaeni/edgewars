package ch.sebastianhaeni.edgewars.logic.entities;

import ch.sebastianhaeni.edgewars.logic.ai.AI;
import ch.sebastianhaeni.edgewars.logic.ai.RuleBasedAI;

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
