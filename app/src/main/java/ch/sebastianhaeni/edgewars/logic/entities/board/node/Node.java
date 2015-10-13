package ch.sebastianhaeni.edgewars.logic.entities.board.node;

import android.databinding.Bindable;

import java.util.ArrayList;
import java.util.Stack;

import ch.sebastianhaeni.edgewars.BR;
import ch.sebastianhaeni.edgewars.graphics.shapes.Circle;
import ch.sebastianhaeni.edgewars.graphics.shapes.IDrawable;
import ch.sebastianhaeni.edgewars.graphics.shapes.decorators.DeathParticleDecorator;
import ch.sebastianhaeni.edgewars.logic.Game;
import ch.sebastianhaeni.edgewars.logic.commands.MoveUnitCommand;
import ch.sebastianhaeni.edgewars.logic.entities.board.BoardEntity;
import ch.sebastianhaeni.edgewars.logic.entities.board.factories.MeleeFactory;
import ch.sebastianhaeni.edgewars.logic.entities.board.factories.SprinterFactory;
import ch.sebastianhaeni.edgewars.logic.entities.board.factories.TankFactory;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.NeutralState;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.NodeState;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.MeleeUnit;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.SprinterUnit;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.TankUnit;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.Unit;
import ch.sebastianhaeni.edgewars.util.Position;

public class Node extends BoardEntity {

    private final Circle mCircle;
    private ArrayList<MeleeUnit> mMeleeUnits = new ArrayList<>();
    private ArrayList<TankUnit> mTankUnits = new ArrayList<>();
    private ArrayList<SprinterUnit> mSprinterUnits = new ArrayList<>();

    private MeleeFactory mMeleeFactory = new MeleeFactory(this);
    private TankFactory mTankFactory = new TankFactory(this);
    private SprinterFactory mSprinterFactory = new SprinterFactory(this);

    private int mHealth;
    private int mHealthLevel = 1;
    private int mDamageLevel = 1;
    private Position mPosition;
    private ArrayList<IDrawable> mDrawables = new ArrayList<>();

    private Stack<MoveUnitCommand> mMoveUnitCommands = new Stack<>();
    private NodeState mState;

    public Node(Position position) {
        super(50);
        setState(new NeutralState(this));
        mPosition = position;
        mHealth = getMaxHealth();

        mCircle = new Circle(mPosition);

        mDrawables.add(mCircle);
    }

    @Override
    public void update(long millis) {
        if (mMoveUnitCommands.size() > 0) {
            MoveUnitCommand c = mMoveUnitCommands.pop();
            Game.getInstance().register(c);
        }

        mState.update(millis);
    }

    public Position getPosition() {
        return mPosition;
    }

    @Override
    public ArrayList<IDrawable> getDrawables() {
        return mDrawables;
    }

    public void addUnit(MeleeUnit unit) {
        mMeleeUnits.add(unit);
        notifyPropertyChanged(BR.meleeCount);
    }

    public void addUnit(TankUnit unit) {
        mTankUnits.add(unit);
        notifyPropertyChanged(BR.tankCount);
    }

    public void addUnit(SprinterUnit unit) {
        mSprinterUnits.add(unit);
        notifyPropertyChanged(BR.sprinterCount);
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
        notifyPropertyChanged(BR.healthLevel);
    }

    public void upgradeDamage() {
        if (mDamageLevel >= 3) {
            return;
        }
        mDamageLevel++;
        notifyPropertyChanged(BR.damageLevel);
    }

    public void repair() {
        mHealth = getMaxHealth();
        notifyPropertyChanged(BR.health);
    }

    @Bindable
    public int getRepairCost() {
        return getMaxHealth() - getHealth() * 10;
    }

    @Bindable
    public int getHealth() {
        return mHealth;
    }

    @Bindable
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

    @Bindable
    public int getHealthLevel() {
        return mHealthLevel;
    }

    @Bindable
    public int getDamageLevel() {
        return mDamageLevel;
    }

    @Bindable
    public int getMeleeCount() {
        return mMeleeUnits.size();
    }

    @Bindable
    public int getTankCount() {
        return mTankUnits.size();
    }

    @Bindable
    public int getSprinterCount() {
        return mSprinterUnits.size();
    }

    public MeleeFactory getMeleeFactory() {
        return mMeleeFactory;
    }

    public TankFactory getTankFactory() {
        return mTankFactory;
    }

    public SprinterFactory getSprinterFactory() {
        return mSprinterFactory;
    }

    public void sendMeleeUnits(Node node) {
        for (Unit u : mMeleeUnits) {
            mMoveUnitCommands.add(new MoveUnitCommand(u, node));
        }
        mMeleeUnits.clear();
        notifyPropertyChanged(BR.node);
    }

    public void sendTankUnits(Node node) {
        for (Unit u : mTankUnits) {
            mMoveUnitCommands.add(new MoveUnitCommand(u, node));
        }
        mMeleeUnits.clear();
        notifyPropertyChanged(BR.node);
    }

    public void sendSprinterUnits(Node node) {
        for (Unit u : mSprinterUnits) {
            mMoveUnitCommands.add(new MoveUnitCommand(u, node));
        }
        mMeleeUnits.clear();
        notifyPropertyChanged(BR.node);
    }

    public void deductHealth(int attackDamage) {
        int newHealth = mHealth - attackDamage;
        if (newHealth <= 0) {
            setState(new NeutralState(this));
            mHealth = 0;
            return;
        }
        mHealth = newHealth;

        notifyPropertyChanged(BR.health);
        notifyPropertyChanged(BR.repairCost);
    }

    public void setState(NodeState state) {
        mState = state;
        setUpdateInterval(state.getUpdateInterval());
    }

    public void setColor(float[] color) {
        for (IDrawable s : getDrawables()) {
            s.getRootShape().setColor(color);
        }
    }

    @Override
    public String toString() {
        return "Node ["
                + "State: " + (mState == null ? null : mState.toString())
                + ", Position: " + (mPosition == null ? null : (mPosition.getX() + ", " + mPosition.getY()))
                + "]";
    }

    public NodeState getState() {
        return mState;
    }

    public boolean maxHealthLevelReached() {
        return mHealthLevel >= 3;
    }

    public boolean maxDamageLevelReached() {
        return mDamageLevel >= 3;
    }

    public void die() {
        mDrawables.add(new DeathParticleDecorator(mCircle));
        // TODO
    }
}
