package ch.sebastianhaeni.edgewars.logic.entities.board.factories;

import ch.sebastianhaeni.edgewars.logic.Constants;
import ch.sebastianhaeni.edgewars.logic.entities.Entity;
import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.OwnedState;

/**
 * A factory can build units inside a node. A factory always exists but has a flag that states if
 * it is built. If the factory has not set isBuilt = true, then no units must be built with this
 * factory.
 */
public abstract class Factory extends Entity {

    private final Node mNode;

    private int mLevel = 1;

    private long mBuildStartTime;

    private boolean mActive;

    /**
     * Constructor
     *
     * @param node the node this factory is at
     */
    Factory(Node node) {
        super();
        setUpdateInterval(getProducingDuration());
        mNode = node;
    }

    /**
     * @return gets the level
     */
    public int getLevel() {
        return mLevel;
    }

    /**
     * Upgrades the factory's level.
     */
    public void upgrade() {
        if (mLevel >= Constants.FACTORY_MAX_LEVEL) {
            return;
        }
        mLevel++;

        setChanged();
        notifyObservers(this);
        setUpdateInterval(getProducingDuration());
    }

    /**
     * @return gets the node this factory is at
     */
    public Node getNode() {
        return mNode;
    }

    @Override
    public void update(long millis) {
        if (!mActive
                || mBuildStartTime + getProducingDuration() > System.currentTimeMillis()
                || !(getNode().getState() instanceof OwnedState)) {
            return;
        }
        mBuildStartTime = System.currentTimeMillis();
        Player owner = ((OwnedState) getNode().getState()).getOwner();
        if (owner.getEnergy() < getUnitCost()) {
            return;
        }
        owner.removeEnergy(getUnitCost());
        produceUnit();

        setChanged();
        notifyObservers(getNode());
    }

    /**
     * Produces a unit by adding it to the node.
     */
    protected abstract void produceUnit();

    /**
     * @return gets the cost of upgrading this factory
     */
    public abstract int getUpgradeCost();

    /**
     * @return gets the cost of building 1 unit
     */
    public abstract int getUnitCost();

    /**
     * @return gets the duration to produce 1 unit
     */
    protected abstract long getProducingDuration();

    /**
     * @return true if the max level has been reached for this factory
     */
    public boolean maxLevelReached() {
        return mLevel >= Constants.FACTORY_MAX_LEVEL;
    }

    /**
     * Deactivates this factory so it stops building units.
     */
    public void deactivate() {
        mActive = false;
    }

    /**
     * Activates this factory to start building units.
     */
    public void activate() {
        mActive = true;
    }

    /**
     * @return gets if this factory is active
     */
    public boolean isActivated() {
        return mActive;
    }
}
