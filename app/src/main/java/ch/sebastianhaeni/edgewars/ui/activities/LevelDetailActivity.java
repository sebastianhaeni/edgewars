package ch.sebastianhaeni.edgewars.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ch.sebastianhaeni.edgewars.R;

public class LevelDetailActivity extends Activity {

    public static final String LEVEL_ID = "LEVEL_ID";
    private int mLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_detail);

        mLevel = getIntent().getExtras().getInt(LEVEL_ID);
    }

    public void startLevel(View view) {
        Intent intent = new Intent(LevelDetailActivity.this, GameActivity.class);
        intent.putExtra(LEVEL_ID, mLevel);
        startActivity(intent);
    }

    public void back(View view) {
        finish();
    }

    public int getLevel() {
        return mLevel;
    }
}

