package ch.sebastianhaeni.edgewars.logic.entities.board;

import java.util.ArrayList;

import ch.sebastianhaeni.edgewars.graphics.shapes.Line;
import ch.sebastianhaeni.edgewars.graphics.shapes.Shape;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;

public class Edge extends BoardEntity {
    private Node mNode1;
    private Node mNode2;
    private double mLength;
    private ArrayList<Shape> mShapes = new ArrayList<>();

    public Edge(Node node1, Node node2) {
        super(); // an edge does not update
        mNode1 = node1;
        mNode2 = node2;

        calculateLength();

        mShapes.add(new Line(node1.getPosition(), node2.getPosition()));
    }

    public double getLength() {
        return mLength;
    }

    private void calculateLength() {
        float x1 = mNode1.getPosition().getX();
        float y1 = mNode1.getPosition().getY();

        float x2 = mNode2.getPosition().getX();
        float y2 = mNode2.getPosition().getY();

        mLength = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    @Override
    public ArrayList<Shape> getShapes() {
        return mShapes;
    }

    @Override
    public void update(long millis) {
        // no op
    }
}