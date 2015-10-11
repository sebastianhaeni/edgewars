package ch.bfh.edgewars.logic.levels;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.bfh.edgewars.logic.entities.Player;
import ch.bfh.edgewars.logic.entities.board.Edge;
import ch.bfh.edgewars.logic.entities.board.node.Node;
import ch.bfh.edgewars.logic.entities.board.node.state.NeutralState;
import ch.bfh.edgewars.logic.entities.board.node.state.NodeState;
import ch.bfh.edgewars.logic.entities.board.node.state.OwnedState;
import ch.bfh.edgewars.util.Colors;
import ch.bfh.edgewars.util.Position;


public class LevelsDeserializer implements JsonDeserializer {

    private Levels levels;
    private List<Level> levelList;
    private Map nodeMap;

    private Player human, computer;

    @Override
    public Levels deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        human = new Player(Colors.BLUE);
        computer = new Player(Colors.RED);

        levels = new Levels();
        levelList = new ArrayList<Level>();
        nodeMap = new HashMap();

        JsonObject jsonObject = json.getAsJsonObject();
        JsonArray levelsArray = jsonObject.get("levels").getAsJsonArray();


        for (int i=0; i<levelsArray.size(); i++) {
            JsonObject levelObject = levelsArray.get(i).getAsJsonObject();

            Level level = new Level();
            int levelNumber = levelObject.get("level_number").getAsInt();
            level.setLevelNumber(levelNumber);

            JsonArray nodesArray = levelObject.get("nodes").getAsJsonArray();
            List<Node> nodesList =  this.createNodes(nodesArray);
            level.setNodes(nodesList);

            JsonArray edgesArray = levelObject.get("edges").getAsJsonArray();
            List<Edge> edgesList = createEdges(edgesArray);
            level.setEdges(edgesList);

            levelList.add(level);
        }

        levels.setLevels(levelList);
        return levels;
    }

    private List<Node> createNodes (JsonArray nodesArray) {

        List<Node> levelNodesList = new ArrayList<Node>();

        for (int i=0; i<nodesArray.size(); i++) {
            JsonObject nodeObject = nodesArray.get(i).getAsJsonObject();

            Node node = new Node();

            // store to hash map (in order to match with edges)
            int nodeId = nodeObject.get("node_id").getAsInt();
            nodeMap.put(nodeId, node);

            // set position of Node
            JsonObject position = nodeObject.get("position").getAsJsonObject();
            int coordinateX = position.get("coordinateX").getAsInt();
            int coordinateY = position.get("coordinateY").getAsInt();
            node.setPosition(new Position(coordinateX, coordinateY));

            // set state of Node
            String stateString = nodeObject.get("state").getAsString();
            NodeState state;
            switch (stateString) {
                case "ownedByPlayer":
                    state = new OwnedState(node, human);
                    break;
                case "ownedByEnemy":
                    state = new OwnedState(node, computer);
                    break;
                default:    // neutral
                    state = new NeutralState(node);
                    break;
            }
            node.setState(state);

            levelNodesList.add(node);
        }

        return levelNodesList;
    }

    private List<Edge> createEdges (JsonArray edgesArray) {

        List<Edge> levelEdgesList = new ArrayList<Edge>();

        for (int i=0; i<edgesArray.size(); i++) {
            JsonObject edgeObject = edgesArray.get(i).getAsJsonObject();

            Edge edge = new Edge();

            int node1_id = edgeObject.get("node1_id").getAsInt();
            Node node1 = (Node) nodeMap.get(node1_id);

            int node2_id = edgeObject.get("node2_id").getAsInt();
            Node node2 = (Node) nodeMap.get(node2_id);

            edge.setNodes(node1, node2);

            levelEdgesList.add(edge);
        }

        return levelEdgesList;
    }
}
