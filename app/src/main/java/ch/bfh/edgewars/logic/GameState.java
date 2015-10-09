package ch.bfh.edgewars.logic;

import ch.bfh.edgewars.logic.entities.Camera;
import ch.bfh.edgewars.logic.entities.Player;
import ch.bfh.edgewars.logic.entities.board.Board;

public class GameState {
    private final Camera mCamera;
    private final Board mBoard;
    private final Player mHuman;

    public GameState(Camera camera, Board board, Player human) {
        mCamera = camera;
        mBoard = board;
        mHuman = human;
    }

    public Camera getCamera() {
        return mCamera;
    }

    public Board getBoard() {
        return mBoard;
    }

    public Player getHuman() {
        return mHuman;
    }
}
