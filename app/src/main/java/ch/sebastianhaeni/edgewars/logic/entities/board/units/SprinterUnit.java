package ch.sebastianhaeni.edgewars.logic.entities.board.units;

import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;

/**
 * A sprinter unit.
 */
public class SprinterUnit extends Unit {

    /**
     * Constructor
     *
     * @param count  count of the units in this container
     * @param node   the node this unit goes to
     * @param player the owning player
     */
    public SprinterUnit(int count, Node node, Player player) {
        super(count, node, player);
    }

    @Override
    public String getName() {
        return "Sprinter";
    }

    @Override
    public int getAttackDamage() {
        return UNIT_SPRINTER_ATTACK_DAMAGE;
    }

    @Override
    public int getMaxHealth() {
        return UNIT_SPRINTER_HEALTH;
    }

    @Override
    public float getAccuracy() {
        return UNIT_SPRINTER_ACCURACY;
    }

    @Override
    public long getSpeed() {
        return UNIT_SPRINTER_SPEED;
    }

    @Override
    public int getPolygonCorners() {
        return UNIT_SPRINTER_CORNERS;
    }

}
