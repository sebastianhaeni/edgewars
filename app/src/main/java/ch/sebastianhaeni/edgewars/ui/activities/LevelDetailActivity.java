package ch.sebastianhaeni.edgewars.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import ch.sebastianhaeni.edgewars.R;
import ch.sebastianhaeni.edgewars.databinding.ActivityLevelDetailBinding;
import ch.sebastianhaeni.edgewars.model.LevelRecord;

public class LevelDetailActivity extends Activity {

    public static final String LEVEL_ID = "LEVEL_ID";
    private int mLevelNr;
    private LevelRecord mLevelRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLevelNr = getIntent().getExtras().getInt(LEVEL_ID);

        findOrCreateLevelRecord();

        ActivityLevelDetailBinding binding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.activity_level_detail,
                null,
                false);
        binding.setActivity(this);
        setContentView(binding.getRoot());
    }

    public void startLevel(View view) {
        Intent intent = new Intent(LevelDetailActivity.this, GameActivity.class);
        intent.putExtra(LEVEL_ID, mLevelNr);
        startActivity(intent);
    }

    public void back(View view) {
        finish();
    }

    private void findOrCreateLevelRecord() {
        List<LevelRecord> records = LevelRecord.find(LevelRecord.class, "level_nr = ?", Integer.toString(mLevelNr));
        if (records.size() > 0) {
            mLevelRecord = records.get(0);
        }
        else {
            LevelRecord record = new LevelRecord(mLevelNr, 0, 0);
            record.save();
            mLevelRecord = record;
        }
    }

    public int getLevel() {
        return mLevelNr;
    }

    public int getScore() { return mLevelRecord.getScore(); }
    public String getTime () {
        long millis = mLevelRecord.getTime();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss", new Locale("de", "CH"));
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(millis);
    }
}

