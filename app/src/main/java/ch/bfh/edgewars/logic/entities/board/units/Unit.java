package ch.bfh.edgewars.logic.entities.board.units;

import ch.bfh.edgewars.logic.entities.board.BoardEntity;
import ch.bfh.edgewars.logic.entities.board.node.Node;
import ch.bfh.edgewars.logic.entities.board.units.state.AttackNodeState;
import ch.bfh.edgewars.logic.entities.board.units.state.DeadState;
import ch.bfh.edgewars.logic.entities.board.units.state.DefendingState;
import ch.bfh.edgewars.logic.entities.board.units.state.FightUnitState;
import ch.bfh.edgewars.logic.entities.board.units.state.IdleState;
import ch.bfh.edgewars.logic.entities.board.units.state.MovingState;
import ch.bfh.edgewars.logic.entities.board.units.state.UnitState;

public abstract class Unit extends BoardEntity {

    private UnitState mState;
    private int mHealth;

    public Unit(Node node) {
        super(-1);
        setUpdateInterval(getSpeed());
        setState(new IdleState(this, node));
        mHealth = getMaxHealth();
    }

    public abstract String getName();

    public abstract int getAttackDamage();

    protected abstract int getMaxHealth();

    public abstract int getAccuracy();

    public abstract long getSpeed();

    public UnitState getState() {
        return mState;
    }

    public void setState(UnitState state) {
        validateStateTransition(getState(), state);
        this.mState = state;
    }

    @Override
    protected void updateState(long millis) {
        getState().update(millis);
    }

    protected void validateStateTransition(UnitState oldState, UnitState newState) {
        // TODO implement state transition checks
        // e.g. a unit cannot fight if it has been attacking
    }

    /**
     * Sends the unit along the edge to the target node.
     *
     * @param node
     */
    public void move(Node node) {
        setState(new MovingState(this, node));
    }

    public void attack(Node node) {
        setState(new AttackNodeState(this, node));
    }

    public void defend(Unit attacker) {
        setState(new DefendingState(this, attacker));
    }

    public void fight(Unit unit) {
        setState(new FightUnitState(this, unit));
    }

    public void idle(Node node) {
        setState(new IdleState(this, node));
    }

    public void deductHealth(int attackDamage) {
        int newHealth = mHealth - attackDamage;
        if (newHealth <= 0) {
            setState(new DeadState(this));
            return;
        }
        mHealth = newHealth;
    }
}
