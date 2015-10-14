package ch.sebastianhaeni.edgewars.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ch.sebastianhaeni.edgewars.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void start(View view) {
        startActivity(new Intent(MainActivity.this, LevelSelectionActivity.class));

    }

    public void showAbout(View view) {
        startActivity(new Intent(MainActivity.this, AboutActivity.class));

    }

}
