package ch.sebastianhaeni.edgewars.logic;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import ch.sebastianhaeni.edgewars.graphics.drawables.Drawable;
import ch.sebastianhaeni.edgewars.graphics.drawables.RenderQueue;
import ch.sebastianhaeni.edgewars.logic.commands.Command;
import ch.sebastianhaeni.edgewars.logic.entities.Entity;
import ch.sebastianhaeni.edgewars.logic.entities.board.Edge;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;

/**
 * This class controls the game.
 */
public class Game {

    private static Game mGame;

    private final Stack<Command> mCommandStack = new Stack<>();
    private final HashMap<Entity, Long> mEntities = new HashMap<>();
    private final Stack<Entity> mEntitiesQueue = new Stack<>();
    private final RenderQueue mDrawables = new RenderQueue();
    private boolean mUpdating;

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
     * Resets the singleton and thus a new game is born.
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
        Log.d("Game", "Registering command: " + command);
    }

    /**
     * Registers a new entity to be updated in the game loop.
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
     * Registers a new drawable to be drawn.
     *
     * @param drawable drawable to register.
     * @param layer    layer to draw on
     */
    public void register(Drawable drawable, int layer) {
        mDrawables.add(drawable, layer);
        Log.d("Game", "Registering drawable: " + drawable);
    }

    /**
     * Unregisters a drawable so it's not drawn any longer.
     *
     * @param drawable drawable to unregister
     */
    public void unregister(Drawable drawable) {
        mDrawables.remove(drawable);
        Log.d("Game", "Unregistering drawable: " + drawable);
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
            Entity e = mEntitiesQueue.pop();
            mEntities.put(e, 0L);
            Log.d("Game", "Registering entity: " + e);
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

    /**
     * Gets the edge between two nodes or null.
     *
     * @param node1 node 1
     * @param node2 node 2
     * @return the edge between or <code>null</code>
     */
    public Edge getEdgeBetween(Node node1, Node node2) {
        for (Entity entity : mEntities.keySet()) {
            if (entity instanceof Edge) {
                Edge edge = (Edge) entity;
                if ((edge.getSourceNode().equals(node1) && edge.getTargetNode().equals(node2)) || (edge.getTargetNode().equals(node1) && edge.getSourceNode().equals(node2))) {
                    return edge;
                }
            }
        }

        throw new RuntimeException("No edge between these two nodes.");
    }

    /**
     * @return gets the drawables on the board
     */
    public RenderQueue getDrawables() {
        return mDrawables;
    }

    /**
     * Gets a list of connected nodes.
     *
     * @param node source node (which is not included in the list)
     * @return list of connected nodes
     */
    public List<Node> getConnectedNodes(Node node) {
        List<Node> neighbors = new ArrayList<>();

        for (Entity entity : mEntities.keySet()) {
            if (entity instanceof Edge) {
                Edge edge = (Edge) entity;
                if (edge.getSourceNode().equals(node)) {
                    neighbors.add(edge.getTargetNode());
                } else if (edge.getTargetNode().equals(node)) {
                    neighbors.add(edge.getSourceNode());
                }
            }
        }

        return neighbors;
    }

}

