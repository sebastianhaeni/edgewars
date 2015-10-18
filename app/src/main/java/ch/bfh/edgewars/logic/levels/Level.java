package ch.bfh.edgewars.logic.levels;

import java.util.ArrayList;

import ch.bfh.edgewars.logic.entities.Player;
import ch.bfh.edgewars.logic.entities.board.Edge;
import ch.bfh.edgewars.logic.entities.board.node.Node;


public class Level {

    private int mLevelNumber;
    private ArrayList<Player> mPlayers = new ArrayList<>();
    private ArrayList<Node> mNodes = new ArrayList<>();
    private ArrayList<Edge> mEdges = new ArrayList<>();

    /**
     * @return The number of the level
     */
    public int getLevelNumber() {
        return mLevelNumber;
    }

    /**
     * @param levelNumber The number of the level
     */
    public void setLevelNumber(int levelNumber) {
        this.mLevelNumber = levelNumber;
    }

    public ArrayList<Player> getPlayers() {
        return mPlayers;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.mPlayers = players;
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
    public void setmEdges(ArrayList<Edge> edges) {
        this.mEdges = edges;
    }

}