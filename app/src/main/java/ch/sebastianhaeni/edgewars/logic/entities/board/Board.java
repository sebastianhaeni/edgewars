package ch.sebastianhaeni.edgewars.logic.entities.board;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import ch.sebastianhaeni.edgewars.logic.entities.Entity;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;

/**
 * The board is the controller of everything visible on the game board. It has board entities and
 * drawables.
 */
public class Board extends Entity implements Serializable {

    // enum to define outer node positions for getOuterNode() method
    public enum NodePosition {
        TOP,
        RIGHT,
        BOTTOM,
        LEFT
    }

    private final static transient HashMap<NodePosition, Node> outerNodes = new HashMap<>();

    private final transient ArrayList<BoardEntity> mEntities = new ArrayList<>();

    /**
     * Constructor
     */
    public Board() {
        super();
    }

    /**
     * Adds a board entity to the board.
     *
     * @param e new board entity
     */
    public void addEntity(BoardEntity e) {
        mEntities.add(e);
    }

    /**
     * @return gets the nodes on the board
     */
    public ArrayList<Node> getNodes() {
        ArrayList<Node> nodes = new ArrayList<>();
        for (BoardEntity e : mEntities) {
            if (e instanceof Node) {
                nodes.add((Node) e);
            }
        }
        return nodes;
    }

    /**
     * This function returns one of the four outer nodes specified by the parameter position
     *
     * @param position specifies which node to return
     * @return gets one of the four outermost nodes on the board
     */
    public Node getOuterNode(NodePosition position) {
        switch (position) {
            case TOP:
                return getTopMostNode();

            case RIGHT:
                return getRightMostNode();

            case BOTTOM:
                return getBottomMostNode();

            case LEFT:
                return getLeftMostNode();

            default:
                throw new IllegalArgumentException("I do not know this position!");
        }
    }

    @Override
    public void update(long millis) {
        // no op
    }

    /**
     * Initializes the board with the game.
     */
    public void initialize() {
        for (BoardEntity entity : mEntities) {
            entity.register();
        }
    }

    /**
     * @return gets list of board entities
     */
    public ArrayList<BoardEntity> getEntities() {
        return mEntities;
    }

    /**
     * @return the Node that is positioned the most at the top (Y axis) among all Nodes of the current game board
     */
    private Node getTopMostNode() {
        if (outerNodes.containsKey(NodePosition.TOP))
            return outerNodes.get(NodePosition.TOP);

        Node theNode = getNodes().get(0);
        for (Node node : getNodes()) {
            if (node.getPosition().getY() < theNode.getPosition().getY())
                theNode = node;
        }

        outerNodes.put(NodePosition.TOP, theNode);
        return theNode;
    }

    /**
     * @return the Node that is positioned the most at the bottom (Y axis) among all Nodes of the current game board
     */
    private Node getBottomMostNode() {
        if (outerNodes.containsKey(NodePosition.BOTTOM))
            return outerNodes.get(NodePosition.BOTTOM);

        Node theNode = getNodes().get(0);
        for (Node node : getNodes()) {
            if (node.getPosition().getY() > theNode.getPosition().getY())
                theNode = node;
        }

        outerNodes.put(NodePosition.BOTTOM, theNode);
        return theNode;
    }

    /**
     * @return the Node that is positioned the most at right (X axis) among all Nodes of the current game board
     */
    private Node getRightMostNode() {
        if (outerNodes.containsKey(NodePosition.RIGHT))
            return outerNodes.get(NodePosition.RIGHT);

        Node theNode = getNodes().get(0);
        for (Node node : getNodes()) {
            if (node.getPosition().getX() > theNode.getPosition().getX())
                theNode = node;
        }

        outerNodes.put(NodePosition.RIGHT, theNode);
        return theNode;
    }

    /**
     * @return the Node that is positioned the most at left (X axis) among all Nodes of the current game board
     */
    private Node getLeftMostNode() {
        if (outerNodes.containsKey(NodePosition.LEFT))
            return outerNodes.get(NodePosition.LEFT);

        Node theNode = getNodes().get(0);
        for (Node node : getNodes()) {
            if (node.getPosition().getX() < theNode.getPosition().getX())
                theNode = node;
        }

        outerNodes.put(NodePosition.LEFT, theNode);
        return theNode;
    }
}
