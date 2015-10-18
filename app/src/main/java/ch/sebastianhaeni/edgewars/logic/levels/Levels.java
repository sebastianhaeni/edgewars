package ch.sebastianhaeni.edgewars.logic.levels;

import java.util.ArrayList;

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

}
