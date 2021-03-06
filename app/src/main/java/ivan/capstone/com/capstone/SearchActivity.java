package ivan.capstone.com.capstone;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import ivan.capstone.com.capstone.DataObjects.Serie;


public class SearchActivity extends AppCompatActivity implements SearchFragment.Callback  {

    private Toolbar mToolbar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public boolean mTwoPane;
    private static final String DETAILFRAGMENT_TAG = "PFTAG";
    public static final String Name = "Search";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mToolbar = (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (findViewById(R.id.fragment_detail_serie) != null) {
            mTwoPane = true;

        } else {
            mTwoPane = false;
            //this will get rid of an unnecessary shadow below the action bar for smaller screen devices like phones.
            // Then the action bar and Today item will appear to be on the same plane (as opposed to two different planes,
            // where one casts a shadow on the other).
            getSupportActionBar().setElevation(0f);
        }
    }

    @Override
    public void onItemSelected(Serie value, ImageView imageView) {
        if (mTwoPane) {
            Bundle args = new Bundle();
            if (value != null) {
                args.putParcelable("Serie", value);
                args.putString("ActivityOrigin", Name);
            }
            DetailSerieFragment fragment = new DetailSerieFragment();
            fragment.setArguments(args);
            if (imageView != null) {
                getSupportFragmentManager().beginTransaction()
                        .addSharedElement(imageView, getResources().getString(R.string.transition_photo))
                        .replace(R.id.fragment_detail_serie, fragment, DETAILFRAGMENT_TAG)
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_detail_serie, fragment, DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            Intent intent = new Intent(this, DetailSerieSearchedActivity.class);
            intent.putExtra("Serie", value);
            intent.putExtra("ActivityOrigin", Name);
            if (imageView != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, imageView, getResources().getString(R.string.transition_photo));
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                }
            }else {
                startActivity(intent);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
