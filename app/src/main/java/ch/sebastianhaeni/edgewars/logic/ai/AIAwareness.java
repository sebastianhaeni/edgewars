package ch.sebastianhaeni.edgewars.logic.ai;

import android.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import ch.sebastianhaeni.edgewars.logic.Game;
import ch.sebastianhaeni.edgewars.logic.GameState;
import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.Edge;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.NeutralState;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.OwnedState;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.Unit;

public class AIAwareness {

    private static boolean isInitialized = false;
    private static GameState mGameState;
    private static ArrayList<Player> mComputerPlayers;
    private static ConcurrentHashMap<Player, ArrayList<Node>> mPlayerNodes;
    private static ConcurrentHashMap<Player, ConcurrentHashMap<Node, Integer>> mPlayerDistancesToEnemy;
    private static ConcurrentHashMap<Player, ConcurrentHashMap<Node, Node>> mPlayerGatewaysToEnemy;
    private static ConcurrentHashMap<Edge, ArrayList<Unit>> mUnitsOnEdges;

    // private constructor to avoid instantiation of the class
    private AIAwareness() {
    }

    /**
     * This method sets up the AI Awareness of the game. Other methods should only be called after call to initialize().
     *
     * @param gameState       the current GameState object
     * @param computerPlayers the current computer players
     */
    public static void initialize(GameState gameState, ArrayList<Player> computerPlayers) {
        mGameState = gameState;
        mComputerPlayers = computerPlayers;
        mPlayerNodes = new ConcurrentHashMap<>();
        mPlayerDistancesToEnemy = new ConcurrentHashMap<>();
        mPlayerGatewaysToEnemy = new ConcurrentHashMap<>();
        mUnitsOnEdges = new ConcurrentHashMap<>();
        recalculate();
        isInitialized = true;
    }

    /**
     * This method checks whether AI Awareness has already been initialized
     *
     * @return true if initialized, false if not
     */
    public static boolean isReady() {
        return isInitialized;
    }

    /**
     * This method updates the internals of AI Awareness. Should be called, when a node state has changed.
     */
    public static void update() {
        if (!isInitialized || Game.getInstance().isOver()) {
            return;
        }

        recalculate();
    }

    /**
     * This method returns all the player's current nodes
     *
     * @param player the player whose nodes shall be returned
     * @return a list of all the player's current nodes
     */
    public static ArrayList<Node> getMyNodes(Player player) {
        return mPlayerNodes.get(player);
    }

    /**
     * Checks whether the player has neighbor nodes that are still neutral.
     * If yes, it returns the first neutral neighbor node that is found.
     *
     * @param node the node to be assessed
     * @return a neutral neighbor node, or null
     */
    public static Node getNeutralNeighbor(Node node) {
        Node neighbor = null;

        for (Node n : Game.getInstance().getConnectedNodes(node)) {
            if (n.getState() instanceof NeutralState) {
                neighbor = n;
                break;
            }
        }

        return neighbor;
    }

    /**
     * Checks whether the player has neighbor nodes that are closer to the enemy.
     * If yes, it returns the one with fewer total unit count.
     *
     * @param node the node to be assessed
     * @return the node towards the enemy with the smallest unit count, or null
     */
    public static Node getBackupTargetNode(Node node) {
        Node backupTarget = null;
        Player player = getPlayer(node);

        for (Node n : Game.getInstance().getConnectedNodes(node)) {
            if (n.getState() instanceof OwnedState) {
                OwnedState state = (OwnedState) n.getState();
                Player owner = state.getOwner();
                if (owner.equals(player) && mPlayerDistancesToEnemy.get(player).get(n) < mPlayerDistancesToEnemy.get(player).get(node)) {
                    if (backupTarget == null)
                        backupTarget = n;
                    int newUnitCount = n.getMeleeCount() + n.getSprinterCount() + n.getTankCount();
                    int tempUnitCount = backupTarget.getMeleeCount() + backupTarget.getSprinterCount() + backupTarget.getTankCount();
                    if (newUnitCount < tempUnitCount)
                        backupTarget = n;
                }
            }
        }

        return backupTarget;
    }

