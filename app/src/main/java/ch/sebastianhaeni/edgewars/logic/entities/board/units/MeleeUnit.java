package ch.sebastianhaeni.edgewars.logic.entities.board.units;

import ch.sebastianhaeni.edgewars.logic.Constants;
import ch.sebastianhaeni.edgewars.logic.entities.Player;

/**
 * A melee unit.
 */
public class MeleeUnit extends Unit {

    /**
     * Constructor
     *
     * @param count  count of units in this container
     * @param player the owning player
     */
    public MeleeUnit(int count, Player player) {
        super(count, player);
    }

    @Override
    public int getAttackDamage() {
        return Constants.UNIT_MELEE_ATTACK_DAMAGE;
    }

    @Override
    public int getMaxHealth() {
        return Constants.UNIT_MELEE_HEALTH;
    }

    @Override
    public float getAccuracy() {
        return Constants.UNIT_MELEE_ACCURACY;
    }

    @Override
    public long getSpeed() {
        return Constants.UNIT_MELEE_SPEED;
    }

    @Override
    public int getPolygonCorners() {
        return Constants.UNIT_MELEE_CORNERS;
    }
}
