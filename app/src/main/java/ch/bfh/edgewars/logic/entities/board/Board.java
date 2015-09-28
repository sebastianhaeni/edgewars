package ch.bfh.edgewars.logic.entities.board;

import java.util.ArrayList;

import ch.bfh.edgewars.graphics.shapes.Shape;
import ch.bfh.edgewars.logic.entities.Entity;

public class Board extends Entity {
    private ArrayList<BoardEntity> mEntities = new ArrayList<>();
    private ArrayList<Shape> mShapes = new ArrayList<>();

    @Override
    public void update(long millis) {
        for (Entity e : mEntities) {
            e.update(millis);
        }
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
}
