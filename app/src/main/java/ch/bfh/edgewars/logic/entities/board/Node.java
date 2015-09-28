package ch.bfh.edgewars.logic.entities.board;

import java.util.ArrayList;

import ch.bfh.edgewars.graphics.shapes.Circle;
import ch.bfh.edgewars.graphics.shapes.Shape;
import ch.bfh.edgewars.logic.entities.Player;
import ch.bfh.edgewars.logic.entities.board.units.Unit;
import ch.bfh.edgewars.util.Position;

public class Node extends BoardEntity {
    private Player mOwner;
    private ArrayList<Unit> mUnits = new ArrayList<>();
    private int mActiveDefenseLevel = 1;
    private int mPassiveDefenseLevel = 1;
    private Position mPosition;
    private ArrayList<Shape> mShapes = new ArrayList<>();

    public Node(Player owner, Position position) {
        mOwner = owner;
        mPosition = position;

        mShapes.add(new Circle(mPosition));
    }

    public Position getPosition() {
        return mPosition;
    }


    @Override
    public ArrayList<Shape> getShapes() {
        return mShapes;
    }
}
