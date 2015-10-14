package ch.sebastianhaeni.edgewars.logic.entities.board;

import java.util.ArrayList;

import ch.sebastianhaeni.edgewars.graphics.shapes.IDrawable;
import ch.sebastianhaeni.edgewars.logic.entities.Entity;

public abstract class BoardEntity extends Entity {

    public BoardEntity(long interval) {
        super(interval);
    }

    public BoardEntity() {
        super();
    }

    public abstract ArrayList<IDrawable> getDrawables();
}
