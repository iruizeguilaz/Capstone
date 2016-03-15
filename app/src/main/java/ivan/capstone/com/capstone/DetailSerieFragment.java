package ivan.capstone.com.capstone;


import android.app.ProgressDialog;
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

    ImageButton no_viewed_serie;
    ImageButton viewed_serie;
    TextView status_serie;
    TextView viewed_serie_text;


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

        no_viewed_serie = (ImageButton)rootView.findViewById(R.id.no_viewed_serie);
        no_viewed_serie.setOnClickListener(this);
        viewed_serie = (ImageButton)rootView.findViewById(R.id.viewed_serie);
        viewed_serie.setOnClickListener(this);
        status_serie = (TextView)rootView.findViewById(R.id.status_serie);
        viewed_serie_text = (TextView)rootView.findViewById(R.id.viewed_serie_text);

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
                serie.LoadActors();
                serie.LoadEpisodes();
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
        String status;
        if (serie.getStatus().equals(serie.ENDED)) {
            status = getResources().getString(R.string.status)+ " " + getResources().getString(R.string.endend);
        } else {
            status = getResources().getString(R.string.status)+ " " +getResources().getString(R.string.continuing);
        }
        status_serie.setText(status);
        CheckViewedSerie();
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
        LoadEpisodes();


        // LOAD Actors
        LoadActors();

    }

    private void CheckViewedSerie(){
        // if it is saved and the serie has fully released
        if (serie.getStatus().equals(serie.ENDED) && serie.get_id() != 0) {
            viewed_serie_text.setVisibility(View.VISIBLE);
            if (serie.getViewed() == 1) {
                viewed_serie.setVisibility(View.VISIBLE);
                no_viewed_serie.setVisibility(View.GONE);
            } else{
                no_viewed_serie.setVisibility(View.VISIBLE);
                viewed_serie.setVisibility(View.GONE);
            }
        } else {
            viewed_serie_text.setVisibility(View.GONE);
            viewed_serie.setVisibility(View.GONE);
            no_viewed_serie.setVisibility(View.GONE);
        }
    }

    private void LoadEpisodes() {
        season = serie.GetSeasonFistEpisodeNotViewed();
        reloadSeason();
    }

    private void LoadActors(){
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

    private void reloadSeason(){
        if (serie.getEpisodes().size() > 0){
            episodeRecyclerView.setVisibility(View.VISIBLE);
            empty_Episodes.setVisibility(View.GONE);
            actual_season.setVisibility(View.VISIBLE);
            List<Episode> episodeList = serie.getSeason(season);
            episodesAdapter = new EpisodesAdapter(episodeList, R.layout.item_list_episodes, serie.getViewed());
            episodeRecyclerView.setAdapter(episodesAdapter);
            episodesAdapter.notifyDataSetChanged();
            if (season == serie.getSeasonsCount()) {
                next_season.setVisibility(View.GONE);
                next_season_disable.setVisibility(View.VISIBLE);
            } else {
                next_season.setVisibility(View.VISIBLE);
                next_season_disable.setVisibility(View.GONE);
            }
            if (season == 1) {
                previous_season.setVisibility(View.GONE);
                previous_season_disable.setVisibility(View.VISIBLE);
            } else {
                previous_season.setVisibility(View.VISIBLE);
                previous_season_disable.setVisibility(View.GONE);
            }
            actual_season.setText(season + "/" + serie.getSeasonsCount() + " " + getResources().getString(R.string.season_series));
            // if the serie is viewed it is not necessary the check of the seasons
            if (serie.IsSaved() && serie.getViewed() == 0){
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
        }else {
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

    }

    @Override
    public void onClick(View view) {
        ImageButton button = (ImageButton)view;
        //share button
        switch (button.getId()){
            case R.id.share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String title = getResources().getString(R.string.check_serie) + serie.getName();
                String shareBody = "https://trakt.tv/search/tvdb/" + serie.getId() + "?id_type=show";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, title + " " + shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;
            case R.id.iv_tick_off:
                save_button.setVisibility(View.GONE);
                save_text.setVisibility(View.GONE);
                unsave_button.setVisibility(View.VISIBLE);
                unsave_text.setVisibility(View.VISIBLE);
                if (serie != null && !serie.getId().equals("")) {
                    SaveTask save = new SaveTask();
                    save.execute();
                    sendSaveSerie(); //Analytics
                }

                break;
            case R.id.iv_tick_on:
                unsave_button.setVisibility(View.GONE);
                unsave_text.setVisibility(View.GONE);
                save_button.setVisibility(View.VISIBLE);
                save_text.setVisibility(View.VISIBLE);
                if (serie != null && !serie.getId().equals("")) {
                    serie.Delete();
                    sendDeleteSerie(); //Analytics
                }
                LoadEpisodes();
                CheckViewedSerie();
                break;
            case  R.id.refresh:
                if (serie != null && !serie.getId().equals("")) {
                    isRefreshing = true;
                    FetchSerieByIDTask getSerie = new FetchSerieByIDTask();
                    getSerie.execute();
                }
                break;
            case  R.id.next_season:
                season = season + 1;
                reloadSeason();
                break;
            case  R.id.previous_season:
                season = season - 1;
                reloadSeason();
                break;
            case  R.id.viewed_season:
                List<Episode> episodeList = serie.getSeason(season);
                for (Episode episode : episodeList) {
                    episode.setViewed(0);
                    episode.UpdateViewed();
                }
                episodesAdapter = new EpisodesAdapter(episodeList, R.layout.item_list_episodes, serie.getViewed());
                episodeRecyclerView.setAdapter(episodesAdapter);
                episodesAdapter.notifyDataSetChanged();
                viewed_season.setVisibility(View.GONE);
                no_viewed_season.setVisibility(View.VISIBLE);
                break;
            case  R.id.no_viewed_season:
                List<Episode> episodeViewList = serie.getSeason(season);
                for (Episode episode : episodeViewList) {
                    if (episode.getViewed() == 0){
                        episode.setViewed(1);
                        episode.UpdateViewed();
                    }
                }
                episodesAdapter = new EpisodesAdapter(episodeViewList, R.layout.item_list_episodes, serie.getViewed());
                episodeRecyclerView.setAdapter(episodesAdapter);
                episodesAdapter.notifyDataSetChanged();
                viewed_season.setVisibility(View.VISIBLE);
                no_viewed_season.setVisibility(View.GONE);
                break;
            case  R.id.viewed_serie:
                serie.setViewed(0);
                serie.UpdateVieded();
                season = 1;
                reloadSeason();
                CheckViewedSerie();
                break;
            case  R.id.no_viewed_serie:
                serie.setViewed(1);
                serie.UpdateVieded();
                season = serie.getSeasonsCount();
                reloadSeason();
                CheckViewedSerie();
                break;
        }
    }

    public class FetchSerieByIDTask extends AsyncTask<String, Void,  Serie> {

        private final String LOG_TAG = FetchSerieByIDTask.class.getSimpleName();


        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nDialog = new ProgressDialog(getActivity());
            nDialog.setMessage(getActivity().getResources().getString(R.string.Loading));
            nDialog.setTitle(getActivity().getResources().getString(R.string.downloading_data));
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(false);
            nDialog.show();

        }

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
            nDialog.dismiss();
        }
    }

    // sometime the save could last,  that is why I use this, to show the process dialog
    private class SaveTask extends AsyncTask<Void, Void, String> {

        private final String LOG_TAG = SaveTask.class.getSimpleName();


        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nDialog = new ProgressDialog(getActivity());
            nDialog.setMessage(getResources().getString(R.string.Loading));
            nDialog.setTitle(getResources().getString(R.string.saving_data));
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(false);
            nDialog.show();

        }

        @Override
        protected String doInBackground(Void... params) {
            serie.Save();
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            LoadEpisodes();
            CheckViewedSerie();
            nDialog.dismiss();
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
