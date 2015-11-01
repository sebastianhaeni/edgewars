package ch.sebastianhaeni.edgewars.logic.entities.board.units;

import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;

/**
 * A sprinter unit.
 */
public class SprinterUnit extends Unit {
    // TODO adjust values
    private static final int ATTACK_DAMAGE = 5;
    private static final int HEALTH = 5;
    private static final float ACCURACY = .85f;
    private static final int SPEED = 5;

    /**
     * Constructor
     *
     * @param count count of the units in this container
     * @param node  the node this unit starts at
     */
    public SprinterUnit(int count, Node node) {
        super(count, node);
    }

    @Override
    public String getName() {
        return "Sprinter";
    }

    @Override
    public int getAttackDamage() {
        return ATTACK_DAMAGE;
    }

    @Override
    public int getMaxHealth() {
        return HEALTH;
    }

    @Override
    public float getAccuracy() {
        return ACCURACY;
    }

    @Override
    public long getSpeed() {
        return SPEED;
    }

    @Override
    public int getPolygonCorners() {
        return 5;
    }

}
