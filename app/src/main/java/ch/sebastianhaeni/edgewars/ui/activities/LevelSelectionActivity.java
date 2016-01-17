package ch.sebastianhaeni.edgewars.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

            binding.layoutLevelButtons.addView(lvlText);
        }

        setContentView(binding.getRoot());
    }

    private void selectLevel(View view) {
        Intent intent = new Intent(LevelSelectionActivity.this, LevelDetailActivity.class);
        intent.putExtra(LevelDetailActivity.LEVEL_ID, view.getId());
        startActivity(intent);
    }

    public void back(View view) {
        finish();
    }
}
