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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.List;

import ivan.capstone.com.capstone.Adapter.ActorsAdapter;
import ivan.capstone.com.capstone.Adapter.EpisodesAdapter;
import ivan.capstone.com.capstone.DataObjects.Episode;
import ivan.capstone.com.capstone.DataObjects.Serie;
import ivan.capstone.com.capstone.XML.XMLManager;


public class DetailSerieFragment extends Fragment implements View.OnClickListener {

    private static final String API_SERIES_KEY = BuildConfig.API_SERIES_KEY;
    int season = 0;
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

    EpisodesAdapter episodesAdapter;
    RecyclerView episodeRecyclerView;
    TextView empty_Episodes;

    ActorsAdapter actorsAdapter;
    RecyclerView actorRecyclerView;
    TextView empty_Actors;

    ImageButton previous_season;
    ImageButton previous_season_disable;
    ImageButton next_season;
    ImageButton next_season_disable;
    TextView actual_season;

    ImageButton no_viewed_season;
    ImageButton viewed_season;

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
        episodeRecyclerView = (RecyclerView) rootView.findViewById(R.id.episodes_recycler);
        empty_Episodes = (TextView)rootView.findViewById(R.id.empty_episodes);
        actorRecyclerView = (RecyclerView) rootView.findViewById(R.id.actors_recycler);
        empty_Actors = (TextView)rootView.findViewById(R.id.empty_actors);
        next_season = (ImageButton)rootView.findViewById(R.id.next_season);
        next_season.setOnClickListener(this);
        next_season_disable = (ImageButton)rootView.findViewById(R.id.next_season_disable);
        previous_season = (ImageButton)rootView.findViewById(R.id.previous_season);
        previous_season.setOnClickListener(this);
        previous_season_disable = (ImageButton)rootView.findViewById(R.id.previous_season_disable);
        actual_season= (TextView)rootView.findViewById(R.id.text_episode_number);
        no_viewed_season = (ImageButton)rootView.findViewById(R.id.no_viewed_season);
        no_viewed_season.setOnClickListener(this);
        viewed_season= (ImageButton)rootView.findViewById(R.id.viewed_season);
        viewed_season.setOnClickListener(this);

        // si es movil
        if (getActivity().getClass().getSimpleName().equals(DetailSerieSearchedActivity.class.getSimpleName())){
            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            episodeRecyclerView.setLayoutManager(layoutManager);

            LinearLayoutManager layoutManager2
                   = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

            actorRecyclerView.setLayoutManager(layoutManager2);
        }


