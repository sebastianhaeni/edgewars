package ch.bfh.edgewars.logic.entities.board;

import java.util.ArrayList;
import java.util.Stack;

import ch.bfh.edgewars.graphics.shapes.Circle;
import ch.bfh.edgewars.graphics.shapes.Shape;
import ch.bfh.edgewars.logic.commands.SendUnitCommand;
import ch.bfh.edgewars.logic.entities.Player;
import ch.bfh.edgewars.logic.entities.board.units.MeleeUnit;
import ch.bfh.edgewars.logic.entities.board.units.SprinterUnit;
import ch.bfh.edgewars.logic.entities.board.units.TankUnit;
import ch.bfh.edgewars.logic.entities.board.units.Unit;
import ch.bfh.edgewars.util.Position;

public class Node extends BoardEntity {
    private Player mOwner;
    private ArrayList<MeleeUnit> mMeleeUnits = new ArrayList<>();
    private ArrayList<TankUnit> mTankUnits = new ArrayList<>();
    private ArrayList<SprinterUnit> mSprinterUnits = new ArrayList<>();
    private int mHealth;
    private int mHealthLevel = 1;
    private int mDamageLevel = 1;
    private Position mPosition;
    private ArrayList<Shape> mShapes = new ArrayList<>();

    private Stack<SendUnitCommand> mSendUnitCommands = new Stack<>();

    public Node(Player owner, Position position) {
        super(50);
        mOwner = owner;
        mPosition = position;
        mHealth = getMaxHealth();

        mShapes.add(new Circle(mPosition));
    }

    @Override
    protected void updateState(long millis) {
        if (mSendUnitCommands.size() > 0) {
            SendUnitCommand c = mSendUnitCommands.pop();
            c.execute();
        }
    }

    public Position getPosition() {
        return mPosition;
    }

    @Override
    public ArrayList<Shape> getShapes() {
        return mShapes;
    }

    public void addUnit(MeleeUnit unit) {
        mMeleeUnits.add(unit);
    }

    public void addUnit(TankUnit unit) {
        mTankUnits.add(unit);
    }

    public void addUnit(SprinterUnit unit) {
        mSprinterUnits.add(unit);
    }

    public void upgradeHealth() {
        if (mHealthLevel >= 3) {
            return;
        }
        mHealthLevel++;
        repair();
    }

    public void upgradeDamage() {
        if (mDamageLevel >= 3) {
            return;
        }
        mDamageLevel++;
    }

    public void repair() {
        mHealth = getMaxHealth();
    }

    public int getRepairCost() {
        return getMaxHealth() - getHealth() * 10;
    }

    public int getHealth() {
        return mHealth;
    }

    public int getMaxHealth() {
        switch (mHealthLevel) {
            case 1:
                return 100;
            case 2:
                return 150;
            case 3:
                return 220;
            default:
                throw new IllegalStateException("Level must be 1, 2 or 3");
        }
    }

    public void sendMeleeUnits(Node node) {
        for (Unit u : mMeleeUnits) {
            mSendUnitCommands.add(new SendUnitCommand(u, node));
        }
        mMeleeUnits.clear();
    }

    public void sendTankUnits(Node node) {
        for (Unit u : mTankUnits) {
            mSendUnitCommands.add(new SendUnitCommand(u, node));
        }
        mMeleeUnits.clear();
    }

    public void sendSprinterUnits(Node node) {
        for (Unit u : mSprinterUnits) {
            mSendUnitCommands.add(new SendUnitCommand(u, node));
        }
        mMeleeUnits.clear();
    }

}
