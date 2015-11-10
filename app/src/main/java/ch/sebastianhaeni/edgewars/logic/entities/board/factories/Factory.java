package ch.sebastianhaeni.edgewars.logic.entities.board.factories;

import android.databinding.Bindable;

import ch.sebastianhaeni.edgewars.logic.Constants;
import ch.sebastianhaeni.edgewars.logic.entities.Entity;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;

/**
 * A factory can build units inside a node. A factory always exists but has a flag that states if
 * it is built. If the factory has not set isBuilt = true, then no units must be built with this
 * factory.
 */
public abstract class Factory extends Entity {

    private final Node mNode;

    private int mLevel = 1;

    private int mProducingStack;
    private long mBuildStartTime;

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
    @Bindable
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
        setUpdateInterval(getProducingDuration());
    }

    /**
     * Schedules a new unit to be built by increasing the stack size. A unit needs a certain time
     * to be produced, this is handled in the update method.
     */
    public void buildUnit() {
        if (mProducingStack == 0) {
            mBuildStartTime = System.currentTimeMillis();
        }

        mProducingStack++;
    }

    /**
     * @return gets the node this factory is at
     */
    public Node getNode() {
        return mNode;
    }

    @Override
    public void update(long millis) {
        if (mProducingStack <= 0 || mBuildStartTime + getProducingDuration() > System.currentTimeMillis()) {
            return;
        }
        mBuildStartTime = System.currentTimeMillis();
        mProducingStack--;
        produceUnit();
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

}