        if (savedInstanceState != null && savedInstanceState.getParcelable("Serie")!= null) {
            serie = savedInstanceState.getParcelable("Serie");
            season = savedInstanceState.getInt("Season");
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

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putParcelable("Serie", serie);
        outState.putInt("Season", season);
        super.onSaveInstanceState(outState);
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
        String rating;
        if (serie.getVotes().equals("0")){
            rating = serie.getVotes()+ " "   + getResources().getString(R.string.votes_serie);
        } else {
            rating = serie.getRating() + getResources().getString(R.string.rating_serie) + "  " + serie.getVotes()+ " "   + getResources().getString(R.string.votes_serie);
        }
        rating_text.setText(rating);
        String network = getResources().getString(R.string.network_serie) + "  " + serie.getNetwork();
        network_text.setText(network);
        scheduleStartPostponedTransition(poster);
        if (serie.getPoster_url().equals("")) {
            Picasso.with(getActivity())
                    .load(R.drawable.no_image)
                    .fit().centerCrop()
            .into(poster);
        }
        else {
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
        }else {
            Picasso.with(getActivity())
                    .load(R.drawable.no_image)
                    .fit().centerCrop()
                    .into(banner);
        }
        if (serie.IsSaved()) {
            save_button.setVisibility(View.GONE);
            save_text.setVisibility(View.GONE);
            unsave_button.setVisibility(View.VISIBLE);
            unsave_text.setVisibility(View.VISIBLE);
        }

        // LOAD Episodes
        // we are going to the first episode without been viewed to get the season

        season = serie.GetSeasonFistEpisodeNotViewed();
        List<Episode> episodeList = serie.getSeason(season);
        episodesAdapter = new EpisodesAdapter(episodeList, R.layout.item_list_episodes);
        episodeRecyclerView.setAdapter(episodesAdapter);
        episodesAdapter.notifyDataSetChanged();
        if (serie.getEpisodes().size() > 0){
            episodeRecyclerView.setVisibility(View.VISIBLE);
            empty_Episodes.setVisibility(View.GONE);
            actual_season.setVisibility(View.VISIBLE);
            actual_season.setText(season + "/" + serie.getSeasonsCount() + " " + getResources().getString(R.string.season_series));
            if (season < serie.getSeasonsCount()) {
                next_season.setVisibility(View.VISIBLE);
                next_season_disable.setVisibility(View.GONE);
            }
            else {
                next_season.setVisibility(View.GONE);
                next_season_disable.setVisibility(View.VISIBLE);
            }
            if (season > 1){
                previous_season.setVisibility(View.VISIBLE);
                previous_season_disable.setVisibility(View.GONE);
            }else {
                previous_season.setVisibility(View.GONE);
                previous_season_disable.setVisibility(View.VISIBLE);
            }
            if (serie.IsSaved()){
                boolean seasonViewd = true;
                for (Episode episode : episodeList) {
                    if (episode.getViewed() == 0)
                        seasonViewd = false;
                }
                if (seasonViewd) {
                    viewed_season.setVisibility(View.VISIBLE);
                    no_viewed_season.setVisibility(View.GONE);
                } else{
                    viewed_season.setVisibility(View.GONE);
                    no_viewed_season.setVisibility(View.VISIBLE);
                }
            }else {
                viewed_season.setVisibility(View.GONE);
                no_viewed_season.setVisibility(View.GONE);
            }

        } else {
            episodeRecyclerView.setVisibility(View.GONE);
            empty_Episodes.setVisibility(View.VISIBLE);
            previous_season.setVisibility(View.GONE);
            previous_season_disable.setVisibility(View.GONE);
            next_season.setVisibility(View.GONE);
            next_season_disable.setVisibility(View.GONE);
            actual_season.setVisibility(View.GONE);
            viewed_season.setVisibility(View.GONE);
            no_viewed_season.setVisibility(View.GONE);
        }

        // LOAD Actors
        actorsAdapter = new ActorsAdapter(serie.getActors(), R.layout.item_list_actors);
        actorRecyclerView.setAdapter(actorsAdapter);
        actorsAdapter.notifyDataSetChanged();

        if (serie.getActors().size() > 0){
            actorRecyclerView.setVisibility(View.VISIBLE);
            empty_Actors.setVisibility(View.GONE);
        } else {
            actorRecyclerView.setVisibility(View.GONE);
            empty_Actors.setVisibility(View.VISIBLE);
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
            viewed_season.setVisibility(View.VISIBLE);
            no_viewed_season.setVisibility(View.GONE);
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
            viewed_season.setVisibility(View.GONE);
            no_viewed_season.setVisibility(View.GONE);
        }
        if (button.getId() == R.id.refresh) {
            if (serie != null && !serie.getId().equals("")) {
                isRefreshing = true;
                FetchSerieByIDTask getSerie = new FetchSerieByIDTask();
                getSerie.execute();
            }
        }

        if (button.getId() == R.id.next_season) {
            season = season + 1;
            List<Episode> episodeList = serie.getSeason(season);
            episodesAdapter = new EpisodesAdapter(episodeList, R.layout.item_list_episodes);
            episodeRecyclerView.setAdapter(episodesAdapter);
            episodesAdapter.notifyDataSetChanged();
            if (season == serie.getSeasonsCount()) {
                next_season.setVisibility(View.GONE);
                next_season_disable.setVisibility(View.VISIBLE);
            }
            previous_season.setVisibility(View.VISIBLE);
            previous_season_disable.setVisibility(View.GONE);
            actual_season.setText(season + "/" + serie.getSeasonsCount() + " " + getResources().getString(R.string.season_series));
            if (serie.IsSaved()){
                boolean seasonViewd = true;
                for (Episode episode : episodeList) {
                    if (episode.getViewed() == 0)
                        seasonViewd = false;
                }
                if (seasonViewd) {
                    viewed_season.setVisibility(View.VISIBLE);
                    no_viewed_season.setVisibility(View.GONE);
                } else{
                    viewed_season.setVisibility(View.GONE);
                    no_viewed_season.setVisibility(View.VISIBLE);
                }

            }else {
                viewed_season.setVisibility(View.GONE);
                no_viewed_season.setVisibility(View.GONE);
            }
        }
        if (button.getId() == R.id.previous_season) {
            season = season - 1;
            List<Episode> episodeList = serie.getSeason(season);
            episodesAdapter = new EpisodesAdapter(episodeList, R.layout.item_list_episodes);
            episodeRecyclerView.setAdapter(episodesAdapter);
            episodesAdapter.notifyDataSetChanged();
            if (season == 1) {
                previous_season.setVisibility(View.GONE);
                previous_season_disable.setVisibility(View.VISIBLE);
            }
            next_season.setVisibility(View.VISIBLE);
            next_season_disable.setVisibility(View.GONE);
            actual_season.setText(season + "/" + serie.getSeasonsCount() + " " + getResources().getString(R.string.season_series));
            if (serie.IsSaved()){
                boolean seasonViewd = true;
                for (Episode episode : episodeList) {
                    if (episode.getViewed() == 0)
                        seasonViewd = false;
                }
                if (seasonViewd) {
                    viewed_season.setVisibility(View.VISIBLE);
                    no_viewed_season.setVisibility(View.GONE);
                } else{
                    viewed_season.setVisibility(View.GONE);
                    no_viewed_season.setVisibility(View.VISIBLE);
                }

            }else {
                viewed_season.setVisibility(View.GONE);
                no_viewed_season.setVisibility(View.GONE);
            }

        }
        if (button.getId() == R.id.viewed_season){
            List<Episode> episodeList = serie.getSeason(season);
            for (Episode episode : episodeList) {
                episode.setViewed(0);
                episode.UpdateViewed();
            }
            episodesAdapter = new EpisodesAdapter(episodeList, R.layout.item_list_episodes);
            episodeRecyclerView.setAdapter(episodesAdapter);
            episodesAdapter.notifyDataSetChanged();
            viewed_season.setVisibility(View.GONE);
            no_viewed_season.setVisibility(View.VISIBLE);
        }
        if (button.getId() == R.id.no_viewed_season){
            List<Episode> episodeList = serie.getSeason(season);
            for (Episode episode : episodeList) {
                if (episode.getViewed() == 0){
                    episode.setViewed(1);
                    episode.UpdateViewed();
                }
            }
            episodesAdapter = new EpisodesAdapter(episodeList, R.layout.item_list_episodes);
            episodeRecyclerView.setAdapter(episodesAdapter);
            episodesAdapter.notifyDataSetChanged();
            viewed_season.setVisibility(View.VISIBLE);
            no_viewed_season.setVisibility(View.GONE);
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
                    final String SERIES_BASE_URL = "http://thetvdb.com/api/" + API_SERIES_KEY + "/series/" + serie.getId() + "/all";
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
                    result.setEpisodes(XMLManager.GetEpisodesFromXML(parser));

                    // bring the actors
                    final String ACTORS_BASE_URL = "http://thetvdb.com/api/" + API_SERIES_KEY + "/series/" + serie.getId() + "/actors.xml";
                    builtUri = Uri.parse(ACTORS_BASE_URL).buildUpon()
                            .build();
                    url = new URL(builtUri.toString());
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();
                    inputStream = urlConnection.getInputStream();
                    if (inputStream == null) {
                        return null;
                    }
                    pullParserFactory = XmlPullParserFactory.newInstance();
                    parser = pullParserFactory.newPullParser();
                    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    parser.setInput(inputStream, null);
                    result.setActors(XMLManager.GetActorsFromXML(parser, serie.getId()));
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
                    serie.Update(true);
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
