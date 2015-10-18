package ch.sebastianhaeni.edgewars.logic;

import java.util.ArrayList;

import ch.sebastianhaeni.edgewars.logic.entities.Camera;
import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.Board;

/**
 * The game state contains everything we know about the game. This class allows other classes
 * to gather data for decisions. It's basically a bag.
 */
public class GameState {
    private final Camera mCamera;
    private final Board mBoard;
    private final ArrayList<Player> mPlayers;
    private final Player mHuman;

    /**
     * Constructor
     *
     * @param camera  camera entity
     * @param board   board entity
     * @param players list of players
     * @param human   the human player (also contained in <code>players</code>)
     */
    public GameState(Camera camera, Board board, ArrayList<Player> players, Player human) {
        mCamera = camera;
        mBoard = board;
        mPlayers = players;
        mHuman = human;
    }

    /**
     * @return gets the camera
     */
    public Camera getCamera() {
        return mCamera;
    }

    /**
     * @return gets the game board
     */
    public Board getBoard() {
        return mBoard;
    }

    /**
     * @return gets a list of players
     */
    public ArrayList<Player> getPlayers() {
        return mPlayers;
    }

    /**
     * @return gets the human player
     */
    public Player getHuman() {
        return mHuman;
    }
}
