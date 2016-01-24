package ch.sebastianhaeni.edgewars.logic.entities.board.units;

import ch.sebastianhaeni.edgewars.logic.Constants;
import ch.sebastianhaeni.edgewars.logic.entities.Player;

/**
 * A sprinter unit.
 */
public class SprinterUnit extends Unit {

    /**
     * Constructor
     *
     * @param count  count of the units in this container
     * @param player the owning player
     */
    public SprinterUnit(int count, Player player) {
        super(count, player);
    }

    @Override
    public int getAttackDamage() {
        return Constants.UNIT_SPRINTER_ATTACK_DAMAGE;
    }

    @Override
    public int getMaxHealth() {
        return Constants.UNIT_SPRINTER_HEALTH;
    }

    @Override
    public float getAccuracy() {
        return Constants.UNIT_SPRINTER_ACCURACY;
    }

    @Override
    public long getSpeed() {
        return Constants.UNIT_SPRINTER_SPEED;
    }

    @Override
    public int getPolygonCorners() {
        return Constants.UNIT_SPRINTER_CORNERS;
    }

}
