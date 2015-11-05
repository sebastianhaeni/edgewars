package ch.sebastianhaeni.edgewars.logic.levels;

import java.util.ArrayList;

import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.Edge;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;

public class Level {

    private ArrayList<Player> mHumanPlayers = new ArrayList<>();
    private ArrayList<Player> mComputerPlayers = new ArrayList<>();
    private ArrayList<Node> mNodes = new ArrayList<>();
    private ArrayList<Edge> mEdges = new ArrayList<>();

    public ArrayList<Player> getHumanPlayers() {
        return mHumanPlayers;
    }

    public void setHumanPlayers(ArrayList<Player> humanPlayers) {
        this.mHumanPlayers = humanPlayers;
    }

    public ArrayList<Player> getComputerPlayers() {
        return mComputerPlayers;
    }

    public void setComputerPlayers(ArrayList<Player> computerPlayers) {
        this.mComputerPlayers = computerPlayers;
    }

    /**
     * @return The nodes
     */
    public ArrayList<Node> getNodes() {
        return mNodes;
    }

    /**
     * @param nodes The nodes
     */
    public void setNodes(ArrayList<Node> nodes) {
        this.mNodes = nodes;
    }

    /**
     * @return The edges
     */
    public ArrayList<Edge> getEdges() {
        return mEdges;
    }

    /**
     * @param edges The edges
     */
    public void setEdges(ArrayList<Edge> edges) {
        this.mEdges = edges;
    }

}
