package ch.sebastianhaeni.edgewars.logic;

import java.io.IOException;
import java.util.ArrayList;

import ch.sebastianhaeni.edgewars.logic.ai.RuleBasedAI;
import ch.sebastianhaeni.edgewars.logic.entities.Camera;
import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.Board;
import ch.sebastianhaeni.edgewars.logic.entities.board.Edge;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.logic.levels.Level;

/**
 * Created by rlaubscher on 13/11/15.
 */
public class GameStateRestorer {

    public GameState restore(int levelNr) throws RuntimeException {

//        Level level;
//
//        Board board = new Board();
//        Camera camera = new Camera();
//
//        ArrayList<Node> nodes = level.getNodes();
//        ArrayList<Edge> edges = level.getEdges();
//
//        // first add edges to board, so that they are drawn under nodes
//        for (Edge edge : edges) {
//            board.addEntity(edge);
//        }
//
//        // then add the nodes
//        for (Node node : nodes) {
//            board.addEntity(node);
//        }
//
//        // get human player
//        Player humanPlayer = level.getHumanPlayers().get(0);
//
//        // get all players of level and add to array list
//        ArrayList<Player> allPlayers = new ArrayList<>();
//        ArrayList<Player> computerPlayers = level.getComputerPlayers();
//        allPlayers.addAll(computerPlayers);
//        allPlayers.add(humanPlayer);
//
//        GameState state = new GameState(camera, board, allPlayers, humanPlayer);
//
//        // add AI to computer players
//        for (Player computerPlayer : computerPlayers) {
//            computerPlayer.setAI(new RuleBasedAI(state, computerPlayer));
//        }
//
//        return state;
        return null;
    }
}
