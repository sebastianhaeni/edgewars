package ch.bfh.edgewars.logic.levels;

import java.util.ArrayList;
import java.util.List;

import ch.bfh.edgewars.logic.entities.Player;
import ch.bfh.edgewars.logic.entities.board.Edge;
import ch.bfh.edgewars.logic.entities.board.node.Node;


public class Level {

    private int mLevelNumber;
    private List<Player> mPlayers = new ArrayList<>();
    private List<Node> mNodes = new ArrayList<>();
    private List<Edge> mEdges = new ArrayList<>();

    /**
     *
     * @return
     * The mLevelNumber
     */
    public int getLevelNumber() {
        return mLevelNumber;
    }

    /**
     *
     * @param levelNumber
     * The level_number
     */
    public void setLevelNumber(int levelNumber) {
        this.mLevelNumber = levelNumber;
    }

    public List<Player> getPlayers () {
        return mPlayers;
    }

    public void setPlayers (List<Player> players) {
        this.mPlayers = players;
    }

    /**
     *
     * @return
     * The mNodes
     */
    public List<Node> getNodes() {
        return mNodes;
    }

    /**
     *
     * @param nodes
     * The mNodes
     */
    public void setNodes(List<Node> nodes) {
        this.mNodes = nodes;
    }

    /**
     *
     * @return
     * The mEdges
     */
    public List<Edge> getEdges() {
        return mEdges;
    }

    /**
     *
     * @param edges
     * The mEdges
     */
    public void setmEdges(List<Edge> edges) {
        this.mEdges = edges;
    }

}