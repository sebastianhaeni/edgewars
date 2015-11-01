package ch.sebastianhaeni.edgewars.logic.levels;

import java.util.ArrayList;
import java.util.Collections;

public class Levels {

    private ArrayList<Level> mLevels = new ArrayList<>();

    /**
     * @return gets levels
     */
    public ArrayList<Level> getLevels() {
        return mLevels;
    }

    /**
     * @param mLevels levels
     */
    public void setLevels(ArrayList<Level> mLevels) {
        this.mLevels = mLevels;
    }

    public ArrayList<Integer> getAllLevelNumbers() {
        ArrayList<Integer> levelNumbers = new ArrayList<>();
        ArrayList<Level> levels = this.getLevels();

        // add level number of each level to array list
        for (int i = 0; i < levels.size(); i++) {
            levelNumbers.add(levels.get(i).getLevelNumber());
        }

        // sort numbers in ascending order
        Collections.sort(levelNumbers);

        return levelNumbers;
    }

}
