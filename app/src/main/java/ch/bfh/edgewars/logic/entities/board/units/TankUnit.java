package ch.bfh.edgewars.logic.entities.board.units;

import java.util.ArrayList;

import ch.bfh.edgewars.graphics.shapes.Shape;

public class TankUnit extends Unit {
    private static final int ATTACK_DAMAGE = 5;
    private static final int HEALTH = 5;
    private static final int ACCURACY = 5;
    private static final int SPEED = 5;

    @Override
    public String getName() {
        return "Tank";
    }

    @Override
    public int getAttackDamage() {
        return ATTACK_DAMAGE;
    }

    @Override
    public int getHealth() {
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
