package ch.bfh.edgewars.logic;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import ch.bfh.edgewars.logic.commands.Command;
import ch.bfh.edgewars.logic.entities.Entity;
import ch.bfh.edgewars.logic.entities.Player;
import ch.bfh.edgewars.util.Colors;

public class Game {

    private static Game mGame;

    private Stack<Command> mCommandStack = new Stack<>();
    private HashMap<Entity, Long> mEntities = new HashMap<>();
    private boolean mUpdating;
    private Stack<Entity> mEntitiesQueue = new Stack<>();

    /**
     * Privatised constructor. Because singleton.
     */
    private Game() {
    }

    public static Game getInstance() {
        if (mGame == null) {
            mGame = new Game();
        }
        return mGame;
    }

    public void reset() {
        mGame = null;
    }

    public void register(Command command) {
        mCommandStack.push(command);
    }

    public void register(Entity entity) {
        if (mEntities.containsKey(entity)) {
            return;
        }

        if (mUpdating) {
            mEntitiesQueue.push(entity);
            return;
        }

        mEntities.put(entity, 0L);
    }

    public void update(long millis) {
        // Executing commands, but not more than 5 per cycle
        int commandCount = mCommandStack.size();
        while (mCommandStack.size() > 0 && mCommandStack.size() - commandCount < 5) {
            mCommandStack.pop().execute();
        }

        // Add entities in queue to entity list
        while (mEntitiesQueue.size() > 0) {
            mEntities.put(mEntitiesQueue.pop(), 0L);
        }

        // Update entities
        mUpdating = true;
        for (Map.Entry<Entity, Long> pair : mEntities.entrySet()) {
            if (pair.getKey().getInterval() < 0) {
                return;
            }
            pair.setValue(pair.getValue() + millis);

            if (pair.getValue() > pair.getKey().getInterval()) {
                pair.getKey().update(pair.getValue());
                pair.setValue(0L);
            }
        }
        mUpdating = false;
    }
}

