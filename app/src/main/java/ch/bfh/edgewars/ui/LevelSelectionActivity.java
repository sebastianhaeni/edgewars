package ch.bfh.edgewars.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import ch.bfh.edgewars.R;

public class LevelSelectionActivity extends Activity {

    @Bind(R.id.buttonLevelSelectionToMain)
    Button mButtonBack;

    @Bind(R.id.buttonLevel1)
    Button mButtonLevel1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_selection);

        ButterKnife.bind(this);

        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mButtonLevel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LevelSelectionActivity.this, LevelDetailActivity.class);
                intent.putExtra(LevelDetailActivity.LEVEL_ID, 1);
                startActivity(intent);
            }
        });
    }

}