    /**
     * Returns a node's distance to the closest enemy node
     *
     * @param node the node to be assessed
     * @return integer indicating the distance to the closest enemy node
     */
    public static int getDistanceToEnemy(Node node) {
        Player player = getPlayer(node);

        if (!mPlayerDistancesToEnemy.get(player).containsKey(node)) {
            throw new IllegalArgumentException("I am not aware that I own this node!");
        }

        return mPlayerDistancesToEnemy.get(player).get(node);
    }

    /**
     * Returns a node that is connected to the assessed node and is closer to the next enemy node
     *
     * @param node the node to be assessed
     * @return a node connected to the assessed node which is closer to the next enemy node
     */
    public static Node getGatewayToEnemy(Node node) {
        Player player = getPlayer(node);

        if (!mPlayerGatewaysToEnemy.get(player).containsKey(node)) {
            throw new IllegalArgumentException("I am not aware that I own this node!");
        }

        return mPlayerGatewaysToEnemy.get(player).get(node);
    }

    /**
     * Checks whether the player's node is being attacked by enemy units.
     * If yes, it returns the enemy node the units originate from (the first that is found).
     *
     * @param node the node to be assessed
     * @return the first node found where attacking enemy units originate from, or null if no intervention is required
     */
    public static Node getDefenseTargetNode(Node node) {
        Player player = getPlayer(node);

        for (Node n : Game.getInstance().getConnectedNodes(node)) {
            boolean isDefenseTarget = true;
            Edge edge = Game.getInstance().getEdgeBetween(node, n);
            if (mUnitsOnEdges.containsKey(edge) && mUnitsOnEdges.get(edge).size() > 0) { // units are present on edge
                for (Unit unit : mUnitsOnEdges.get(edge)) {
                    if (unit.getPlayer().equals(player)) {  // own units are already on edge, no intervention needed
                        isDefenseTarget = false;
                        break;
                    }
                }
                if (isDefenseTarget) {  // only enemy units are present on node, intervention is required
                    return n;
                }
            }
        }

        return null; // no Edge requires an intervention
    }

    /**
     * Updates the AIAwareness with information that a new unit is on an edge
     *
     * @param edge the edge the unit is on
     * @param unit the unit to be added
     */
    public static void addUnitOnEdge(Edge edge, Unit unit) {
        if (mUnitsOnEdges.containsKey(edge)) { // edge already present in HashMap
            if (mUnitsOnEdges.get(edge).contains(unit)) {
                throw new RuntimeException("I have already added this unit to the edge!");
            }
            mUnitsOnEdges.get(edge).add(unit);
        } else {    // edge not yet present in HashMap
            ArrayList<Unit> units = new ArrayList<>();
            units.add(unit);
            mUnitsOnEdges.put(edge, units);
        }
    }

    /**
     * Updated the AIAwareness with information that a unit is not on an edge any more
     *
     * @param edge the edge the unit was on
     * @param unit the unit to be removed
     */
    public static void removeUnitOnEdge(Edge edge, Unit unit) {
        if (!mUnitsOnEdges.containsKey(edge) || !mUnitsOnEdges.get(edge).contains(unit)) {
            throw new RuntimeException("I had no awareness of units from this player on this edge and hence cannot remove them!");
        }

        mUnitsOnEdges.get(edge).remove(unit);
    }

    private static void recalculate() {
        mPlayerNodes.clear();
        mPlayerDistancesToEnemy.clear();
        mPlayerGatewaysToEnemy.clear();

        for (Player player : mComputerPlayers) {
            prepareNodes(player);
            prepareDistances(player);
        }
    }

