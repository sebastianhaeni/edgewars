package ch.sebastianhaeni.edgewars.logic.entities;

import java.util.ArrayList;
import java.util.List;

import ch.sebastianhaeni.edgewars.ui.IClickable;
import ch.sebastianhaeni.edgewars.util.Position;

/**
 * An in-game button.
 */
public abstract class Button extends Entity implements IClickable {

    private final Position mPosition;
    private List<OnGameClickListener> mListeners = new ArrayList<>();

    public Button(Position position) {
        mPosition = position;
    }

    public void addClickListener(OnGameClickListener listener) {
        mListeners.add(listener);
    }

    @Override
    public Position getPosition() {
        return mPosition;
    }

    @Override
    public void onClick() {
        for (OnGameClickListener listener : mListeners) {
            listener.onClick();
        }
    }

    @Override
    public void onUnhandledClick() {
        // no op
    }

    @Override
    public void update(long millis) {
        // no op
    }

    /**
     * Interface for listeners of the in game button.
     */
    public interface OnGameClickListener {
        void onClick();
    }
}
