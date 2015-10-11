package ch.bfh.edgewars.logic.level_deserialization;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Levels {

    @SerializedName("levels")
    @Expose
    private List<Level> levels = new ArrayList<Level>();

    /**
     *
     * @return
     * The levels
     */
    public List<Level> getLevels() {
        return levels;
    }

    /**
     *
     * @param levels
     * The levels
     */
    public void setLevels(List<Level> levels) {
        this.levels = levels;
    }

}