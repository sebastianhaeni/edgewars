package ch.sebastianhaeni.edgewars.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import ch.sebastianhaeni.edgewars.R;
import ch.sebastianhaeni.edgewars.databinding.ActivityLevelSelectionBinding;
import ch.sebastianhaeni.edgewars.logic.LevelLoader;
import ch.sebastianhaeni.edgewars.model.LevelRecord;

public class LevelSelectionActivity extends Activity {

    private LevelRecord mLevelRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityLevelSelectionBinding binding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.activity_level_selection,
                null,
                false);
        binding.setActivity(this);

        LevelLoader levelLoader = new LevelLoader(this);
        for (int lvl : levelLoader.getLevelNumbers()) {
            findOrCreateLevelRecord(lvl);

            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;

            TextView lvlText = new TextView(this);
            lvlText.setId(lvl);
            lvlText.setText(String.valueOf(lvl));
            Typeface typeface = Typeface.createFromAsset(getAssets(), "DISTGRG.ttf");
            lvlText.setTypeface(typeface);
            lvlText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 200);
            lvlText.setWidth(width);
            lvlText.setHeight(height);
            lvlText.setGravity(Gravity.CENTER);
            lvlText.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    selectLevel(view);
                }
            });

            if (mLevelRecord.hasWon()) {
                lvlText.setTextColor(Color.GREEN);
            } else if (mLevelRecord.hasPlayed() && !mLevelRecord.hasWon()) {
                lvlText.setTextColor(Color.RED);
            }

            binding.layoutLevelButtons.addView(lvlText);
        }

        setContentView(binding.getRoot());
    }

    private void selectLevel(View view) {
        Intent intent = new Intent(LevelSelectionActivity.this, LevelDetailActivity.class);
        intent.putExtra(LevelDetailActivity.LEVEL_ID, view.getId());
        startActivity(intent);
    }

    private void findOrCreateLevelRecord(int lvl) {
        List<LevelRecord> records = LevelRecord.find(LevelRecord.class, "level_nr = ?", Integer.toString(lvl));
        if (records.size() > 0) {
            mLevelRecord = records.get(0);
        } else {
            LevelRecord record = new LevelRecord(lvl, 0, 0, 0, 0);
            record.save();
            mLevelRecord = record;
        }
    }

    public void back(View view) {
        finish();
    }
}
