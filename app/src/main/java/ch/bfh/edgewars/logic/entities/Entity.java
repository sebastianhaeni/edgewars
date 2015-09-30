package ch.bfh.edgewars.logic.entities;

public abstract class Entity {
    private long mInterval;
    private long mWaitingTime;

    public Entity(long interval) {
        mInterval = interval;
    }

    public void update(long millis) {
        if (mInterval < 0) {
            return;
        }
        mWaitingTime += millis;
        if (mWaitingTime > mInterval) {
            mWaitingTime = 0;
            updateState(mWaitingTime);
        }
    }

    protected void updateState(long millis) {
    }

    /**
     * Sets the interval this object wants to be updated in.
     *
     * @param interval milliseconds
     */
    protected void setUpdateInterval(long interval) {
        mInterval = interval;
    }

}
