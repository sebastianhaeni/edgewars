package ch.bfh.edgewars.logic;

import java.util.ArrayList;

import ch.bfh.edgewars.logic.entities.Camera;
import ch.bfh.edgewars.logic.entities.Player;
import ch.bfh.edgewars.logic.entities.board.Board;

public class GameState {
    private final Camera mCamera;
    private final Board mBoard;
    private final ArrayList<Player> mPlayers;
    private final Player mHuman;

    public GameState(Camera camera, Board board, ArrayList<Player> players, Player human) {
        mCamera = camera;
        mBoard = board;
        mPlayers = players;
        mHuman = human;
    }

    public Camera getCamera() {
        return mCamera;
    }

    public Board getBoard() {
        return mBoard;
    }

    public ArrayList<Player> getPlayers() {
        return mPlayers;
    }

    public Player getHuman() {
        return mHuman;
    }
}
