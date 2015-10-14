package ch.sebastianhaeni.edgewars.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import ch.sebastianhaeni.edgewars.R;
import ch.sebastianhaeni.edgewars.databinding.ActivityMainBinding;

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
    }

    public void start(View view) {
        startActivity(new Intent(MainActivity.this, LevelSelectionActivity.class));
    }

    public void showAbout(View view) {
        startActivity(new Intent(MainActivity.this, AboutActivity.class));
    }

}
