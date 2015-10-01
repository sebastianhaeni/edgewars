package ch.bfh.edgewars.logic.entities.board.node;

import java.util.ArrayList;
import java.util.Stack;

import ch.bfh.edgewars.graphics.shapes.Circle;
import ch.bfh.edgewars.graphics.shapes.Shape;
import ch.bfh.edgewars.logic.commands.MoveUnitCommand;
import ch.bfh.edgewars.logic.entities.board.BoardEntity;
import ch.bfh.edgewars.logic.entities.board.node.state.NeutralState;
import ch.bfh.edgewars.logic.entities.board.node.state.NodeState;
import ch.bfh.edgewars.logic.entities.board.units.MeleeUnit;
import ch.bfh.edgewars.logic.entities.board.units.SprinterUnit;
import ch.bfh.edgewars.logic.entities.board.units.TankUnit;
import ch.bfh.edgewars.logic.entities.board.units.Unit;
import ch.bfh.edgewars.util.Position;

public class Node extends BoardEntity {

    private ArrayList<MeleeUnit> mMeleeUnits = new ArrayList<>();
    private ArrayList<TankUnit> mTankUnits = new ArrayList<>();
    private ArrayList<SprinterUnit> mSprinterUnits = new ArrayList<>();
    private int mHealth;
    private int mHealthLevel = 1;
    private int mDamageLevel = 1;
    private Position mPosition;
    private ArrayList<Shape> mShapes = new ArrayList<>();

    private Stack<MoveUnitCommand> mMoveUnitCommands = new Stack<>();
    private NodeState mState;

    public Node(Position position) {
        super(50);
        setState(new NeutralState(this));
        mPosition = position;
        mHealth = getMaxHealth();

        mShapes.add(new Circle(mPosition));
    }

    @Override
    protected void updateState(long millis) {
        if (mMoveUnitCommands.size() > 0) {
            MoveUnitCommand c = mMoveUnitCommands.pop();
            c.execute();
        }

        mState.update(millis);
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

    public void addUnit(Unit unit) {
        if (unit instanceof MeleeUnit) {
            mMeleeUnits.add((MeleeUnit) unit);
            return;
        }
        if (unit instanceof TankUnit) {
            mTankUnits.add((TankUnit) unit);
            return;
        }
        if (unit instanceof SprinterUnit) {
            mSprinterUnits.add((SprinterUnit) unit);
        }
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
            mMoveUnitCommands.add(new MoveUnitCommand(u, node));
        }
        mMeleeUnits.clear();
    }

    public void sendTankUnits(Node node) {
        for (Unit u : mTankUnits) {
            mMoveUnitCommands.add(new MoveUnitCommand(u, node));
        }
        mMeleeUnits.clear();
    }

    public void sendSprinterUnits(Node node) {
        for (Unit u : mSprinterUnits) {
            mMoveUnitCommands.add(new MoveUnitCommand(u, node));
        }
        mMeleeUnits.clear();
    }

    public void deductHealth(int attackDamage) {
        int newHealth = mHealth - attackDamage;
        if (newHealth <= 0) {
            setState(new NeutralState(this));
            mHealth = 0;
            return;
        }
        mHealth = newHealth;
    }

    public void setState(NodeState state) {
        mState = state;
        setUpdateInterval(state.getUpdateInterval());
    }

    public void setColor(float[] color) {
        for (Shape s : getShapes()) {
            s.setColor(color);
        }
    }

    @Override
    public String toString() {
        return "Node ["
                + "\n\tState: " + mState.toString()
                + "\n\tPosition: " + mPosition.getX() + ", " + mPosition.getY()
                + "\n]";
    }

    public NodeState getState() {
        return mState;
    }
}
