package ch.sebastianhaeni.edgewars.logic.ai;

import java.util.ArrayList;
import java.util.LinkedList;

import ch.sebastianhaeni.edgewars.logic.Game;
import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.Edge;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.OwnedState;

public class AIHelper {

    private static Player mPlayer;
    private static Node mNode;
    private static Node mClosestEnemyNode;
    private static Node mNodeTowardsEnemy;
    private static int mDistanceToEnemy;
    private static ArrayList<Node> mVisitedNodes;
    private static ArrayList<Edge> mDiscoveryEdges;


    public static int getDistanceToEnemy (Node node) {
        if (mNode == null || !node.equals(mNode)) {
            calculateDistanceToEnemy(node);
        }

        return mDistanceToEnemy;
    }

    public static Node getNodeTowardsEnemy (Node node) {
        if (mNode == null || !node.equals(mNode)) {
            calculateDistanceToEnemy(node);
        }

        return mNodeTowardsEnemy;
    }

    private static void calculateDistanceToEnemy (Node node) {

        if (! (node.getState() instanceof OwnedState)) {
            throw new IllegalArgumentException("This node is not owned!");
        }

        mPlayer = ((OwnedState) (node.getState())).getOwner();
        mNode = node;

        mVisitedNodes = new ArrayList<>();
        mDiscoveryEdges = new ArrayList<>();

        mDistanceToEnemy = bfs();
        mNodeTowardsEnemy = getNodeTowardsEnemy();

    }

    private static int bfs () {
        int distance = 0;
        Node startNode = mNode;

        ArrayList<LinkedList<Node>> queues = new ArrayList<>();
        queues.add(new LinkedList<Node>());
        queues.get(distance).addLast(startNode);

        mVisitedNodes.add(startNode);

        while (!queues.get(distance).isEmpty()) {

            queues.add(new LinkedList<Node>());

            for (Node node : queues.get(distance)) {

                for (Node neighborNode : Game.getInstance().getConnectedNodes(node)) {

                    if (!mVisitedNodes.contains(neighborNode)) {
                        mVisitedNodes.add(neighborNode);
                        queues.get(distance+1).add(neighborNode);
                        mDiscoveryEdges.add(Game.getInstance().getEdgeBetween(node, neighborNode));
                    }

                    if (neighborNode.getState() instanceof OwnedState) {
                        OwnedState state = (OwnedState) neighborNode.getState();
                        if (mPlayer.equals(state.getOwner())) {
                            mClosestEnemyNode = neighborNode;
                            return distance+1;
                        }
                    }
                }

            }

            distance++;

        }

        return -1; // no enemy node was found
    }

    private static Node getNodeTowardsEnemy() {

        Node currentNode = mClosestEnemyNode;
        boolean done = false;

        while (!done) {

            for (Edge edge : mDiscoveryEdges) {
                if (edge.getTargetNode().equals(currentNode)) {
                    if (edge.getSourceNode().equals(mNode)) {
                        done = true;
                    } else {
                        currentNode = edge.getSourceNode();
                        mDiscoveryEdges.remove(edge);
                    }
                    break;
                } else if (edge.getSourceNode().equals(currentNode)) {
                    if (edge.getTargetNode().equals(mNode)) {
                        done = true;
                    } else {
                        currentNode = edge.getTargetNode();
                        mDiscoveryEdges.remove(edge);
                    }
                    break;
                }
            }

        }

        return currentNode;
    }

}
