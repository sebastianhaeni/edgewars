package ch.sebastianhaeni.edgewars.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

import ch.sebastianhaeni.edgewars.graphics.GameRenderer;
import ch.sebastianhaeni.edgewars.graphics.GameSurfaceView;
import ch.sebastianhaeni.edgewars.graphics.drawables.Drawable;
import ch.sebastianhaeni.edgewars.graphics.drawables.RenderQueue;
import ch.sebastianhaeni.edgewars.logic.commands.Command;
import ch.sebastianhaeni.edgewars.logic.commands.MoveUnitCommand;
import ch.sebastianhaeni.edgewars.logic.entities.Entity;
import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.Edge;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.OwnedState;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.Unit;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.state.OnEdgeState;
import ch.sebastianhaeni.edgewars.ui.GameController;
import ch.sebastianhaeni.edgewars.ui.IClickable;

/**
 * This class controls the game.
 */
public class Game {

    private static Game mGame;

    private static final int COMMANDS_PER_CYCLE = 5;
    private final Stack<Command> mCommandStack = new Stack<>();
    private final ConcurrentHashMap<Entity, Long> mEntities = new ConcurrentHashMap<>();
    private final RenderQueue mDrawables = new RenderQueue();

    private GameState mGameState;
    private GameController mGameController;
    private GameSurfaceView mGLView;
    private GameRenderer mGameRenderer;
    private boolean mGameOver = false;
    private GameThread mGameThread;

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
        if (isOver()) {
            throw new RuntimeException("The game has not started yet!");
        } else {
            return mGameState;
        }
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

    public void setGLView(GameSurfaceView view) {
        mGLView = view;
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
     */
    public void register(Drawable drawable) {
        mDrawables.add(drawable);
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
     * Unregister an entity.
     *
     * @param entity the entity to be unregistered
     */
    public void unregister(Entity entity) {
        mEntities.remove(entity);
    }

    /**
     * @return true if game is over
     */
    public boolean isOver() {
        return mGameOver || mGameState == null || !mGameState.gameIsRunning();
    }

    /**
     * Initiate check for game over from outside (e.g. when a Node state is updated)
     */
    public void checkGameOver() {
        if (isOver()) {
            return;
        }

        boolean[] result = isGameOver();
        boolean gameOver = result[0];
        boolean won = result[1];
        if (mGLView != null && gameOver) {
            mGameOver = true;
            mGLView.stopLevel(won);
        }
    }

    /**
     * Executes commands (but not too many at once) and updates entities.
     *
     * @param millis time passed since last update
     */
    public void update(long millis) {

        // Executing commands, but not more than 5 per cycle
        int commandCount = 0;

        // Stores the Nodes that have sent units already during this cycle
        ArrayList<Node> moveUnitNodes = new ArrayList<>();

        while (mCommandStack.size() > 0 && commandCount < COMMANDS_PER_CYCLE) {

            // do not continue execution of commands if game has stopped meanwhile
            if (mGameOver) return;

            // test if it is a MoveUnitCommand and ignore if the node has already sent units in this cycle
            Command command = mCommandStack.peek();
            if (command instanceof MoveUnitCommand) {
                MoveUnitCommand moveCommand = (MoveUnitCommand) command;
                Node sourceNode = moveCommand.getSourceNode();
                if (moveUnitNodes.contains(sourceNode)) {
                    // skip this command, since the node has sent units already
                    mCommandStack.pop();
                    continue;
                } else {
                    moveUnitNodes.add(sourceNode);
                }
            }

            mCommandStack.pop().execute();
            commandCount++;
        }

        for (Map.Entry<Entity, Long> pair : mEntities.entrySet()) {
            // do not continue updates if game has stopped meanwhile
            if (mGameOver) return;

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
     * Checks if game is over.
     *
     * @return result Array with two boolean values. First: gameOver?, Second: won?.
     */
    private boolean[] isGameOver() {
        boolean[] result = new boolean[2];
        // check if game is over
        ArrayList<Node> nodes = mGameState.getBoard().getNodes();
        Player owner = null;
        boolean gameOver = true;
        boolean won = false;
        for (Node node : nodes) {
            if (!(node.getState() instanceof OwnedState)) {
                continue;
            }
            Player currentOwner = ((OwnedState) node.getState()).getOwner();
            if (owner == null) {
                owner = currentOwner;
            } else if (!owner.equals(currentOwner)) {
                gameOver = false;
                break;
            }
        }
        if (gameOver && owner != null) {
            won = owner.isHuman();
        }
        result[0] = gameOver;
        result[1] = won;
        return result;
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
     * Gets the Node that is connected to the specified Node via specified Edge.
     *
     * @param node the node
     * @param edge the edge
     * @return the opposite node or <code>null</code>
     */
    public Node getOppositeNode(Node node, Edge edge) {
        List<Node> neighborNodes = this.getConnectedNodes(node);
        for (Node currentNode : neighborNodes) {
            Edge currentEdge = this.getEdgeBetween(node, currentNode);
            if (edge.equals(currentEdge)) {
                return currentNode;
            }
        }
        throw new RuntimeException("This edge is not connected to the node!");
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

    /**
     * Set game renderer
     *
     * @param gameRenderer the renderer of the game
     */
    public void setGameRenderer(GameRenderer gameRenderer) {
        mGameRenderer = gameRenderer;
    }

    /**
     * @return game renderer
     */
    public GameRenderer getGameRenderer() {
        return mGameRenderer;
    }

    /**
     * Sets game thread.
     *
     * @param thread game thread
     */
    public void setGameThread(GameThread thread) {
        mGameThread = thread;
    }

    /**
     * @return gets game thread
     */
    public GameThread getGameThread() {
        return mGameThread;
    }
}
