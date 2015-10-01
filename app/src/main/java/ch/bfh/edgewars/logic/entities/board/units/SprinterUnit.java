package ch.bfh.edgewars.logic.entities.board.units;

import java.util.ArrayList;

import ch.bfh.edgewars.graphics.shapes.Shape;
import ch.bfh.edgewars.logic.entities.board.node.Node;

public class SprinterUnit extends Unit {
    // TODO adjust values
    private static final int ATTACK_DAMAGE = 5;
    private static final int HEALTH = 5;
    private static final int ACCURACY = 5;
    private static final int SPEED = 5;

    public SprinterUnit(Node node) {
        super(node);
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
    public int getAccuracy() {
        return ACCURACY;
    }

    @Override
    public long getSpeed() {
        return SPEED;
    }

    @Override
    public ArrayList<Shape> getShapes() {
        return null;
    }
}
