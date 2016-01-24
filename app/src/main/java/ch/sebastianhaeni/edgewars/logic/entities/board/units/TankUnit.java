package ch.sebastianhaeni.edgewars.logic.entities.board.units;

import ch.sebastianhaeni.edgewars.logic.Constants;
import ch.sebastianhaeni.edgewars.logic.entities.Player;

/**
 * A tank unit.
 */
public class TankUnit extends Unit {

    /**
     * Constructor
     *
     * @param count  count of the units in this container
     * @param player the owning player
     */
    public TankUnit(int count, Player player) {
        super(count, player);
    }

    @Override
    public int getAttackDamage() {
        return Constants.UNIT_TANK_ATTACK_DAMAGE;
    }

    @Override
    public int getMaxHealth() {
        return Constants.UNIT_TANK_HEALTH;
    }

    @Override
    public float getAccuracy() {
        return Constants.UNIT_TANK_ACCURACY;
    }

    @Override
    public long getSpeed() {
        return Constants.UNIT_TANK_SPEED;
    }

    @Override
    public int getPolygonCorners() {
        return Constants.UNIT_TANK_CORNERS;
    }

}
