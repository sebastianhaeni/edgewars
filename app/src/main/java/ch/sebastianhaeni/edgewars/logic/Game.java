package ch.sebastianhaeni.edgewars.logic;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import ch.sebastianhaeni.edgewars.logic.commands.Command;
import ch.sebastianhaeni.edgewars.logic.entities.Entity;

/**
 * This class controls the game.
 */
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

    /**
     * @return gets the singleton instance
     */
    public static Game getInstance() {
        if (mGame == null) {
            mGame = new Game();
        }
        return mGame;
    }

    /**
     * Resets the singelton and thus a new game is born.
     */
    public void reset() {
        mGame = null;
    }

    /**
     * Registers a new command to be executed in the game loop.
     *
     * @param command the command to be executed
     */
    public void register(Command command) {
        mCommandStack.push(command);
    }

    /**
     * Registers a new entity  to be updated in the game loop.
     *
     * @param entity the entity to be updated
     */
    public void register(Entity entity) {
        if (mEntities.containsKey(entity)) {
            return;
        }

        if (mUpdating) {
            mEntitiesQueue.push(entity);
            return;
        }

        Log.d("Game", "Registering entity: " + entity);
        mEntities.put(entity, 0L);
    }

    /**
     * Executes commands (but not too many at once) and updates entities.
     *
     * @param millis time passed since last update
     */
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
                continue;
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

