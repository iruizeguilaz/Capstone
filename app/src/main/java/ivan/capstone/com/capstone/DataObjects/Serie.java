package ivan.capstone.com.capstone.DataObjects;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import ivan.capstone.com.capstone.Data.SeriesContract;
import ivan.capstone.com.capstone.MyApplication;

/**
 * Data object Serie, it is parcelble, it also has methods to bring data from database, to save, and delete
 * Created by Ivan on 19/02/2016.
 */
public class Serie implements Parcelable {

    private long _id; // databaseID

    private String id;  // server series id
    private String name;
    private String image_url;
    private String overView;
    private String dateReleased;
    private String network;
    private String rating;
    private String votes;
    private String genre;
    private String poster_url;
    private ArrayList<Episode> episodes = new ArrayList<Episode>();

    public Serie(){
        _id = 0;
        id = "";
        name= "";
        image_url= "";
        overView= "";
        dateReleased= "";
        network= "";
        rating= "";
        votes= "";
        genre= "";
        poster_url= "";
        episodes = new ArrayList<Episode>();
    }

    protected Serie(Parcel in) {
        _id = in.readLong();
        id = in.readString();
        name = in.readString();
        image_url = in.readString();
        overView = in.readString();
        dateReleased = in.readString();
        network = in.readString();
        rating = in.readString();
        votes = in.readString();
        genre = in.readString();
        poster_url = in.readString();
        in.readTypedList(episodes, Episode.CREATOR);
    }

