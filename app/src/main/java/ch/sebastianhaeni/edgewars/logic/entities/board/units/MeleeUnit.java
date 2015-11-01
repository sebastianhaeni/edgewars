package ch.sebastianhaeni.edgewars.logic.entities.board.units;

import ch.sebastianhaeni.edgewars.graphics.drawables.shapes.Polygon;
import ch.sebastianhaeni.edgewars.graphics.drawables.shapes.Shape;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;

/**
 * A melee unit.
 */
public class MeleeUnit extends Unit {
    // TODO adjust values
    private static final int ATTACK_DAMAGE = 5;
    private static final int HEALTH = 5;
    private static final float ACCURACY = 1;
    private static final int SPEED = 15;

    /**
     * Constructor
     *
     * @param count count of units in this container
     * @param node  the node this unit starts at
     */
    public MeleeUnit(int count, Node node) {
        super(count, node);
    }

    @Override
    public String getName() {
        return "Melee";
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
        return 3;
    }
}
