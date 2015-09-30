package ch.bfh.edgewars.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import ch.bfh.edgewars.R;

public class LevelDetailActivity extends Activity {

    public static final String LEVEL_ID = "LEVEL_ID";

    @Bind(R.id.textLevel)
    TextView mTextLevel;

    @Bind(R.id.buttonStartLevel)
    Button mButtonStartLevel;

    @Bind(R.id.buttonLevelDetailToSelection)
    Button mButtonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_detail);

        ButterKnife.bind(this);

        final int level = getIntent().getExtras().getInt(LEVEL_ID);

        mTextLevel.setText("Level " + level);

        mButtonStartLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LevelDetailActivity.this, GameActivity.class);
                intent.putExtra(LEVEL_ID, level);
                startActivity(intent);
            }
        });

        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}