    private static Player getPlayer(Node node) {
        for (Player player : mPlayerNodes.keySet()) {
            ArrayList<Node> playerNodes = mPlayerNodes.get(player);
            for (Node playerNode : playerNodes) {
                if (node.equals(playerNode))
                    return player;
            }
        }
        throw new IllegalArgumentException("This node belongs to no computer player!"); // no AI player found to which the node belongs
    }

    private static void prepareNodes(Player player) {
        ArrayList<Node> nodes = new ArrayList<>();        // get all nodes controlled by player
        for (Node n : mGameState.getBoard().getNodes()) {
            if (n.getState() instanceof OwnedState) {
                OwnedState state = (OwnedState) n.getState();
                Player owner = state.getOwner();
                if (owner.equals(player)) {
                    nodes.add(n);
                }
            }
        }

        mPlayerNodes.put(player, nodes);
    }

    private static void prepareDistances(Player player) {
        ConcurrentHashMap<Node, Integer> distancesToEnemy = new ConcurrentHashMap<>();
        ConcurrentHashMap<Node, Node> gatewaysToEnemy = new ConcurrentHashMap<>();

        for (Node node : mPlayerNodes.get(player)) {
            Pair<Integer, Node> result = bfs(player, node);
            if (result == null)   // no connected enemy node was found
                return;
            distancesToEnemy.put(node, result.first);
            gatewaysToEnemy.put(node, result.second);
        }

        mPlayerDistancesToEnemy.put(player, distancesToEnemy);
        mPlayerGatewaysToEnemy.put(player, gatewaysToEnemy);
    }

    // BFS
    private static Pair<Integer, Node> bfs(Player player, Node node) {

        int currentDistance = 0;
        ArrayList<Node> visitedNodes = new ArrayList<>();
        ArrayList<Edge> discoveryEdges = new ArrayList<>();

        LinkedList<Node> queue = new LinkedList<>();

        visitedNodes.add(node);
        queue.addLast(node);

        while (!queue.isEmpty()) {
            currentDistance++;

            LinkedList<Node> tempQueue = new LinkedList<>();
            while (!queue.isEmpty()) {
                tempQueue.add(queue.removeFirst());
            }

            while (!tempQueue.isEmpty()) {
                Node currentNode = tempQueue.removeFirst();

                for (Node neighborNode : Game.getInstance().getConnectedNodes(currentNode)) {

                    if (!visitedNodes.contains(neighborNode)) {
                        visitedNodes.add(neighborNode);
                        queue.add(neighborNode);
                        discoveryEdges.add(Game.getInstance().getEdgeBetween(currentNode, neighborNode));
                    }

                    if (neighborNode.getState() instanceof OwnedState) {
                        OwnedState state = (OwnedState) neighborNode.getState();
                        if (!player.equals(state.getOwner())) {
                            Node gatewayTowardsEnemy = getGatewayTowardsEnemy(node, neighborNode, discoveryEdges);
                            return new Pair<>(currentDistance, gatewayTowardsEnemy);
                        }
                    }
                }
            }

        }

        return null;

    }

    private static Node getGatewayTowardsEnemy(Node node, Node closestEnemyNode, ArrayList<Edge> discoveryEdges) {

        Node currentNode = closestEnemyNode;
        boolean done = false;

        while (!done) {

            for (Edge edge : discoveryEdges) {
                if (edge.getTargetNode().equals(currentNode)) {
                    if (edge.getSourceNode().equals(node)) {
                        done = true;
                    } else {
                        currentNode = edge.getSourceNode();
                        discoveryEdges.remove(edge);
                    }
                    break;
                } else if (edge.getSourceNode().equals(currentNode)) {
                    if (edge.getTargetNode().equals(node)) {
                        done = true;
                    } else {
                        currentNode = edge.getTargetNode();
                        discoveryEdges.remove(edge);
                    }
                    break;
                }
            }

        }

        return currentNode;
    }

}
