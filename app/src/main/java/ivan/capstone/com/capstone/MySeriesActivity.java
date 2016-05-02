package ivan.capstone.com.capstone;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;


import ivan.capstone.com.capstone.DataObjects.Serie;
import ivan.capstone.com.capstone.Sync.SyncAdapter;

public class MySeriesActivity extends AppCompatActivity implements MySeriesFragment.Callback{

    public boolean mTwoPane;
    private static final String LISTFRAGMENT_TAG = "LFTAG";
    private static final String DETAILFRAGMENT_TAG = "PFTAG";
    public static final String Name = "MySeries";


    // there are 3 types of series:  pending, following, viewed.
    public int type_list_serie = Serie.FOLLOWING;

    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myseries);

        Toolbar  mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        if (findViewById(R.id.fragment_detail_serie) != null) {
            mTwoPane = true;
        } else {
            mTwoPane = false;
            //this will get rid of an unnecessary shadow below the action bar for smaller screen devices like phones.
            // Then the action bar and Today item will appear to be on the same plane (as opposed to two different planes,
            // where one casts a shadow on the other).
            getSupportActionBar().setElevation(0f);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        if (savedInstanceState != null){
            type_list_serie = savedInstanceState.getInt("Type_list_serie");

        }else {
            // initialite by default the viewing page if it is not coming from savedInstence
            navigationView.setCheckedItem(R.id.nav_home_viewing);
            navigationView.getMenu().performIdentifierAction(R.id.nav_home_viewing, 0);
        }
// here fails
        SyncAdapter.initializeSyncAdapter(this);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        MySeriesFragment fragment;
                        Fragment detailFragment = getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);
                        Fragment parentFragment = getSupportFragmentManager().findFragmentByTag(LISTFRAGMENT_TAG);
                        switch (menuItem.getItemId()) {
                            case R.id.nav_home_saved:
                                if(detailFragment != null)
                                    getSupportFragmentManager().beginTransaction().remove(detailFragment).commit();
                                if(parentFragment != null)
                                    getSupportFragmentManager().beginTransaction().remove(parentFragment).commit();
                                type_list_serie = Serie.PENDING;
                                fragment = new MySeriesFragment();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_my_series, fragment, LISTFRAGMENT_TAG)
                                        .commit();
                                break;
                            case R.id.nav_home_viewed:
                                if(detailFragment != null)
                                    getSupportFragmentManager().beginTransaction().remove(detailFragment).commit();
                                if(parentFragment != null)
                                    getSupportFragmentManager().beginTransaction().remove(parentFragment).commit();
                                type_list_serie = Serie.VIEWED;
                                fragment = new MySeriesFragment();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_my_series, fragment, LISTFRAGMENT_TAG)
                                        .commit();
                                break;
                            case R.id.nav_home_viewing:
                                if(detailFragment != null)
                                    getSupportFragmentManager().beginTransaction().remove(detailFragment).commit();
                                if(parentFragment != null)
                                    getSupportFragmentManager().beginTransaction().remove(parentFragment).commit();
                                type_list_serie = Serie.FOLLOWING;
                                fragment = new MySeriesFragment();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_my_series, fragment, LISTFRAGMENT_TAG)
                                        .commit();
                                break;
                        }
                        return true;
                    }
                });
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
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
                        .commitAllowingStateLoss();

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
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putInt("Type_list_serie", type_list_serie);
        super.onSaveInstanceState(outState);
    }

}
