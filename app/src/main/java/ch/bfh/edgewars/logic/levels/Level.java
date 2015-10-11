package ch.bfh.edgewars.logic.levels;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ch.bfh.edgewars.logic.entities.board.Edge;
import ch.bfh.edgewars.logic.entities.board.node.Node;


public class Level {

    @SerializedName("level_number")
    @Expose
    private int levelNumber;
    @SerializedName("nodes")
    @Expose
    private List<Node> nodes = new ArrayList<Node>();
    @SerializedName("edges")
    @Expose
    private List<Edge> edges = new ArrayList<Edge>();

    /**
     *
     * @return
     * The levelNumber
     */
    public int getLevelNumber() {
        return levelNumber;
    }

    /**
     *
     * @param levelNumber
     * The level_number
     */
    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    /**
     *
     * @return
     * The nodes
     */
    public List<Node> getNodes() {
        return nodes;
    }

    /**
     *
     * @param nodes
     * The nodes
     */
    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    /**
     *
     * @return
     * The edges
     */
    public List<Edge> getEdges() {
        return edges;
    }

    /**
     *
     * @param edges
     * The edges
     */
    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

}