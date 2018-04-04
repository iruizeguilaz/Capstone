package ivan.capstone.com.capstone.DataObjects;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import ivan.capstone.com.capstone.Data.SeriesContract;
import ivan.capstone.com.capstone.MyApplication;

/**
 * Created by Ivan on 12/03/2016.
 */
public class Episode {

    private long _id;
    private String serie_id;
    private String season_id;
    private String episode_id;

    private int season_number;
    private int episode_number;

    private String name;
    private String date;
    private String overview;
    private String votes;
    private String rating;
    private String image_url;

    private int viewed;

    public Episode(){
        _id = 0;
        serie_id = "";
        season_id = "";
        episode_id = "";
        season_number = 0;
        episode_number = 0;
        name = "";
        date = "";
        overview= "";
        votes= "";
        rating= "";
        image_url= "";
        viewed = 0;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getSerie_id() {
        return serie_id;
    }

    public void setSerie_id(String serie_id) {
        this.serie_id = serie_id;
    }

    public String getSeason_id() {
        return season_id;
    }

    public void setSeason_id(String season_id) {
        this.season_id = season_id;
    }

    public String getEpisode_id() {
        return episode_id;
    }

    public void setEpisode_id(String episode_id) {
        this.episode_id = episode_id;
    }

    public int getSeason_number() {
        return season_number;
    }

    public void setSeason_number(int season_number) {
        this.season_number = season_number;
    }

    public int getEpisode_number() {
        return episode_number;
    }

    public void setEpisode_number(int episode_number) {
        this.episode_number = episode_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getImage_url() {
        return image_url.replace("http", "https");
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getViewed() {
        return viewed;
    }

    public void setViewed(int viewed) {
        this.viewed = viewed;
    }

    public boolean IsSaved() {
        if (episode_id.equals("")) return false;
        Cursor seriesCursor = MyApplication.getContext().getContentResolver().query(
                SeriesContract.EpisodesEntry.CONTENT_URI,
                new String[]{SeriesContract.EpisodesEntry._ID},
                SeriesContract.EpisodesEntry.COLUMN_EPISODE_ID + " = ?",
                new String[]{episode_id},
                null);
        if (seriesCursor != null && seriesCursor.moveToFirst()) return true;
        else return false;
    }
    private static final String[] VIEWED_COLUMN = {
            SeriesContract.EpisodesEntry.COLUMN_VIEWED
    };

    public void LoadViewed()
    {
        if (episode_id.equals("")) return;
        Cursor data = MyApplication.getContext().getContentResolver().query(
                SeriesContract.EpisodesEntry.CONTENT_URI,
                VIEWED_COLUMN,
                SeriesContract.EpisodesEntry.COLUMN_EPISODE_ID + " = ?",
                new String[]{episode_id},
                null);

        if (data != null && data.moveToFirst()) {
            viewed = data.getInt(0);
        }
        data.close();
    }

    public void Save() {
        if (!IsSaved()) {
            ContentValues locationValues = new ContentValues();
            // Then add the data, along with the corresponding name of the data type,
            // so the content provider knows what kind of value is being inserted.
            locationValues.put(SeriesContract.EpisodesEntry.COLUMN_SERIE_ID, serie_id);
            locationValues.put(SeriesContract.EpisodesEntry.COLUMN_SEASON_ID, season_id);
            locationValues.put(SeriesContract.EpisodesEntry.COLUMN_EPISODE_ID, episode_id);
            locationValues.put(SeriesContract.EpisodesEntry.COLUMN_SEASON_NUMBER, season_number);
            locationValues.put(SeriesContract.EpisodesEntry.COLUMN_EPISODE_NUMBER, episode_number);
            locationValues.put(SeriesContract.EpisodesEntry.COLUMN_NAME, name);
            locationValues.put(SeriesContract.EpisodesEntry.COLUMN_DATE, date);
            locationValues.put(SeriesContract.EpisodesEntry.COLUMN_OVERVIEW, overview);
            locationValues.put(SeriesContract.EpisodesEntry.COLUMN_VOTES, votes);
            locationValues.put(SeriesContract.EpisodesEntry.COLUMN_RATING, rating);
            locationValues.put(SeriesContract.EpisodesEntry.COLUMN_IMAGE_URL, image_url);
            locationValues.put(SeriesContract.EpisodesEntry.COLUMN_VIEWED, viewed);
            // Finally, insert episode data into the database.
            Uri insertedUri = MyApplication.getContext().getContentResolver().insert(
                    SeriesContract.EpisodesEntry.CONTENT_URI,
                    locationValues
            );
            _id = ContentUris.parseId(insertedUri);
        }
    }

    public void Delete() {
        if (IsSaved()) {
            MyApplication.getContext().getContentResolver().delete(SeriesContract.EpisodesEntry.CONTENT_URI, SeriesContract.EpisodesEntry.COLUMN_EPISODE_ID +"=?", new String[]{episode_id});
            _id = 0;
        }
    }

    public void UpdateViewed() {
        if (IsSaved()) {
            ContentValues values = new ContentValues();
            values.put(SeriesContract.EpisodesEntry.COLUMN_VIEWED, viewed);
            MyApplication.getContext().getContentResolver().update(SeriesContract.EpisodesEntry.CONTENT_URI, values,
                    SeriesContract.EpisodesEntry.COLUMN_EPISODE_ID +"=?", new String[]{episode_id} );
            _id = 0;
        }
    }

    public void Update() {
        if (IsSaved()) {
            ContentValues values = new ContentValues();
            values.put(SeriesContract.EpisodesEntry.COLUMN_NAME, name);
            values.put(SeriesContract.EpisodesEntry.COLUMN_DATE, date);
            values.put(SeriesContract.EpisodesEntry.COLUMN_OVERVIEW, overview);
            values.put(SeriesContract.EpisodesEntry.COLUMN_VOTES, votes);
            values.put(SeriesContract.EpisodesEntry.COLUMN_RATING, rating);
            values.put(SeriesContract.EpisodesEntry.COLUMN_IMAGE_URL, image_url);
            MyApplication.getContext().getContentResolver().update(SeriesContract.EpisodesEntry.CONTENT_URI, values,
                    SeriesContract.EpisodesEntry.COLUMN_EPISODE_ID +"=?", new String[]{episode_id} );
        }
    }

}