    public Serie(int _id, String id, String name, String image_url, String overView, String dateReleased, String network, String rating, String votes, String genre, String poster_url, ArrayList<Episode> episodes) {
        this._id = _id;
        this.id = id;
        this.name = name;
        this.image_url = image_url;
        this.overView = overView;
        this.dateReleased = dateReleased;
        this.network = network;
        this.rating = rating;
        this.votes = votes;
        this.genre = genre;
        this.poster_url = poster_url;
        this.episodes = episodes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(_id);
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(image_url);
        dest.writeString(overView);
        dest.writeString(dateReleased);
        dest.writeString(network);
        dest.writeString(rating);
        dest.writeString(votes);
        dest.writeString(genre);
        dest.writeString(poster_url);
        dest.writeTypedList(episodes);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Serie> CREATOR = new Parcelable.Creator<Serie>() {
        @Override
        public Serie createFromParcel(Parcel in) {
            return new Serie(in);
        }

        @Override
        public Serie[] newArray(int size) {
            return new Serie[size];
        }
    };

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public String getDateReleased() {
        return dateReleased;
    }

    public void setDateReleased(String dateReleased) {
        this.dateReleased = dateReleased;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getRating() {
        return rating;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPoster_url() {
        return poster_url;
    }

    public void setPoster_url(String poster_url) {
        this.poster_url = poster_url;
    }

    public ArrayList<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(ArrayList<Episode> episodes) {
        this.episodes = episodes;
    }

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
            SeriesContract.SeriesEntry.COLUMN_NETWORK
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

    private static final String[] EPISODES_COLUMNS = {
            SeriesContract.EpisodesEntry._ID,
            SeriesContract.EpisodesEntry.COLUMN_SERIE_ID,
            SeriesContract.EpisodesEntry.COLUMN_SEASON_ID,
            SeriesContract.EpisodesEntry.COLUMN_EPISODE_ID,
            SeriesContract.EpisodesEntry.COLUMN_SEASON_NUMBER,
            SeriesContract.EpisodesEntry.COLUMN_EPISODE_NUMBER,
            SeriesContract.EpisodesEntry.COLUMN_NAME,
            SeriesContract.EpisodesEntry.COLUMN_DATE,
            SeriesContract.EpisodesEntry.COLUMN_OVERVIEW,
            SeriesContract.EpisodesEntry.COLUMN_RATING,
            SeriesContract.EpisodesEntry.COLUMN_VOTES,
            SeriesContract.EpisodesEntry.COLUMN_IMAGE_URL
    };


    static final int COLUMN_SERIE_ID = 1;
    static final int COLUMN_SEASON_ID = 2;
    static final int COLUMN_EPISODE_ID = 3;
    static final int COLUMN_SEASON_NUMBER = 4;
    static final int COLUMN_EPISODE_NUMBER = 5;
    static final int COLUMN_NAME = 6;
    static final int COLUMN_DATE = 7;
    static final int COLUMN_OVERVIEW = 8;
    static final int COLUMN_RATING = 9;
    static final int COLUMN_VOTES = 10;
    static final int COLUMN_IMAGE_URL = 11;

    // get the data from the database by id
    public void LoadData(){
        if (id.equals("")) return;
        Cursor data = MyApplication.getContext().getContentResolver().query(
                SeriesContract.SeriesEntry.CONTENT_URI,
                SERIES_COLUMNS,
                SeriesContract.SeriesEntry.COLUMN_ID + " = ?",
                new String[]{id},
                null);
        if (data != null && data.moveToFirst()) {
            _id = data.getLong(COL__ID);
            name = data.getString(COL_NAME);
            rating = data.getString(COL_RATING);
            votes = data.getString(COL_VOTES);
            image_url = data.getString(COL_BANNER);
            poster_url = data.getString(COL_POSTER);
            dateReleased = data.getString(COL_RELEASED_DATE);
            overView = data.getString(COL_OVERVIEW);
            genre = data.getString(COL_GENRE);
            network = data.getString(COL_NETWORK);
        }
        if (data != null) data.close();
        // Load episodes
        LoadEpisodes();
    }

    public void LoadEpisodes(){
        if (id.equals("")) return;
        Cursor data = MyApplication.getContext().getContentResolver().query(
                SeriesContract.EpisodesEntry.CONTENT_URI,
                EPISODES_COLUMNS,
                SeriesContract.EpisodesEntry.COLUMN_SERIE_ID + " = ?",
                new String[]{id},
                null);
        episodes = new ArrayList<>();
        while (data.moveToNext()) {
            Episode myEpisode = new Episode();
            myEpisode.set_id(COL__ID);
            myEpisode.setSerie_id(data.getString(COLUMN_SERIE_ID));
            myEpisode.setSeason_id(data.getString(COLUMN_SEASON_ID));
            myEpisode.setEpisode_id(data.getString(COLUMN_EPISODE_ID));
            myEpisode.setSeason_number(data.getInt(COLUMN_SEASON_NUMBER));
            myEpisode.setEpisode_number(data.getInt(COLUMN_EPISODE_NUMBER));
            myEpisode.setName(data.getString(COLUMN_NAME));
            myEpisode.setDate(data.getString(COLUMN_DATE));
            myEpisode.setOverview(data.getString(COLUMN_OVERVIEW));
            myEpisode.setRating(data.getString(COLUMN_RATING));
            myEpisode.setVotes(data.getString(COLUMN_VOTES));
            myEpisode.setImage_url(data.getString(COLUMN_IMAGE_URL));
            episodes.add(myEpisode);
        }
        data.close();
    }

    public boolean IsSaved() {
        if (id.equals("")) return false;
        Cursor seriesCursor = MyApplication.getContext().getContentResolver().query(
                    SeriesContract.SeriesEntry.CONTENT_URI,
                    new String[]{SeriesContract.SeriesEntry._ID},
                    SeriesContract.SeriesEntry.COLUMN_ID + " = ?",
                    new String[]{id},
                    null);
        boolean saved = false;
        if (seriesCursor != null && seriesCursor.moveToFirst()) saved = true;
        if (seriesCursor != null) seriesCursor.close();
        return saved;
    }

    public void Save() {
        if (!IsSaved()) {
            ContentValues locationValues = new ContentValues();
            // Then add the data, along with the corresponding name of the data type,
            // so the content provider knows what kind of value is being inserted.
            locationValues.put(SeriesContract.SeriesEntry.COLUMN_ID, id);
            locationValues.put(SeriesContract.SeriesEntry.COLUMN_NAME, name);
            locationValues.put(SeriesContract.SeriesEntry.COLUMN_NETWORK, network);
            locationValues.put(SeriesContract.SeriesEntry.COLUMN_POSTER_URL, poster_url);
            locationValues.put(SeriesContract.SeriesEntry.COLUMN_BANNER_URL, image_url);
            locationValues.put(SeriesContract.SeriesEntry.COLUMN_OVERVIEW, overView);
            locationValues.put(SeriesContract.SeriesEntry.COLUMN_RATING, rating);
            locationValues.put(SeriesContract.SeriesEntry.COLUMN_VOTES, votes);
            locationValues.put(SeriesContract.SeriesEntry.COLUMN_REALSED_DATE, dateReleased);
            locationValues.put(SeriesContract.SeriesEntry.COLUMN_GENRE, genre);
            // Finally, insert serie data into the database.
            Uri insertedUri = MyApplication.getContext().getContentResolver().insert(
                    SeriesContract.SeriesEntry.CONTENT_URI,
                    locationValues
            );
            // The resulting URI contains the ID for the row.  Extract the locationId from the Uri.
            _id = ContentUris.parseId(insertedUri);
            if (episodes != null && episodes.size() > 0) {
                List<Episode> list = episodes;
                for (Episode episode : list) {
                    episode.Save();
                }
            }
        }
    }

    public void Update() {
        if (IsSaved()) Delete();
        Save();
    }

    public void Delete() {
        if (IsSaved()) {
            if (episodes!= null && episodes.size() > 0) {
                MyApplication.getContext().getContentResolver().delete(SeriesContract.EpisodesEntry.CONTENT_URI, SeriesContract.EpisodesEntry.COLUMN_SERIE_ID +"=?", new String[]{id});
            }
            MyApplication.getContext().getContentResolver().delete(SeriesContract.SeriesEntry.CONTENT_URI, SeriesContract.SeriesEntry.COLUMN_ID +"=?", new String[]{id});
            _id = 0;
        }
    }
}
