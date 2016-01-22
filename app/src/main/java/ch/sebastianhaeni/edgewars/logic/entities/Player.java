package ch.sebastianhaeni.edgewars.logic.entities;

import java.util.Arrays;
import java.util.UUID;

import ch.sebastianhaeni.edgewars.graphics.drawables.shapes.Text;
import ch.sebastianhaeni.edgewars.logic.Constants;
import ch.sebastianhaeni.edgewars.logic.ai.AI;
import ch.sebastianhaeni.edgewars.logic.ai.RuleBasedAI;
import ch.sebastianhaeni.edgewars.util.Colors;
import ch.sebastianhaeni.edgewars.util.Position;

/**
 * A player in the game can take action and issue commands. At least 1 player is always human.
 */
public class Player extends Entity {
    private final UUID mId;
    private final boolean mIsHuman;
    private transient Text mEnergyLabel;
    private transient AI mAi;
    private final float[] mColor;
    private int mEnergy;

    /**
     * Constructor
     *
     * @param color   color for this player
     * @param isHuman if this player is human
     */
    public Player(float[] color, boolean isHuman) {
        super(Constants.PLAYER_UPDATE_INTERVAL);
        mId = UUID.randomUUID();
        mColor = color;
        mIsHuman = isHuman;

    }

    @Override
    public void update(long millis) {

        if (mAi != null) {
            mAi.update(millis);
        }
        if (mEnergyLabel != null) {
            mEnergyLabel.setText(String.valueOf(mEnergy) + Text.ENERGY);
        }
    }

    @Override
    public void register() {
        super.register();
        if (mIsHuman) {
            mEnergyLabel = new Text(
                    new Position(1, .4f),
                    Colors.ENERGY_TEXT, String.valueOf(mEnergy) + Text.ENERGY,
                    10, true);
            mEnergyLabel.register();
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
        setChanged();
        notifyObservers(this);
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

        setChanged();
        notifyObservers(this);
    }

    /**
     * @return true if this player is human or some other sort of organic life form, false otherwise
     */
    public boolean isHuman() {
        return mIsHuman;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Player) {
            return mId.equals(((Player) other).mId);
        }
        return equals(other);
    }

    @Override
    public String toString() {
        return super.toString() + "{UUID=" + mId + ", color=" + Arrays.toString(mColor) + "}";
    }
}
