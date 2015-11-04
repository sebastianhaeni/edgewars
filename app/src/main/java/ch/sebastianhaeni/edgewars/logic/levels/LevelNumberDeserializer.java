package ch.sebastianhaeni.edgewars.logic.levels;


import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

public class LevelNumberDeserializer implements JsonDeserializer {

    @Override
    public ArrayList<Integer> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        ArrayList<Integer> levelNumbers = new ArrayList<>();

        JsonObject levelsObject = json.getAsJsonObject();
        JsonArray levelArray = levelsObject.get("levels").getAsJsonArray();

        // iterate through all levels of json file and retrieve level numbers
        for (int i = 0; i < levelArray.size(); i++) {
            JsonObject levelObject = levelArray.get(i).getAsJsonObject();
            int levelNumber = levelObject.get("level_number").getAsInt();
            levelNumbers.add(levelNumber);
        }

        Collections.sort(levelNumbers);

        return levelNumbers;

    }

}
