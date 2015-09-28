package ch.bfh.edgewars.logic;

import java.util.ArrayList;

import ch.bfh.edgewars.logic.entities.Camera;
import ch.bfh.edgewars.logic.entities.Player;
import ch.bfh.edgewars.logic.entities.board.Board;
import ch.bfh.edgewars.logic.entities.board.Node;
import ch.bfh.edgewars.util.Position;

public class LevelCreator {
    public GameState build() {
        Camera camera = new Camera();
        Board board = new Board();

        ArrayList<Player> players = new ArrayList<>();

        Player human = new Player();
        Player computer = new Player();

        board.addEntity(new Node(human, new Position(0, 0)));

        players.add(human);
        players.add(computer);

        return new GameState(camera, board, players, human);
    }
}
