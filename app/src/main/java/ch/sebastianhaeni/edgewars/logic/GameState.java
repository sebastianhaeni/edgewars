package ch.sebastianhaeni.edgewars.logic;

import java.io.Serializable;
import java.util.ArrayList;

import ch.sebastianhaeni.edgewars.logic.ai.AIAwareness;
import ch.sebastianhaeni.edgewars.logic.entities.Camera;
import ch.sebastianhaeni.edgewars.logic.entities.Entity;
import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.Board;

/**
 * The game state contains everything we know about the game. This class allows other classes
 * to gather data for decisions. It's basically a bag.
 */
public class GameState implements Serializable {
    private final Camera mCamera;
    private final Board mBoard;
    private final ArrayList<Player> mPlayers;

    private boolean gameIsRunning = false;

    /**
     * Constructor
     *
     * @param camera  camera entity
     * @param board   board entity
     * @param players list of players
     */
    public GameState(Camera camera, Board board, ArrayList<Player> players) {
        mCamera = camera;
        mBoard = board;
        mPlayers = players;

        Game.getInstance().setGameState(this);

        mBoard.initialize();

        ArrayList<Player> computerPlayers = new ArrayList<>();
        for (Player player : mPlayers) {
            if (!player.isHuman()) {
                computerPlayers.add(player);
            }
        }
        AIAwareness.initialize(this, computerPlayers);
    }

    /**
     * This method reports to GameState whether the game is running or not
     *
     * @param isRunning true or false, depending if game is running or not
     */
    public void setGameIsRunning(boolean isRunning) {
        gameIsRunning = isRunning;
    }

    /**
     * @return true if game is running, false if not
     */
    public boolean gameIsRunning() {
        return gameIsRunning;
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
     * Initialize the state and register all entities.
     */
    public void init() {
        mCamera.register();
        for (Entity e : mBoard.getEntities()) {
            e.register();
        }
        for (Player p : mPlayers) {
            p.register();
        }
    }
}
