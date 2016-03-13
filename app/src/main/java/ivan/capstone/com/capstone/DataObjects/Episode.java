package ivan.capstone.com.capstone.DataObjects;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import ivan.capstone.com.capstone.Data.SeriesContract;
import ivan.capstone.com.capstone.MyApplication;

/**
 * Created by Ivan on 12/03/2016.
 */
public class Episode implements Parcelable {

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

    protected Episode(Parcel in) {
        _id = in.readLong();
        serie_id = in.readString();
        season_id = in.readString();
        episode_id = in.readString();
        season_number = in.readInt();
        episode_number = in.readInt();
        name = in.readString();
        date = in.readString();
        overview= in.readString();
        votes= in.readString();
        rating= in.readString();
        image_url= in.readString();
        viewed = in.readInt();
    }

    public Episode(int _id, String serie_id, String season_id, String episode_id, int sesion_number, int episode_number, String name, String date, String overview, String votes, String rating, String image_url, int viewed) {
        this._id = _id;
        this.serie_id = serie_id;
        this.season_id = season_id;
        this.episode_id = episode_id;
        this.season_number = sesion_number;
        this.episode_number = episode_number;
        this.name = name;
        this.date = date;
        this.overview= overview;
        this.votes= votes;
        this.rating= rating;
        this.image_url= image_url;
        this.viewed = viewed;
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(_id);
        dest.writeString(serie_id);
        dest.writeString(season_id);
        dest.writeString(episode_id);
        dest.writeInt(season_number);
        dest.writeInt(episode_number);
        dest.writeString(name);
        dest.writeString(date);
        dest.writeString(overview);
        dest.writeString(votes);
        dest.writeString(rating);
        dest.writeString(image_url);
        dest.writeInt(viewed);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Episode> CREATOR = new Parcelable.Creator<Episode>() {
        @Override
        public Episode createFromParcel(Parcel in) {
            return new Episode(in);
        }

        @Override
        public Episode[] newArray(int size) {
            return new Episode[size];
        }
    };


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
        return image_url;
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


}
