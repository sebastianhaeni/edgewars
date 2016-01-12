package ch.sebastianhaeni.edgewars.model;

import com.orm.SugarRecord;

public class LevelRecord extends SugarRecord<LevelRecord> {
    int levelNr;
    int score;
    long startTime;
    long endTime;

    public LevelRecord() {
    }

    public LevelRecord(int level, int score, long startTime, long endTime) {
        this.levelNr = level;
        this.score = score;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getScore() {
        return score;
    }

    public long getTime() { return endTime - startTime; }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    public void setEndTime(long endTime) { this.endTime = endTime; }
}
