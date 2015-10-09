package ch.bfh.edgewars.logic.entities.board;

import java.util.ArrayList;

import ch.bfh.edgewars.graphics.shapes.Shape;
import ch.bfh.edgewars.logic.entities.Entity;

public abstract class BoardEntity extends Entity {

    public BoardEntity(long interval) {
        super(interval);
    }

    public BoardEntity() {
        super();
    }

    public abstract ArrayList<Shape> getShapes();
}
