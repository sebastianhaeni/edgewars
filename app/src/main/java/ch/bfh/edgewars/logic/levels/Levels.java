package ch.bfh.edgewars.logic.levels;

import java.util.ArrayList;
import java.util.List;

public class Levels {

    private List<Level> levels = new ArrayList<>();

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