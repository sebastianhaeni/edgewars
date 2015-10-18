package ch.sebastianhaeni.edgewars.model;

import com.orm.SugarRecord;

import java.sql.Time;

public class LevelRecord extends SugarRecord<LevelRecord> {
    private int id;
    private String json_file;
    private int score;
    private Time time;
    private boolean unlocked;
    private boolean started;
    private boolean finished;
    private int attempts;

    public LevelRecord() {
    }

}
