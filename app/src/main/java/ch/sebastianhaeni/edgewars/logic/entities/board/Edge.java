package ch.sebastianhaeni.edgewars.logic.entities.board;

import java.util.ArrayList;

import ch.sebastianhaeni.edgewars.graphics.shapes.IDrawable;
import ch.sebastianhaeni.edgewars.graphics.shapes.Line;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;

/**
 * An edge connects two nodes and has no functionality.
 */
public class Edge extends BoardEntity {
    private final Node mNode1;
    private final Node mNode2;
    private double mLength;
    private ArrayList<IDrawable> mDrawables = new ArrayList<>();

    /**
     * Constructor
     *
     * @param node1 start edge
     * @param node2 target edge
     */
    public Edge(Node node1, Node node2) {
        mNode1 = node1;
        mNode2 = node2;

        calculateLength();

        mDrawables.clear();
        mDrawables.add(new Line(node1.getPosition(), node2.getPosition()));
    }

    /**
     * @return gets the length of this edge
     */
    public double getLength() {
        return mLength;
    }

    /**
     * Calculates the length of the edge with Pythagoras.
     */
    private void calculateLength() {
        float x1 = mNode1.getPosition().getX();
        float y1 = mNode1.getPosition().getY();

        float x2 = mNode2.getPosition().getX();
        float y2 = mNode2.getPosition().getY();

        mLength = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    @Override
    public ArrayList<IDrawable> getDrawables() {
        return mDrawables;
    }

    @Override
    public void update(long millis) {
        // no op
    }
}
