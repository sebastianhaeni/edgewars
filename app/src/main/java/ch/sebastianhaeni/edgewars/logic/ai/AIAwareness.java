package ch.sebastianhaeni.edgewars.logic.ai;

import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import ch.sebastianhaeni.edgewars.logic.Game;
import ch.sebastianhaeni.edgewars.logic.GameState;
import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.Edge;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.NeutralState;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.OwnedState;

public class AIAwareness {

    private static boolean isInitialized = false;
    private static GameState mGameState;
    private static ArrayList<Player> mComputerPlayers;
    private static HashMap<Player, ArrayList<Node>> mPlayerNodes;
    private static HashMap<Player, HashMap<Node, Integer>> mPlayerDistancesToEnemy;
    private static HashMap<Player, HashMap<Node, Node>> mPlayerGatewaysToEnemy;

    /**
     * This method sets up the AI Awareness of the game. Other methods should only be called after call to initialize().
     *
     * @param gameState       the current GameState object
     * @param computerPlayers the current computer players
     */
    public static void initialize(GameState gameState, ArrayList<Player> computerPlayers) {
        mGameState = gameState;
        mComputerPlayers = computerPlayers;
        mPlayerNodes = new HashMap<>();
        mPlayerDistancesToEnemy = new HashMap<>();
        mPlayerGatewaysToEnemy = new HashMap<>();
        recalculate();
        isInitialized = true;
    }

    /**
     * This method checks whether AI Awareness has already been initialized
     *
     * @return true if initialized, false if not
     */
    public static boolean isInitialized() {
        return isInitialized;
    }

    /**
     * This method updates the internals of AI Awareness. Should be called, when a node state has changed.
     */
    public static void update() {
        if (!isInitialized) {
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
     * This method checks whether the player has neighbor nodes that are closer to the enemy.
     * If yes, it returns the one with fewer total unit count.
     *
     * @param player the player whom the assessed node belongs to
     * @param node   the node to be assessed
     * @return the node towards the enemy with the smallest unit count, or null
     */
    public static Node getBackupTargetNode(Player player, Node node) {
        Node backupTarget = null;

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
     * This method returns a node's distance to the closest enemy node
     *
     * @param player the player whom the assessed node belongs to
     * @param node   the node to be assessed
     * @return integer indicating the distance to the closest enemy node
     */
    public static int getDistanceToEnemy(Player player, Node node) {
        if (!mPlayerDistancesToEnemy.get(player).containsKey(node)) {
            throw new IllegalArgumentException("I am not aware that I own this node!");
        }

        return mPlayerDistancesToEnemy.get(player).get(node);
    }

    /**
     * Returns a node that is connected to the assessed node and is closer to the next enemy node
     *
     * @param player the player whom the assessed node belongs to
     * @param node   the node to be assessed
     * @return a node connected to the assessed node which is closer to the next enemy node
     */
    public static Node getGatewayToEnemy(Player player, Node node) {
        if (!mPlayerGatewaysToEnemy.get(player).containsKey(node)) {
            throw new IllegalArgumentException("I am not aware that I own this node!");
        }

        return mPlayerGatewaysToEnemy.get(player).get(node);
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

    private static void prepareNodes(Player player) {
        ArrayList<Node> nodes = new ArrayList<>();
        // get all nodes controlled by player
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
        HashMap<Node, Integer> distancesToEnemy = new HashMap<>();
        HashMap<Node, Node> gatewaysToEnemy = new HashMap<>();

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

        Log.d("debug", "calculating bfs");

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

        Log.d("debug", "getting node towards enemy");

        Node currentNode = closestEnemyNode;
        boolean done = false;

        while (!done) {

            //Log.d("debug", "not yet done");

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
