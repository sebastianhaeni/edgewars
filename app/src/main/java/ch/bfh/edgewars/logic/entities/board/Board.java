package ch.bfh.edgewars.logic.entities.board;

import java.util.ArrayList;

import ch.bfh.edgewars.graphics.shapes.Shape;
import ch.bfh.edgewars.logic.entities.Entity;
import ch.bfh.edgewars.logic.entities.board.node.Node;

public class Board extends Entity {
    private ArrayList<BoardEntity> mEntities = new ArrayList<>();
    private ArrayList<Shape> mShapes = new ArrayList<>();

    public Board() {
        super();
    }

    public void addEntity(BoardEntity e) {
        mEntities.add(e);
        recalculateShape();
    }

    public void recalculateShape() {
        mShapes.clear();
        for (BoardEntity e : mEntities) {
            for (Shape s : e.getShapes()) {
                mShapes.add(s);
            }
        }
    }

    public ArrayList<Shape> getShapes() {
        return mShapes;
    }

    public ArrayList<Node> getNodes() {
        ArrayList<Node> nodes = new ArrayList<>();
        for (BoardEntity e : mEntities) {
            if (e instanceof Node) {
                nodes.add((Node) e);
            }
        }
        return nodes;
    }

    @Override
    public void update(long millis) {
        // no op
    }
}
