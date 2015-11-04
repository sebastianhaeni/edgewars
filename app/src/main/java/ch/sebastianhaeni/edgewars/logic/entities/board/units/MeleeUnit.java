package ch.sebastianhaeni.edgewars.logic.entities.board.units;

import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;

/**
 * A melee unit.
 */
public class MeleeUnit extends Unit {

    /**
     * Constructor
     *
     * @param count  count of units in this container
     * @param node   the node this unit goes to
     * @param player the owning player
     */
    public MeleeUnit(int count, Node node, Player player) {
        super(count, node, player);
    }

    @Override
    public String getName() {
        return "Melee";
    }

    @Override
    public int getAttackDamage() {
        return UNIT_MELEE_ATTACK_DAMAGE;
    }

    @Override
    public int getMaxHealth() {
        return UNIT_MELEE_HEALTH;
    }

    @Override
    public float getAccuracy() {
        return UNIT_MELEE_ACCURACY;
    }

    @Override
    public long getSpeed() {
        return UNIT_MELEE_SPEED;
    }

    @Override
    public int getPolygonCorners() {
        return UNIT_MELEE_CORNERS;
    }
}
