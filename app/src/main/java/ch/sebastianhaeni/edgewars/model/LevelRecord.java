package ch.sebastianhaeni.edgewars.model;

import com.orm.SugarRecord;

import java.sql.Time;

public class LevelRecord extends SugarRecord<LevelRecord> {
    int levelNr;
    int score;
    long time; // in milliseconds

    public LevelRecord() {
    }

    public LevelRecord(int level, int score, long time) {
        this.levelNr = level;
        this.score = score;
        this.time = time;
    }

    public int getScore() { return score; }
    public long getTime() { return time; }

}
