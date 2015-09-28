package ch.bfh.edgewars.logic.entities.board.units;

import ch.bfh.edgewars.logic.entities.board.BoardEntity;
import ch.bfh.edgewars.logic.entities.board.Edge;
import ch.bfh.edgewars.logic.entities.board.Node;
import ch.bfh.edgewars.logic.entities.board.units.state.IdleState;
import ch.bfh.edgewars.logic.entities.board.units.state.UnitState;

public abstract class Unit extends BoardEntity {

    private UnitState mState;

    private int mLevel;

    private Edge mEdge;
    private Node mHomeNode;
    private Node mAttackNode;
    private Unit mOpponent;
    private float mTravelProgress;

    public Unit(int level) {
        if (level < 1 || level > 3) {
            throw new IllegalArgumentException("Level has to be > 0 and < 4");
        }

        mLevel = level;
        setState(new IdleState(this));
    }

    public abstract String getName();

    public abstract int getAttackDamage();

    public abstract int getHealth();

    public abstract int getAccuracy();

    public abstract int getSpeed();

    public int getLevel() {
        return mLevel;
    }

    public UnitState getState() {
        return mState;
    }

    public void setState(UnitState mState) {
        this.mState = mState;
    }
}
