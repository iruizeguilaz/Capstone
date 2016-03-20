package ivan.capstone.com.capstone;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ivan.capstone.com.capstone.Adapter.SeriesAdapter;
import ivan.capstone.com.capstone.Data.SeriesContract;
import ivan.capstone.com.capstone.DataObjects.Serie;

public class MySeriesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, SeriesAdapter.OnItemClickListener {

  // where params (


    private SeriesAdapter seriesAdapter;
    List<Serie> series;
    RecyclerView recyclerView;
    boolean widgetSource = false;
    AdView mAdView;
    Serie serie;
    boolean isFirstLoad;

    private static final int SERIES_LOADER = 0;

    private static final String[] SERIES_COLUMNS = {
            SeriesContract.SeriesEntry._ID,
            SeriesContract.SeriesEntry.COLUMN_ID,
            SeriesContract.SeriesEntry.COLUMN_NAME,
            SeriesContract.SeriesEntry.COLUMN_RATING,
            SeriesContract.SeriesEntry.COLUMN_VOTES,
            SeriesContract.SeriesEntry.COLUMN_BANNER_URL,
            SeriesContract.SeriesEntry.COLUMN_POSTER_URL,
            SeriesContract.SeriesEntry.COLUMN_REALSED_DATE,
            SeriesContract.SeriesEntry.COLUMN_OVERVIEW,
            SeriesContract.SeriesEntry.COLUMN_GENRE,
            SeriesContract.SeriesEntry.COLUMN_NETWORK,
            SeriesContract.SeriesEntry.COLUMN_MODIFYDATE,
            SeriesContract.SeriesEntry.COLUMN_STATUS,
            SeriesContract.SeriesEntry.COLUMN_TYPE
    };
    static final int COL__ID = 0;
    static final int COL_ID = 1;
    static final int COL_NAME = 2;
    static final int COL_RATING = 3;
    static final int COL_VOTES = 4;
    static final int COL_BANNER = 5;
    static final int COL_POSTER = 6;
    static final int COL_RELEASED_DATE = 7;
    static final int COL_OVERVIEW = 8;
    static final int COL_GENRE = 9;
    static final int COL_NETWORK = 10;
    static final int COL_MODIFYDATE= 11;
    static final int COL_STATUS = 12;
    static final int COL_TYPE= 13;

