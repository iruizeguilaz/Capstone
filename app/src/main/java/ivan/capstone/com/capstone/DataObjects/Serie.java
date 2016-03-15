package ivan.capstone.com.capstone.DataObjects;

import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ivan.capstone.com.capstone.Data.SeriesContract;
import ivan.capstone.com.capstone.MyApplication;
import ivan.capstone.com.capstone.R;

/**
 * Data object Serie, it is parcelble, it also has methods to bring data from database, to save, and delete
 * Created by Ivan on 19/02/2016.
 */
public class Serie implements Parcelable {

    public final String ENDED = "Ended";

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
    private Date modify_date;
    private String status; // Ended or Continuing
    private int viewed;
    private ArrayList<Episode> episodes = new ArrayList<Episode>();
    private ArrayList<Actor> actors = new ArrayList<Actor>();

    public Serie() {
        _id = 0;
        id = "";
        name = "";
        image_url = "";
        overView = "";
        dateReleased = "";
        network = "";
        rating = "";
        votes = "";
        genre = "";
        poster_url = "";
        modify_date = Calendar.getInstance().getTime();
        status = "Continuing";
        viewed = 0;
        episodes = new ArrayList<Episode>();
        actors = new ArrayList<Actor>();
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
        modify_date = new Date(in.readLong());
        status = in.readString();
        viewed = in.readInt();
        LoadEpisodes();
        LoadActors();
        // instead of stored it, I realod the data. this is because stored for instances 27 seasons
        // that the simsonps has fail.
        //in.readTypedList(episodes, Episode.CREATOR);
        //in.readTypedList(actors, Actor.CREATOR);
    }

