package ch.bfh.edgewars.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import ch.bfh.edgewars.R;

public class WinActivity extends Activity {

    @Bind(R.id.buttonWinToLevelDetail)
    Button mButtonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        ButterKnife.bind(this);

        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WinActivity.this, LevelDetailActivity.class);
                intent.putExtra(LevelDetailActivity.LEVEL_ID, getIntent().getExtras().getInt(LevelDetailActivity.LEVEL_ID));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

}
