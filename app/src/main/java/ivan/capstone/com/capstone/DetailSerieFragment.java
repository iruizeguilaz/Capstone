package ivan.capstone.com.capstone;


import android.content.Intent;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ivan.capstone.com.capstone.DataObjects.Serie;
import ivan.capstone.com.capstone.XML.XMLManager;


public class DetailSerieFragment extends Fragment {

    Serie serie;
    TextView name;
    TextView overview;
    TextView genre;
    TextView rating;
    TextView releasedDate;
    ImageView poster;

    public DetailSerieFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_detail_serie, container, false);
        if (savedInstanceState != null && savedInstanceState.getParcelable("Serie")!= null) {
            serie = savedInstanceState.getParcelable("Serie");
            LoadData();
        }
        else {
            Intent intent = getActivity().getIntent();
            serie = intent.getParcelableExtra("Serie");
            FetchSerieByIDTask getSerie = new FetchSerieByIDTask();
            getSerie.execute();
        }
        name = (TextView)rootView.findViewById(R.id.name_serie);
        overview = (TextView)rootView.findViewById(R.id.overview_serie);
        genre = (TextView)rootView.findViewById(R.id.genre_serie);
        rating = (TextView)rootView.findViewById(R.id.rating_serie);
        releasedDate = (TextView)rootView.findViewById(R.id.releasedDate_serie);
        poster = (ImageView) rootView.findViewById(R.id.poster);

        return rootView;
    }

    public void LoadData() {
        name.setText(serie.getName());
        overview.setText(serie.getOverView());
        genre.setText(serie.getGenre());
        rating.setText(serie.getRating());
        releasedDate.setText(serie.getDateReleased());
        Glide.with(getActivity())
                .load(serie.getPoster_url())
                .thumbnail(0.1f)
                .centerCrop()
                .into(poster);
    }

    public class FetchSerieByIDTask extends AsyncTask<String, Void,  Serie> {

        private final String LOG_TAG = FetchSerieByIDTask.class.getSimpleName();

        @Override
        protected  Serie doInBackground(String... params) {
            try {
                Serie result = null;
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                String bookJsonString = null;
                try {
                    final String SERIES_BASE_URL = "http://thetvdb.com/api/31700C7EECC0878D/series/" + serie.getId();
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
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (final IOException e) {
                            Log.e(LOG_TAG, "Error closing stream", e);
                            return null;
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
                LoadData();
            }else {
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(getActivity(),getString(R.string.nonetwork_message) , duration).show();
                //if (((MainActivity)getActivity()).mTwoPane) ((Callback) getActivity()).onItemSelected(null);
            }
        }
    }
}
