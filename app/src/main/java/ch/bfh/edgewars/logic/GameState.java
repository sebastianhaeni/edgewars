package ch.bfh.edgewars.logic;

import java.util.ArrayList;

import ch.bfh.edgewars.logic.entities.Camera;
import ch.bfh.edgewars.logic.entities.Player;
import ch.bfh.edgewars.logic.entities.board.Board;

public class GameState {
    private final Player mHuman;
    private final ArrayList<Player> mPlayers;
    private final Camera mCamera;
    private final Board mBoard;

    public GameState(Camera camera, Board board, ArrayList<Player> players, Player human) {
        mCamera = camera;
        mBoard = board;
        mPlayers = players;
        mHuman = human;
    }

    public Camera getCamera() {
        return mCamera;
    }

    public void update(long millis) {
        mCamera.update(millis);
        mBoard.update(millis);

        for (Player p : mPlayers) {
            p.update(millis);
        }
    }

    public Board getBoard() {
        return mBoard;
    }
}
