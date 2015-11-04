package ch.sebastianhaeni.edgewars.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import ch.sebastianhaeni.edgewars.R;
import ch.sebastianhaeni.edgewars.databinding.ActivityMainBinding;
import ch.sebastianhaeni.edgewars.logic.SoundEngine;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.activity_main,
                null,
                false);
        binding.setActivity(this);
        setContentView(binding.getRoot());

        Typeface typeface = Typeface.createFromAsset(getAssets(), "DISTGRG.ttf");
        TextView appName = (TextView) findViewById(R.id.appName);
        appName.setTypeface(typeface);
    }

    public void start(View view) {
        // initialize sound engine in this thread because we don't want to block the main thread
        SoundEngine.getInstance();
        startActivity(new Intent(MainActivity.this, LevelSelectionActivity.class));
    }

    public void showAbout(View view) {
        startActivity(new Intent(MainActivity.this, AboutActivity.class));
    }

}
