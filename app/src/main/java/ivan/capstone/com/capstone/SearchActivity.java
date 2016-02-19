package ivan.capstone.com.capstone;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;


public class SearchActivity extends AppCompatActivity  {

    private Toolbar mToolbar;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mToolbar = (Toolbar) findViewById(R.id.search_toolbar);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Set refresh to false so the refresh icon doesn't just spin indefinitely
                // This is just a placeholder.
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        setSupportActionBar(mToolbar);
        //getSupportLoaderManager().initLoader(0, null, this);

        if (savedInstanceState == null) {
            refresh();
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_search, new SearchFragment())
                .commit();
    }

    private void refresh() {
        //startService(new Intent(this, UpdaterService.class));
        // TODO relanzar la lista

    }


}
