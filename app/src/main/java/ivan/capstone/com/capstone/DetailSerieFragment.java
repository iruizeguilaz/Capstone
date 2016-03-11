package ivan.capstone.com.capstone;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bluejamesbond.text.DocumentView;
import com.bluejamesbond.text.style.TextAlignment;


import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Picasso;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ivan.capstone.com.capstone.DataObjects.Serie;
import ivan.capstone.com.capstone.XML.XMLManager;


public class DetailSerieFragment extends Fragment implements View.OnClickListener {

    private static final String API_SERIES_KEY = BuildConfig.API_SERIES_KEY;

    boolean mTwoPane;
    Serie serie;
    TextView genre_text;
    TextView rating_text;
    TextView network_text;
    TextView releasedDate_text;
    ImageView poster;
    ImageView banner;
    ImageButton share_button;
    ImageButton save_button;
    TextView save_text;
    ImageButton unsave_button;
    TextView unsave_text;
    LinearLayout layout_detail;
    private String activityOrigin;
    boolean isRefreshing;
    ImageButton refresh_button;

    public DetailSerieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null && serie != null && !serie.getName().equals("")) {
            if (getActivity().getClass().getSimpleName().equals(MySeriesActivity.class.getSimpleName())
                    || getActivity().getClass().getSimpleName().equals(SearchActivity.class.getSimpleName())){
                actionBar.setSubtitle(serie.getName());
            }
            else actionBar.setTitle(serie.getName());
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().postponeEnterTransition();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_detail_serie, container, false);
        isRefreshing = false;
        genre_text = (TextView)rootView.findViewById(R.id.genre_serie);
        rating_text = (TextView)rootView.findViewById(R.id.rating_serie);
        network_text = (TextView)rootView.findViewById(R.id.network_serie);
        releasedDate_text = (TextView)rootView.findViewById(R.id.releasedDate_serie);
        poster = (ImageView) rootView.findViewById(R.id.poster);
        banner = (ImageView) rootView.findViewById(R.id.banner);
        layout_detail = (LinearLayout) rootView.findViewById(R.id.description_wrapper);
        share_button = (ImageButton)rootView.findViewById(R.id.share);
        share_button.setOnClickListener(this);
        save_button = (ImageButton)rootView.findViewById(R.id.iv_tick_off);
        save_button.setOnClickListener(this);
        save_text = (TextView)rootView.findViewById(R.id.save_serie);
        unsave_button = (ImageButton)rootView.findViewById(R.id.iv_tick_on);
        unsave_button.setOnClickListener(this);
        unsave_text = (TextView)rootView.findViewById(R.id.unsave_serie);
        refresh_button = (ImageButton)rootView.findViewById(R.id.refresh);
        refresh_button.setOnClickListener(this);

        if (savedInstanceState != null && savedInstanceState.getParcelable("Serie")!= null) {
            serie = savedInstanceState.getParcelable("Serie");
            LoadData();
        }
        else {
            Bundle arguments = getArguments();
            if (arguments != null) {
                serie = arguments.getParcelable("Serie");
                activityOrigin = arguments.getString("ActivityOrigin");
                mTwoPane = true;
            }else {
                Intent intent = getActivity().getIntent();
                serie = intent.getParcelableExtra("Serie");
                activityOrigin = intent.getStringExtra("ActivityOrigin");
                mTwoPane = false;
            }
            // if the sere has all the information we need, it is not neccesary to load it again from internet
            // the thing is we could come from the search activity, and there, the object serie has not all information
            // so even if we have it stored, we should get it again if we come from the search activity
            // if we come from my series activiy, we can realy on we have all information loaded on the sere object.
            if (activityOrigin!= null && activityOrigin.equals(MySeriesActivity.Name)) {
                LoadData();
            }else {
                // if we come from search activity, and we choose a serie that we have already stored.
                // we could avoid and internet call by get the information from the database
                if (serie != null && serie.IsSaved()) {
                    // if serie is saved, load the information of it from the database
                    serie.LoadData();
                    LoadData();
                } else {
                    FetchSerieByIDTask getSerie = new FetchSerieByIDTask();
                    getSerie.execute();
                }
            }
            // hit analytics
            sendViewSerie();

        }
        return rootView;
    }

    private void scheduleStartPostponedTransition(final View sharedElement) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                            try {
                                getActivity().startPostponedEnterTransition();
                            }catch (Exception e){
                                Log.e( "Error closing stream", e.toString());
                            }

                        }
                        return true;
                    }
                });
    }

    public void LoadData() {
        String date = serie.getDateReleased();
        if (date != null  && !date.equals("")) {
            String str[] = date.split("-");
            if (str != null && str.length > 0) releasedDate_text.setText(getResources().getString(R.string.year_serie)+ " " + str[0]);
        }
        addOverwiewView(new StringBuilder().append(serie.getOverView()));
        String genre = getResources().getString(R.string.genre_serie) + " " + serie.getGenre();
        genre_text.setText(genre);
        String rating = serie.getRating() + getResources().getString(R.string.rating_serie) + "  " + serie.getVotes()+ " "   + getResources().getString(R.string.votes_serie);
        rating_text.setText(rating);
        String network = getResources().getString(R.string.network_serie) + "  " + serie.getNetwork();
        network_text.setText(network);
        scheduleStartPostponedTransition(poster);
        if (serie.getPoster_url().equals("")) {
            Picasso.with(getActivity())
                    .load(R.drawable.old_tv)
                    .fit().centerCrop()
            .into(poster);
        }
        else {
            //poster.getLayoutParams().height = poster.getWidth() * 4 / 3;
            //poster.requestLayout();
            Picasso.with(getActivity())
                    .load(serie.getPoster_url())
                    .fit().centerCrop()
                    .into(poster);
        }
        if (!serie.getImage_url().equals("")) {
            Picasso.with(getActivity())
                    .load(serie.getImage_url())
                    .fit().centerCrop()
                    .into(banner);
        }
        if (serie.IsSaved()) {
            save_button.setVisibility(View.GONE);
            save_text.setVisibility(View.GONE);
            unsave_button.setVisibility(View.VISIBLE);
            unsave_text.setVisibility(View.VISIBLE);
        }
    }

    // Custom view which is justified
    private void addOverwiewView(CharSequence article) {
        final DocumentView documentView = new DocumentView(getActivity(), DocumentView.PLAIN_TEXT);
        documentView.getDocumentLayoutParams().setTextColor(Color.BLACK);
        documentView.getDocumentLayoutParams().setTextTypeface(Typeface.DEFAULT);
        documentView.getDocumentLayoutParams().setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        documentView.getDocumentLayoutParams().setTextAlignment(TextAlignment.JUSTIFIED);
        float margin = getResources().getDimension(R.dimen.medium_margin);
        float margin_calculated = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, margin, getResources().getDisplayMetrics());
        documentView.getDocumentLayoutParams().setInsetPaddingLeft(margin_calculated);
        documentView.getDocumentLayoutParams().setInsetPaddingRight(margin_calculated);
        documentView.getDocumentLayoutParams().setInsetPaddingBottom(margin_calculated);
        documentView.getDocumentLayoutParams().setLineHeightMultiplier(2f);
        documentView.getDocumentLayoutParams().setReverse(true);
        documentView.setText(article);
        layout_detail.addView(documentView);
    }

    @Override
    public void onClick(View view) {
        ImageButton button = (ImageButton)view;
        //share button
        if (button.getId() == R.id.share) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String title = getResources().getString(R.string.check_serie) + serie.getName();
            String shareBody = "https://trakt.tv/search/tvdb/" + serie.getId() + "?id_type=show";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, title + " " + shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }
        if (button.getId() == R.id.iv_tick_off) {
            save_button.setVisibility(View.GONE);
            save_text.setVisibility(View.GONE);
            unsave_button.setVisibility(View.VISIBLE);
            unsave_text.setVisibility(View.VISIBLE);
            if (serie != null && !serie.getId().equals("")) {
                serie.Save();
                sendSaveSerie(); //Analytics
            }
        }
        if (button.getId() == R.id.iv_tick_on) {
            unsave_button.setVisibility(View.GONE);
            unsave_text.setVisibility(View.GONE);
            save_button.setVisibility(View.VISIBLE);
            save_text.setVisibility(View.VISIBLE);
            if (serie != null && !serie.getId().equals("")) {
                serie.Delete();
                sendDeleteSerie(); //Analytics
            }
        }
        if (button.getId() == R.id.refresh) {
            if (serie != null && !serie.getId().equals("")) {
                isRefreshing = true;
                FetchSerieByIDTask getSerie = new FetchSerieByIDTask();
                getSerie.execute();
            }
        }



    }

    public class FetchSerieByIDTask extends AsyncTask<String, Void,  Serie> {

        private final String LOG_TAG = FetchSerieByIDTask.class.getSimpleName();

        @Override
        protected  Serie doInBackground(String... params) {
            try {
                Serie result = null;
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                try {
                    final String SERIES_BASE_URL = "http://thetvdb.com/api/" + API_SERIES_KEY + "/series/" + serie.getId();
                    Uri builtUri = Uri.parse(SERIES_BASE_URL).buildUpon()
                            .build();
                    URL url = new URL(builtUri.toString());
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();
                    InputStream inputStream = urlConnection.getInputStream();
                    if (inputStream == null) {
                        return null;
                    }
                    XmlPullParserFactory pullParserFactory = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = pullParserFactory.newPullParser();
                    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    parser.setInput(inputStream, null);
                    result = XMLManager.GetSerieFromXML(parser);
                } catch ( Exception e){
                    Log.e(LOG_TAG, "Error:" + e.getMessage());
                    result = null;
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (final IOException e) {
                            Log.e(LOG_TAG, "Error closing stream", e);
                            result = null;
                        }
                    }
                }
                return result;
            }
            catch (Exception ex)
            {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Serie result) {
            if (result != null) {
                serie = result;
                if (isRefreshing){
                    serie.Update();
                    isRefreshing = false;
                }
            }else {
                if (!MyApplication.isNetworkAvailable()) {
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(MyApplication.getContext(),MyApplication.getContext().getString(R.string.nonetwork_message) , duration).show();
                } else {
                    // the serie must exist so if we do not find it it is because the server is down or because our connection
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(MyApplication.getContext(),MyApplication.getContext().getString(R.string.noserver_message) , duration).show();
                }
            }
            LoadData();
        }
    }


    private void sendViewSerie(){

        Tracker tracker = ((MyApplication)getActivity().getApplication()).getTracker();
        tracker.setScreenName("DetailSerieFragment");
         tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Serie")
                .setAction("View")
                .setLabel(serie.getName())
                .build());
    }

    private void sendSaveSerie(){

        Tracker tracker = ((MyApplication)getActivity().getApplication()).getTracker();
        tracker.setScreenName("DetailSerieFragment");
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Serie")
                .setAction("Save")
                .setLabel(serie.getName())
                .build());

    }

    private void sendDeleteSerie(){

        Tracker tracker = ((MyApplication)getActivity().getApplication()).getTracker();
        tracker.setScreenName("DetailSerieFragment");
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Serie")
                .setAction("Delete")
                .setLabel(serie.getName())
                .build());

    }
}
