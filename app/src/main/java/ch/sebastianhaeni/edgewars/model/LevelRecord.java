package ch.sebastianhaeni.edgewars.model;

import com.orm.SugarRecord;

/**
 * Used for persisting the statistics of a Level
 */
public class LevelRecord extends SugarRecord<LevelRecord> {
    int levelNr;
    int score;
    long startTime;
    long endTime;
    int won;

    public LevelRecord() {
    }

    public LevelRecord(int level, int score, long startTime, long endTime, int won) {
        this.levelNr = level;
        this.score = score;
        this.startTime = startTime;
        this.endTime = endTime;
        this.won = won;
    }

    public long getTime() { return endTime - startTime; }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(long endTime) { this.endTime = endTime; }

    public boolean hasWon() { return won == 1; }

    public boolean hasPlayed() { return getTime() > 0; }

    public void setWon(boolean won) {
        if (won)
            this.won = 1;
        else
            this.won = 0;
    }
}
