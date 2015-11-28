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

    // position constants to define outer node positions for getOuterNode() method
    public final static int TOP = 0;
    public final static int RIGHT = 1;
    public final static int BOTTOM = 2;
    public final static int LEFT = 3;
    private final static transient HashMap<Integer, Node> outerNodes = new HashMap<>();

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
    public Node getOuterNode(int position) {
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

    private Node getTopMostNode() {
        if (outerNodes.containsKey(TOP))
            return outerNodes.get(TOP);

        Node theNode = getNodes().get(0);
        for (Node node : getNodes()) {
            if (node.getPosition().getY() < theNode.getPosition().getY())
                theNode = node;
        }

        outerNodes.put(TOP, theNode);
        return theNode;
    }

    private Node getBottomMostNode() {
        if (outerNodes.containsKey(BOTTOM))
            return outerNodes.get(BOTTOM);

        Node theNode = getNodes().get(0);
        for (Node node : getNodes()) {
            if (node.getPosition().getY() > theNode.getPosition().getY())
                theNode = node;
        }

        outerNodes.put(BOTTOM, theNode);
        return theNode;
    }

    private Node getRightMostNode() {
        if (outerNodes.containsKey(RIGHT))
            return outerNodes.get(RIGHT);

        Node theNode = getNodes().get(0);
        for (Node node : getNodes()) {
            if (node.getPosition().getX() > theNode.getPosition().getX())
                theNode = node;
        }

        outerNodes.put(RIGHT, theNode);
        return theNode;
    }

    private Node getLeftMostNode() {
        if (outerNodes.containsKey(LEFT))
            return outerNodes.get(LEFT);

        Node theNode = getNodes().get(0);
        for (Node node : getNodes()) {
            if (node.getPosition().getX() < theNode.getPosition().getX())
                theNode = node;
        }

        outerNodes.put(LEFT, theNode);
        return theNode;
    }
}
