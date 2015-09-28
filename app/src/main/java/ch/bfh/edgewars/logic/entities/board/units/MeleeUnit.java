package ch.bfh.edgewars.logic.entities.board.units;

import java.util.ArrayList;

import ch.bfh.edgewars.graphics.shapes.Shape;

public class MeleeUnit extends Unit {
    private static final int[] ATTACK_DAMAGE = new int[]{5, 8, 15};
    private static final int[] HEALTH = new int[]{5, 8, 15};
    private static final int[] ACCURACY = new int[]{5, 8, 15};
    private static final int[] SPEED = new int[]{5, 8, 15};
    private static final int[] COST = new int[]{5, 8, 15};

    public MeleeUnit(int level) {
        super(level);
    }

    @Override
    public String getName() {
        return "Melee";
    }

    @Override
    public int getAttackDamage() {
        return ATTACK_DAMAGE[getLevel() - 1];
    }

    @Override
    public int getHealth() {
        return HEALTH[getLevel() - 1];
    }

    @Override
    public int getAccuracy() {
        return ACCURACY[getLevel() - 1];
    }

    @Override
    public int getSpeed() {
        return SPEED[getLevel() - 1];
    }

    public static int getCost(int level) {
        if (level < 1 || level > 3) {
            throw new IllegalArgumentException("Level has to be > 0 and < 4");
        }
        return COST[level - 1];
    }

    @Override
    public ArrayList<Shape> getShapes() {
        return null;
    }
}
