package ch.sebastianhaeni.edgewars.logic.entities;

import android.databinding.BaseObservable;

import ch.sebastianhaeni.edgewars.logic.Game;

public abstract class Entity extends BaseObservable {
    private long mInterval;

    public Entity(long interval) {
        mInterval = interval;
        Game.getInstance().register(this);
    }

    public Entity() {
        // no op
    }

    public abstract void update(long millis);

    /**
     * Sets the interval this object wants to be updated in.
     *
     * @param interval milliseconds
     */
    public void setUpdateInterval(long interval) {
        mInterval = interval;
        Game.getInstance().register(this);
    }

    public long getInterval() {
        return mInterval;
    }
}
