package ch.sebastianhaeni.edgewars.logic;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

import ch.sebastianhaeni.edgewars.graphics.drawables.Drawable;
import ch.sebastianhaeni.edgewars.graphics.drawables.RenderQueue;
import ch.sebastianhaeni.edgewars.logic.commands.Command;
import ch.sebastianhaeni.edgewars.logic.entities.Entity;
import ch.sebastianhaeni.edgewars.logic.entities.board.Edge;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.Unit;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.state.OnEdgeState;
import ch.sebastianhaeni.edgewars.ui.IClickable;
import ch.sebastianhaeni.edgewars.ui.GameController;

/**
 * This class controls the game.
 */
public class Game {

    private static Game mGame;

    private GameState mGameState;
    private GameController mGameController;
    private final Stack<Command> mCommandStack = new Stack<>();
    private final ConcurrentHashMap<Entity, Long> mEntities = new ConcurrentHashMap<>();
    private final RenderQueue mDrawables = new RenderQueue();

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

    public GameState getGameState() {
        return mGameState;
    }

    public void setGameState(GameState gameState) {
        mGameState = gameState;
    }

    public GameController getGameController() {
        return mGameController;
    }

    public void setGameController(GameController gameController) {
        mGameController = gameController;
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
    }

    /**
     * Unregisters a drawable so it's not drawn any longer.
     *
     * @param drawable drawable to unregister
     */
    public void unregister(Drawable drawable) {
        mDrawables.remove(drawable);
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

        for (Map.Entry<Entity, Long> pair : mEntities.entrySet()) {
            if (pair.getKey().getInterval() < 0) {
                continue;
            }
            pair.setValue(pair.getValue() + millis);
            mEntities.put(pair.getKey(), pair.getValue());

            if (pair.getValue() > pair.getKey().getInterval()) {
                pair.getKey().update(pair.getValue());
                pair.setValue(0L);
            }
        }
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

                if ((edge.getSourceNode().equals(node1) && edge.getTargetNode().equals(node2))
                        || (edge.getTargetNode().equals(node1) && edge.getSourceNode().equals(node2))) {
                    return edge;
                }
            }
        }

        throw new RuntimeException("No edge between these two nodes: "
                + node1.getPosition() + ", " + node2.getPosition());
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

    /**
     * Gets all units that are traversing an edge.
     *
     * @param edge the edge traversed
     * @return list of units on the edge
     */
    public List<Unit> getUnitsOnEdge(Edge edge) {
        List<Unit> units = new ArrayList<>();

        for (Entity entity : mEntities.keySet()) {
            if (!(entity instanceof Unit)) {
                continue;
            }
            Unit u = (Unit) entity;
            if (!(u.getState() instanceof OnEdgeState)) {
                continue;
            }
            OnEdgeState state = (OnEdgeState) u.getState();
            if (state.getEdge().equals(edge)) {
                units.add(u);
            }
        }

        return units;
    }

    /**
     * @return gets a list of clickable elements.
     */
    public List<IClickable> getClickables() {
        List<IClickable> clickables = new ArrayList<>();

        for (Entity entity : mEntities.keySet()) {
            if (!(entity instanceof IClickable)) {
                continue;
            }

            clickables.add((IClickable) entity);
        }

        return clickables;
    }
}

