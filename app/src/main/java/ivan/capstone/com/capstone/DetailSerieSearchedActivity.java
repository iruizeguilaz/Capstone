package ivan.capstone.com.capstone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


public class DetailSerieSearchedActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_serie);
        mToolbar = (Toolbar) findViewById(R.id.detail_toolbar);

        setSupportActionBar(mToolbar);
    }

}
