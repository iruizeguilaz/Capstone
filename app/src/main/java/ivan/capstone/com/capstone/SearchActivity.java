package ivan.capstone.com.capstone;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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


        setSupportActionBar(mToolbar);
        //getSupportLoaderManager().initLoader(0, null, this);
        /*SearchFragment fragment = (SearchFragment) getFragmentManager().findFragmentById(R.id.fragment_search);

        if (fragment == null) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fragment_search, new SearchFragment());
            fragmentTransaction.commit();
        }
        if (getFragmentManager().findFragmentByTag("test_fragment") == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_search, new SearchFragment(), "test_fragment")
                    .commit();
        }*/


    }



}
