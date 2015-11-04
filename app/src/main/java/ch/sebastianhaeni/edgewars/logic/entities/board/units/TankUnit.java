package ch.sebastianhaeni.edgewars.logic.entities.board.units;

import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;

/**
 * A tank unit.
 */
public class TankUnit extends Unit {

    /**
     * Constructor
     *
     * @param count  count of the units in this container
     * @param node   the node this unit goes to
     * @param player the owning player
     */
    public TankUnit(int count, Node node, Player player) {
        super(count, node, player);
    }

    @Override
    public String getName() {
        return "Tank";
    }

    @Override
    public int getAttackDamage() {
        return UNIT_TANK_ATTACK_DAMAGE;
    }

    @Override
    public int getMaxHealth() {
        return UNIT_TANK_HEALTH;
    }

    @Override
    public float getAccuracy() {
        return UNIT_TANK_ACCURACY;
    }

    @Override
    public long getSpeed() {
        return UNIT_TANK_SPEED;
    }

    @Override
    public int getPolygonCorners() {
        return UNIT_TANK_CORNERS;
    }

}