    public Serie(int _id, String id, String name, String image_url, String overView, String dateReleased, String network, String rating, String votes, String genre, String poster_url, Date modify_date, String status, int viewd) {
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
        this.modify_date = modify_date;
        this.status = status;
        this.viewed = viewd;
        //this.episodes = episodes;
        //this.actors = actors;
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
        dest.writeLong(modify_date.getTime());
        dest.writeString(status);
        dest.writeInt(viewed);
        //dest.writeTypedList(episodes);
        //dest.writeTypedList(actors);
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

    public ArrayList<Actor> getActors() {
        return actors;
    }

    public void setActors(ArrayList<Actor> actors) {
        this.actors = actors;
    }

    public Date getModify_date() {
        return modify_date;
    }

    public void setModify_date(Date modify_date) {
        this.modify_date = modify_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getViewed() {
        return viewed;
    }

    public void setViewed(int viewed) {
        this.viewed = viewed;
    }

    public int getSeasonsCount() {
        if (episodes.size() == 0) return 0;
        int seasons = episodes.get(episodes.size() - 1).getSeason_number();
        return seasons;
    }

    public int GetSeasonFistEpisodeNotViewed() {
        if (episodes.size() == 0) return 0;
        if (episodes.size() == 1) return 1;
        List<Episode> list = episodes;
        for (Episode episode : list) {
            if (episode.getViewed() == 0)
                return episode.getSeason_number();
        }
        return 1;
    }

    public ArrayList<Episode> getSeason(int season) {
        if (season == 0 || season > getSeasonsCount()) return new ArrayList<>();
        if (episodes.size() == 0) return new ArrayList<>();
        ArrayList<Episode> seasonEpisodes = new ArrayList<>();
        List<Episode> list = episodes;
        for (Episode episode : list) {
            if (episode.getSeason_number() == season)
                seasonEpisodes.add(episode);
        }
        return seasonEpisodes;
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
            SeriesContract.SeriesEntry.COLUMN_NETWORK,
            SeriesContract.SeriesEntry.COLUMN_MODIFYDATE,
            SeriesContract.SeriesEntry.COLUMN_STATUS,
            SeriesContract.SeriesEntry.COLUMN_VIEWED
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
    static final int COLUMN_MODIFYDATE = 11;
    static final int COL_STATUS = 12;
    static final int COL_VIEWED = 13;

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
            SeriesContract.EpisodesEntry.COLUMN_IMAGE_URL,
            SeriesContract.EpisodesEntry.COLUMN_VIEWED
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
    static final int COLUMN_VIEWED = 12;

    private static final String[] ACTORS_COLUMNS = {
            SeriesContract.ActorsEntry._ID,
            SeriesContract.ActorsEntry.COLUMN_ACTOR_ID,
            SeriesContract.ActorsEntry.COLUMN_SERIE_ID,
            SeriesContract.ActorsEntry.COLUMN_NAME,
            SeriesContract.ActorsEntry.COLUMN_ROLE,
            SeriesContract.ActorsEntry.COLUMN_IMAGE_URL
    };

    static final int COLUMN_ACTOR_ID = 1;
    static final int COLUMN_ACTOR_SERIEID = 2;
    static final int COLUMN_ACTOR_NAME = 3;
    static final int COLUMN_ACTOR_ROLE = 4;
    static final int COLUMN_ACTOR_IMAGEURL = 5;

    // get the data from the database by id
    public void LoadData() {
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
            modify_date = new Date(data.getLong(COLUMN_MODIFYDATE) * 1000);
            status = data.getString(COL_STATUS);
            viewed = data.getInt(COL_VIEWED);
        }
        if (data != null) data.close();
        // Load episodes
        LoadEpisodes();
        // Load actors
        LoadActors();
    }

    public void LoadEpisodes() {
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
            myEpisode.setViewed(data.getInt(COLUMN_VIEWED));
            episodes.add(myEpisode);
        }
        data.close();
    }


    public void LoadActors() {
        if (id.equals("")) return;
        Cursor data = MyApplication.getContext().getContentResolver().query(
                SeriesContract.ActorsEntry.CONTENT_URI,
                ACTORS_COLUMNS,
                SeriesContract.ActorsEntry.COLUMN_SERIE_ID + " = ?",
                new String[]{id},
                null);
        actors = new ArrayList<>();
        while (data.moveToNext()) {
            Actor myActor = new Actor();
            myActor.set_id(COL__ID);
            myActor.setActor_id(data.getString(COLUMN_ACTOR_ID));
            myActor.setSerie_id(data.getString(COLUMN_ACTOR_SERIEID));
            myActor.setName(data.getString(COLUMN_ACTOR_NAME));
            myActor.setRole(data.getString(COLUMN_ACTOR_ROLE));
            myActor.setImage_url(data.getString(COLUMN_ACTOR_IMAGEURL));
            actors.add(myActor);
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
            ContentValues values = new ContentValues();
            // Then add the data, along with the corresponding name of the data type,
            // so the content provider knows what kind of value is being inserted.
            values.put(SeriesContract.SeriesEntry.COLUMN_ID, id);
            values.put(SeriesContract.SeriesEntry.COLUMN_NAME, name);
            values.put(SeriesContract.SeriesEntry.COLUMN_NETWORK, network);
            values.put(SeriesContract.SeriesEntry.COLUMN_POSTER_URL, poster_url);
            values.put(SeriesContract.SeriesEntry.COLUMN_BANNER_URL, image_url);
            values.put(SeriesContract.SeriesEntry.COLUMN_OVERVIEW, overView);
            values.put(SeriesContract.SeriesEntry.COLUMN_RATING, rating);
            values.put(SeriesContract.SeriesEntry.COLUMN_VOTES, votes);
            values.put(SeriesContract.SeriesEntry.COLUMN_REALSED_DATE, dateReleased);
            values.put(SeriesContract.SeriesEntry.COLUMN_GENRE, genre);
            values.put(SeriesContract.SeriesEntry.COLUMN_MODIFYDATE, modify_date.getTime());
            values.put(SeriesContract.SeriesEntry.COLUMN_STATUS, status);
            values.put(SeriesContract.SeriesEntry.COLUMN_VIEWED, viewed);
            // Finally, insert serie data into the database.
            Uri insertedUri = MyApplication.getContext().getContentResolver().insert(
                    SeriesContract.SeriesEntry.CONTENT_URI,
                    values
            );
            // The resulting URI contains the ID for the row.  Extract the locationId from the Uri.
            _id = ContentUris.parseId(insertedUri);
            if (episodes != null && episodes.size() > 0) {
                List<Episode> list = episodes;
                for (Episode episode : list) {
                    episode.Save();
                }
            }
            if (actors != null && actors.size() > 0) {
                List<Actor> list = actors;
                for (Actor actor : list) {
                    actor.Save();
                }
            }
        }
    }


    public void Update(boolean isRefresing) {
        // when we refresh the data from the internet, we load again episodes and we neew to get back
        // to the objects that already exists if the were viewed that is smth we have stored.
        if (IsSaved()) {
            ContentValues values = new ContentValues();
            // Then add the data, along with the corresponding name of the data type,
            // so the content provider knows what kind of value is being inserted.

            values.put(SeriesContract.SeriesEntry.COLUMN_NAME, name);
            values.put(SeriesContract.SeriesEntry.COLUMN_NETWORK, network);
            values.put(SeriesContract.SeriesEntry.COLUMN_POSTER_URL, poster_url);
            values.put(SeriesContract.SeriesEntry.COLUMN_BANNER_URL, image_url);
            values.put(SeriesContract.SeriesEntry.COLUMN_OVERVIEW, overView);
            values.put(SeriesContract.SeriesEntry.COLUMN_RATING, rating);
            values.put(SeriesContract.SeriesEntry.COLUMN_VOTES, votes);
            values.put(SeriesContract.SeriesEntry.COLUMN_REALSED_DATE, dateReleased);
            values.put(SeriesContract.SeriesEntry.COLUMN_GENRE, genre);
            values.put(SeriesContract.SeriesEntry.COLUMN_MODIFYDATE, modify_date.getTime());
            values.put(SeriesContract.SeriesEntry.COLUMN_STATUS, status);

            // Finally, insert serie data into the database.
            MyApplication.getContext().getContentResolver().update(
                    SeriesContract.SeriesEntry.CONTENT_URI,
                    values, SeriesContract.SeriesEntry.COLUMN_ID + "=?", new String[]{id}
            );
            // The resulting URI contains the ID for the row.  Extract the locationId from the Uri.
            if (episodes != null && episodes.size() > 0) {
                List<Episode> list = episodes;
                for (Episode episode : list) {
                    if (episode.IsSaved()) {
                        episode.Update();
                        if (isRefresing) {
                            // we need to get back the field viewed from database to inflate it properly
                            episode.LoadViewed();
                        }
                    } else episode.Save();
                }
            }
            if (actors != null && actors.size() > 0) {
                List<Actor> list = actors;
                for (Actor actor : list) {
                    if (actor.IsSaved()) actor.Update();
                    else actor.Save();
                }
            }
        } else {
            Save();
        }
    }

    public void Delete() {
        if (IsSaved()) {
            if (episodes != null && episodes.size() > 0) {
                MyApplication.getContext().getContentResolver().delete(SeriesContract.EpisodesEntry.CONTENT_URI, SeriesContract.EpisodesEntry.COLUMN_SERIE_ID + "=?", new String[]{id});
            }
            if (actors != null && actors.size() > 0) {
                MyApplication.getContext().getContentResolver().delete(SeriesContract.ActorsEntry.CONTENT_URI, SeriesContract.ActorsEntry.COLUMN_SERIE_ID + "=?", new String[]{id});
            }
            MyApplication.getContext().getContentResolver().delete(SeriesContract.SeriesEntry.CONTENT_URI, SeriesContract.SeriesEntry.COLUMN_ID + "=?", new String[]{id});
            _id = 0;
        }
    }

    public void UpdateVieded() {
        if (IsSaved()) {
            // update serie
            ContentValues values = new ContentValues();
            values.put(SeriesContract.SeriesEntry.COLUMN_VIEWED, viewed);
            MyApplication.getContext().getContentResolver().update(SeriesContract.SeriesEntry.CONTENT_URI, values,
                    SeriesContract.SeriesEntry.COLUMN_ID + "=?", new String[]{id});
            // update episodes
            values = new ContentValues();
            values.put(SeriesContract.EpisodesEntry.COLUMN_VIEWED, viewed);
            MyApplication.getContext().getContentResolver().update(SeriesContract.EpisodesEntry.CONTENT_URI, values,
                    SeriesContract.EpisodesEntry.COLUMN_SERIE_ID + "=?", new String[]{id});
            for (Episode episode : episodes) {
                episode.setViewed(viewed);
            }
        }
    }



}
