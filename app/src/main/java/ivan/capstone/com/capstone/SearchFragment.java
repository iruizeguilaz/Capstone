package ivan.capstone.com.capstone;


import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ivan.capstone.com.capstone.Adapter.SeriesAdapter;
import ivan.capstone.com.capstone.DataObjects.Serie;
import ivan.capstone.com.capstone.XML.XMLManager;


public class SearchFragment extends Fragment implements SeriesAdapter.OnItemClickListener{

    EditText inputSearch;
    RecyclerView recyclerView;
    FetchSerieByNameTask serieByNameTask;
    SeriesAdapter seriesAdapter;
    List<Serie> series;

    public SearchFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.search_recycler);

        // TODO declarar el adapter para reusarlo

        prepararLista();


        try {
            doSearch(rootView);
        }catch (Exception e)
        {
            Log.e("onCreateView", e.toString() + " " + e.getMessage());
        }

        return rootView;
    }

    private void prepararLista() {
        series= new ArrayList<Serie>();
        Serie serie = new Serie();
        serie.setId("280619");
        serie.setName("The Expanse");
        serie.setNetwork("Syfy");
        serie.setDateReleased("2015-11-23");
        serie.setImage_url("http://thetvdb.com//banners/graphical/280619-g6.jpg");
        series.add(serie);
        /*serie = new Serie();
        serie.setId("273385");
        serie.setName("Game of Thrones");
        serie.setNetwork("HBO");
        serie.setDateReleased("2011-04-17");
        serie.setImage_url("http://thetvdb.com//banners/graphical/121361-g37.jpg");
        series.add(serie);*/
        seriesAdapter = new SeriesAdapter(series, this);
        recyclerView.setAdapter(seriesAdapter);
    }

    private void doSearch(View rootView) {
        inputSearch = (EditText)rootView.findViewById(R.id.searchListSeries);

        inputSearch.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            if (serieByNameTask != null) serieByNameTask.cancel(true);
                            if (!inputSearch.getText().toString().equals("")) {
                                serieByNameTask = new FetchSerieByNameTask();
                                serieByNameTask.execute(inputSearch.getText().toString());
                            } else {
                                //if(mSpotifyAdapter!= null) mSpotifyAdapter.clear();
                            }
                            return true; // consume.

                        }
                        return false; // pass on to other listeners.
                    }
                });

       /* inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (serieByNameTask != null) serieByNameTask.cancel(true);
                if (!inputSearch.getText().toString().equals("")) {
                    serieByNameTask = new FetchSerieByNameTask();
                    serieByNameTask.execute(inputSearch.getText().toString());
                } else {
                    //if(mSpotifyAdapter!= null) mSpotifyAdapter.clear();
                }
            }
        });*/
    }

    @Override
    public void onClick(SeriesAdapter.ViewHolder viewHolder, String idArticulo) {
        // TODO cargar detalle
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(getActivity(),idArticulo , duration).show();
        //cargarDetalle(idArticulo);
    }


    public class FetchSerieByNameTask extends AsyncTask<String, Void,  List<Serie>> {

        private final String LOG_TAG = FetchSerieByNameTask.class.getSimpleName();

        private String getDataFromArtist(Serie serie){
            return serie.getName();
        }

        @Override
        protected  List<Serie> doInBackground(String... params) {
            try {
                List<Serie> result= new ArrayList<Serie>();
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                String bookJsonString = null;
                try {
                    final String SERIES_BASE_URL = "http://thetvdb.com/api/GetSeries.php?";
                    final String QUERY_PARAM = "seriesname";

                    Uri builtUri = Uri.parse(SERIES_BASE_URL).buildUpon()
                            .appendQueryParameter(QUERY_PARAM,  params[0] )
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
                    result = XMLManager.GetSeriesFromXML(parser);
                    // this query return a url of a banner image, is better to get the poster
                    // so we will do more queries to bring the posters
                    /*for (Serie serie: result){
                        final String BANNERS_BASE_URL = "http://thetvdb.com/api/31700C7EECC0878D/series/" + serie.getId() + "/banners.xml" ;
                        builtUri = Uri.parse(BANNERS_BASE_URL).buildUpon()
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
                        String url_poster = XMLManager.GetPosrterFromXML(parser);
                        if (!url_poster.equals("")){
                            serie.setImage_url("http://thetvdb.com//banners/" + url_poster);
                        }
                    }
                    */


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
        protected void onPostExecute(List<Serie> result) {
            if (result != null) {
                if (result.size() == 0) {
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(getActivity(),getString(R.string.noseries_message) , duration).show();
                    //if (((MainActivity)getActivity()).mTwoPane) ((Callback) getActivity()).onItemSelected(null);
                } else {
                    // TODO adaptar adapter

                    // intentar reinciar la lista
                    series.clear();
                    series.addAll(result);
                    seriesAdapter.notifyDataSetChanged();
                }
            }else {
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(getActivity(),getString(R.string.nonetwork_message) , duration).show();
                //if (((MainActivity)getActivity()).mTwoPane) ((Callback) getActivity()).onItemSelected(null);
            }
        }
    }

}
