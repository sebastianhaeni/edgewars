package ch.sebastianhaeni.edgewars.logic.entities;

import ch.sebastianhaeni.edgewars.logic.ai.AI;
import ch.sebastianhaeni.edgewars.logic.ai.RuleBasedAI;

/**
 * A player in the game can take action and issue commands. At least 1 player is always human.
 */
public class Player extends Entity {
    private AI mAi;
    private final float[] mColor;
    private int mEnergy;

    /**
     * Constructor
     *
     * @param color color for this player
     */
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

    /**
     * @return gets the player's energy
     */
    public int getEnergy() {
        return mEnergy;
    }

    /**
     * Adds energy to the player.
     *
     * @param amount amount of energy to be added
     */
    public void addEnergy(int amount) {
        mEnergy += amount;
    }

    /**
     * @return gets the player's color
     */
    public float[] getColor() {
        return mColor;
    }

    /**
     * For non-human players the AI has to be set.
     *
     * @param AI the artificial intelligence
     */
    public void setAI(RuleBasedAI AI) {
        mAi = AI;
    }

    /**
     * Removes energy from the player. If the subtraction results in an energy amount below zero,
     * the action is not executed and an exception is thrown instead.
     *
     * @param cost amount to deduct
     */
    public void removeEnergy(int cost) {
        if (mEnergy - cost >= 0) {
            mEnergy -= cost;
        } else {
            throw new RuntimeException("Player energy is lower than 0, this should have been checked before calling this");
        }
    }

    /**
     * @return true if this player is human or some other sort of organic life form, false otherwise
     */
    public boolean isHuman() {
        return mAi == null;
    }
}
