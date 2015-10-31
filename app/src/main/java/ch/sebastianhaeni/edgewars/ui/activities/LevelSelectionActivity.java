package ch.sebastianhaeni.edgewars.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ch.sebastianhaeni.edgewars.R;
import ch.sebastianhaeni.edgewars.databinding.ActivityLevelSelectionBinding;
import ch.sebastianhaeni.edgewars.logic.LevelLoader;

public class LevelSelectionActivity extends Activity {

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

            Button lvlButton = new Button(this);
            lvlButton.setId(lvl);
            lvlButton.setText("Level " + lvl);
            lvlButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    selectLevel(view);
                }
            });

            binding.layoutLevelButtons.addView(lvlButton);
        }

        setContentView(binding.getRoot());
    }

    public void selectLevel(View view) {
        Intent intent = new Intent(LevelSelectionActivity.this, LevelDetailActivity.class);
        intent.putExtra(LevelDetailActivity.LEVEL_ID, view.getId());
        startActivity(intent);
    }

    public void back(View view) {
        finish();
    }
}
