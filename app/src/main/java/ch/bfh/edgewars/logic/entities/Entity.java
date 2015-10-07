package ch.bfh.edgewars.logic.entities;

import android.databinding.BaseObservable;

public abstract class Entity extends BaseObservable{
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
            updateState(millis);
        }
    }

    protected void updateState(long millis) {
    }

    /**
     * Sets the interval this object wants to be updated in.
     *
     * @param interval milliseconds
     */
    public void setUpdateInterval(long interval) {
        mInterval = interval;
    }

}
