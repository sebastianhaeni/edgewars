package ch.bfh.edgewars.logic.entities.board.factories;

import android.databinding.Bindable;

import ch.bfh.edgewars.BR;
import ch.bfh.edgewars.logic.entities.Entity;
import ch.bfh.edgewars.logic.entities.board.node.Node;

@SuppressWarnings("unused")
public abstract class Factory extends Entity {

    private Node mNode;

    private int mLevel = 1;

    private boolean mIsBuilt;
    private int mProducingStack;

    public Factory(Node node) {
        super();
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

    @Bindable
    public int getStackSize() {
        return mProducingStack;
    }

    public void buildUnit() {
        mProducingStack++;
        notifyPropertyChanged(BR.stackSize);
    }

    protected Node getNode() {
        return mNode;
    }

    @Override
    public void update(long millis) {
        if (mProducingStack <= 0) {
            return;
        }
        mProducingStack--;
        produceUnit();
        notifyPropertyChanged(BR.stackSize);
    }

    protected abstract void produceUnit();

    public abstract int getUpgradeCost();

    public abstract int getUnitCost();

    protected abstract long getProducingDuration();

}
