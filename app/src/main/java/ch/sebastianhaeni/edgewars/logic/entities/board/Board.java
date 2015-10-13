package ch.sebastianhaeni.edgewars.logic.entities.board;

import java.util.ArrayList;

import ch.sebastianhaeni.edgewars.graphics.shapes.IDrawable;
import ch.sebastianhaeni.edgewars.logic.entities.Entity;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;

public class Board extends Entity {
    private ArrayList<BoardEntity> mEntities = new ArrayList<>();
    private ArrayList<IDrawable> mDrawables = new ArrayList<>();

    public Board() {
        super();
    }

    public void addEntity(BoardEntity e) {
        mEntities.add(e);
    }

    public ArrayList<IDrawable> getDrawables() {
        mDrawables.clear();
        for (BoardEntity e : mEntities) {
            for (IDrawable s : e.getDrawables()) {
                mDrawables.add(s);
            }
        }
        return mDrawables;
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
