package ch.sebastianhaeni.edgewars.logic.entities.board.units;

import java.util.ArrayList;

import ch.sebastianhaeni.edgewars.graphics.shapes.IDrawable;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;

public class MeleeUnit extends Unit {
    // TODO adjust values
    private static final int ATTACK_DAMAGE = 5;
    private static final int HEALTH = 5;
    private static final float ACCURACY = 1;
    private static final int SPEED = 5;

    public MeleeUnit(Node node) {
        super(node);
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
    public ArrayList<IDrawable> getDrawables() {
        return null;
    }
}
