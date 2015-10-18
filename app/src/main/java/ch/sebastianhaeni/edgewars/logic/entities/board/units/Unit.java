package ch.sebastianhaeni.edgewars.logic.entities.board.units;

import ch.sebastianhaeni.edgewars.logic.entities.board.BoardEntity;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.OwnedState;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.state.DeadState;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.state.IdleState;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.state.MovingState;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.state.UnitState;

/**
 * A unit owned by a player and produced at a node from a factory.
 */
public abstract class Unit extends BoardEntity {

    private UnitState mState;
    private int mHealth;

    /**
     * Constructor
     *
     * @param node the node this unit was produced at
     */
    public Unit(Node node) {
        super(-1);
        setUpdateInterval(getSpeed());
        setState(new IdleState(this));
        mHealth = getMaxHealth();
    }

    /**
     * @return gets the name of this unit type
     */
    public abstract String getName();

    /**
     * @return gets the attack damage of this unit
     */
    public abstract int getAttackDamage();

    /**
     * @return gets the max health of this unit
     */
    protected abstract int getMaxHealth();

    /**
     * @return gets the accuracy of this unit in percent (0.0 - 1.0)
     */
    public abstract float getAccuracy();

    /**
     * @return gets the time in which the unit goes a fixed distance
     */
    public abstract long getSpeed();

    /**
     * @return gets the unit's state
     */
    public UnitState getState() {
        return mState;
    }

    /**
     * Sets the unit's state.
     *
     * @param state the new state
     */
    public void setState(UnitState state) {
        this.mState = state;
        setUpdateInterval(state.getUpdateInterval());
    }

    @Override
    public void update(long millis) {
        getState().update(millis);
    }

    /**
     * Sends the unit along the edge to the target node.
     *
     * @param node to which node the unit should move
     */
    public void move(Node node) {
        setState(new MovingState(this, node, ((OwnedState) node.getState()).getOwner()));
    }

    /**
     * Deducts the unit's health with a damage value. If the health goes <= 0 then the DeadState
     * is set.
     *
     * @param attackDamage amount of damage inflicted
     */
    public void deductHealth(int attackDamage) {
        int newHealth = mHealth - attackDamage;
        if (newHealth <= 0) {
            setState(new DeadState(this));
            return;
        }
        mHealth = newHealth;
    }

}
