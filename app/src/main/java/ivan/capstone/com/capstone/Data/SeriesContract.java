package ivan.capstone.com.capstone.Data;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Created by Ivan on 07/03/2016.
 */
public class SeriesContract {

    public static final String CONTENT_AUTHORITY = "ivan.capstone.com.capstone";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_SERIES = "series";

    public static final String PATH_EPISODES = "episodes";

    public static final String PATH_ACTORS = "actors";

    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    /* Inner class that defines the table contents of the series table */
    public static final class SeriesEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SERIES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SERIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SERIES;

        // Table name
        public static final String TABLE_NAME = "series";

        //fields
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_NETWORK = "network";
        public static final String COLUMN_REALSED_DATE = "date";
        public static final String COLUMN_VOTES = "votes";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_BANNER_URL = "banner";
        public static final String COLUMN_POSTER_URL = "poster";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_GENRE = "genre";

        public static final String COLUMN_MODIFYDATE = "modifydate";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_VIEWED = "viewed";

        public static Uri buildSeriesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getIDFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }


    /* Inner class that defines the table contents of the episodes table */
    public static final class EpisodesEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_EPISODES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EPISODES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EPISODES;

        // Table name
        public static final String TABLE_NAME = "episodes";

        //fields
        public static final String COLUMN_SERIE_ID = "serie_id";
        public static final String COLUMN_SEASON_ID = "season_id";
        public static final String COLUMN_EPISODE_ID = "episode_id";

        public static final String COLUMN_SEASON_NUMBER = "sesion_number";
        public static final String COLUMN_EPISODE_NUMBER = "episode_number";
        public static final String COLUMN_NAME= "name";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_VOTES = "votes";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_IMAGE_URL = "image_url";

        public static final String COLUMN_VIEWED = "viewed";

        public static Uri buildEpisodesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getEpisodeIDFromUri(Uri uri) {
            return uri.getPathSegments().get(3);
        }

        public static String getSerieIDFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    /* Inner class that defines the table contents of the episodes table */
    public static final class ActorsEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ACTORS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ACTORS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ACTORS;

        // Table name
        public static final String TABLE_NAME = "actors";

        //fields
        public static final String COLUMN_ACTOR_ID = "actor_id";
        public static final String COLUMN_SERIE_ID = "serie_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_ROLE = "role";
        public static final String COLUMN_IMAGE_URL = "image_url";


        public static Uri buildActorsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getActorIDFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String getSerieIDFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }
    }

}
