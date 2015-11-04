package ch.sebastianhaeni.edgewars.ui.activities;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import ch.sebastianhaeni.edgewars.R;
import ch.sebastianhaeni.edgewars.databinding.ActivityAboutBinding;

public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityAboutBinding binding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.activity_about,
                null,
                false);
        binding.setActivity(this);
        setContentView(binding.getRoot());

        Typeface typeface = Typeface.createFromAsset(getAssets(), "DISTGRG.ttf");
        TextView titleAbout = (TextView) findViewById(R.id.titleAbout);
        titleAbout.setTypeface(typeface);
    }

    public void back(View view) {
        finish();
    }

}
