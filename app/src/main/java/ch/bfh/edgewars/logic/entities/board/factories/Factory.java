package ch.bfh.edgewars.logic.entities.board.factories;

import ch.bfh.edgewars.logic.entities.Entity;
import ch.bfh.edgewars.logic.entities.board.Node;

public abstract class Factory extends Entity {

    private Node mNode;

    private int mLevel = 1;

    private boolean mIsBuilt;
    private int mProducingStack;

    public Factory(Node node) {
        super(-1);
        setUpdateInterval(getProducingDuration());
        mNode = node;
    }

    public int getLevel() {
        return mLevel;
    }

    public void upgrade() {
        if (mLevel >= 3) {
            return;
        }
        mLevel++;
        setUpdateInterval(getProducingDuration());
    }

    public void build() {
        mIsBuilt = true;
    }

    public boolean isBuilt() {
        return mIsBuilt;
    }

    public void buildUnit() {
        mProducingStack++;
    }

    protected Node getNode() {
        return mNode;
    }

    @Override
    public void updateState(long millis) {
        if (mProducingStack <= 0) {
            return;
        }
        mProducingStack--;
        produceUnit();
    }

    protected abstract void produceUnit();

    public abstract int getUpgradeCost();

    public abstract int getUnitCost();

    protected abstract long getProducingDuration();

}