    public MySeriesFragment() {
        // Required empty public constructor
    }

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        void onItemSelected(Serie value, ImageView imageView);
    }

    @Override
    public void onClick(SeriesAdapter.ViewHolder viewHolder, int position, ImageView imageView) {
        ((Callback) getActivity()).onItemSelected(series.get(position), imageView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_series, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.myseries_recycler);
        recyclerView.setNestedScrollingEnabled(false);
        widgetSource = false;
        if (savedInstanceState != null && savedInstanceState.getParcelableArrayList("ListSeries") != null) {
            series = savedInstanceState.getParcelableArrayList("ListSeries");
            seriesAdapter = new SeriesAdapter(series, this, R.layout.item_list_myseries);
            recyclerView.setAdapter(seriesAdapter);
            seriesAdapter.notifyDataSetChanged();
            getLoaderManager().restartLoader(SERIES_LOADER, null, this);
        } else {

            isFirstLoad = true;
            // check if we come from the widget (to load the serie, even if we come from widget, we have to load
            // from the database if we are in a tablet mode (because we show both framents, list and detai)
            Intent intent = getActivity().getIntent();
            if (intent!= null) {
                serie = intent.getParcelableExtra("Serie");
                intent.removeExtra("Serie");
                if (serie != null) {
                    sendWidgetSerie(serie.getName()); //analytics, if come from widget
                    widgetSource = true;

                }
            }
            series= new ArrayList<Serie>();
            seriesAdapter = new SeriesAdapter(series, this, R.layout.item_list_myseries);
            recyclerView.setAdapter(seriesAdapter);
            getLoaderManager().initLoader(SERIES_LOADER, null, this);
        }
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyApplication.getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });
        mAdView = (AdView) rootView.findViewById(R.id.adView);
        return rootView;
    }

    public class LoadBannerRunnable implements Runnable {

        @Override
        public void run() {
            AdRequest adRequest = new AdRequest.Builder()
                    //.addTestDevice(deviceId)
                    .build();
            mAdView.loadAd(adRequest);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putParcelableArrayList("ListSeries", new ArrayList<Serie>(series));
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (!((MySeriesActivity)getActivity()).mTwoPane) {
            if(getActivity().getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT){
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            }
        }
        LoadBannerRunnable loadView = new LoadBannerRunnable();
        loadView.run();
        if (widgetSource)((Callback) getActivity()).onItemSelected(serie, null);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            switch (((MySeriesActivity)getActivity()).type_list_serie){
                case  Serie.FOLLOWING:
                    actionBar.setTitle( getActivity().getResources().getString(R.string.serie_following_serie));
                    break;
                case  Serie.VIEWED:
                    actionBar.setTitle( getActivity().getResources().getString(R.string.serie_vieweds_serie));
                    break;
                case  Serie.PENDING:
                    actionBar.setTitle( getActivity().getResources().getString(R.string.serie_pending_serie));
                    break;
                default:
                    actionBar.setTitle( getActivity().getResources().getString(R.string.app_name));
                    break;
            }
        }
        super.onActivityCreated(savedInstanceState);
    }

    public void refresh(){
        getLoaderManager().restartLoader(SERIES_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // series are ordered first if the are not viewed and name, and aterwards those which have been viewed
        String sortOrder =  SeriesContract.SeriesEntry.COLUMN_NAME + " ASC";
        String where = SeriesContract.SeriesEntry.COLUMN_TYPE + " = ? ";
        Uri serieUri = SeriesContract.SeriesEntry.CONTENT_URI;
        return new CursorLoader(getActivity(),
                serieUri,
                SERIES_COLUMNS,
                where ,
                new String[]{String.valueOf((((MySeriesActivity)getActivity()).type_list_serie))},
                sortOrder);
    }




    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        series.clear();
        while (data.moveToNext()) {
            Serie mySerie = new Serie();
            mySerie.set_id(data.getLong(COL__ID));
            mySerie.setId(data.getString(COL_ID));
            mySerie.setName(data.getString(COL_NAME));
            mySerie.setRating(data.getString(COL_RATING));
            mySerie.setVotes(data.getString(COL_VOTES));
            mySerie.setImage_url(data.getString(COL_BANNER));
            mySerie.setPoster_url(data.getString(COL_POSTER));
            mySerie.setDateReleased(data.getString(COL_RELEASED_DATE));
            mySerie.setOverView(data.getString(COL_OVERVIEW));
            mySerie.setGenre(data.getString(COL_GENRE));
            mySerie.setNetwork(data.getString(COL_NETWORK));
            mySerie.setModify_date(new Date(data.getLong(COL_MODIFYDATE)*1000));
            mySerie.setStatus(data.getString(COL_STATUS));
            mySerie.setType(data.getInt(COL_TYPE));
            series.add((mySerie));
        }
        seriesAdapter.notifyDataSetChanged();
        // load the firs item on the rigt side if we have a tablet version parent detail
        if (((MySeriesActivity)getActivity()).mTwoPane && series.size() > 0 && !widgetSource) {
            // I only want to do the first time because I want to avoid realad the detailfragment if
            // the user unsave the serie... because the serie would dissaper, on the rigth side
            // and it could be an error of the user... so I prefer to mantein it... that is why I only
            // do this on the first time.
            if (isFirstLoad) {
                ((Callback) getActivity()).onItemSelected(series.get(0), null);
                isFirstLoad = false;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        series.clear();
        seriesAdapter.notifyDataSetChanged();
    }

    //analytics
    public void sendWidgetSerie(String name){
        Tracker tracker = ((MyApplication)getActivity().getApplication()).getTracker();
        tracker.setScreenName("SearchFragment");
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Widget")
                .setAction("View")
                .setLabel(name)
                .build());
    }
}
