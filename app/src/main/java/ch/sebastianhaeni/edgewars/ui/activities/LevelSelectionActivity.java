package ch.sebastianhaeni.edgewars.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ch.sebastianhaeni.edgewars.R;

public class LevelSelectionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_selection);

    }

    public void selectLevel(View view) {
        Intent intent = new Intent(LevelSelectionActivity.this, LevelDetailActivity.class);
        intent.putExtra(LevelDetailActivity.LEVEL_ID, 1);
        startActivity(intent);
    }

    public void back(View view) {
        finish();
    }
}
