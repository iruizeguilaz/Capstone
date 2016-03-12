package ivan.capstone.com.capstone.Data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by Ivan on 07/03/2016.
 */
public class SeriesProvider  extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private SeriesDbHelper mOpenHelper;

    static final int SERIES = 100;
    static final int SERIES_ID = 101;
    static final int EPISODES = 200;
    static final int EPISODES_ID = 201;
    static final int EPISODES_SERIEID = 202;

    //location.location_setting = ?
    private static final String sSeriesIDSelection =
            SeriesContract.SeriesEntry.TABLE_NAME+
                    "." + SeriesContract.SeriesEntry.COLUMN_ID + " = ? ";

    private static final String sEpisodesIDSelection =
            SeriesContract.EpisodesEntry.TABLE_NAME+
                    "." + SeriesContract.EpisodesEntry.COLUMN_EPISODE_ID + " = ? ";

    private static final String sEpisodesSerieSelection =
            SeriesContract.EpisodesEntry.TABLE_NAME+
                    "." + SeriesContract.EpisodesEntry.COLUMN_SERIE_ID + " = ? ";

    @Override
    public boolean onCreate() {
        mOpenHelper = new SeriesDbHelper(getContext());
        return true;
    }

    private static final SQLiteQueryBuilder sSeriesByIdQueryBuilder;

    static{
        sSeriesByIdQueryBuilder = new SQLiteQueryBuilder();
        sSeriesByIdQueryBuilder.setTables( SeriesContract.SeriesEntry.TABLE_NAME );


    }

    private static final SQLiteQueryBuilder sEpisodesByIdQueryBuilder;

    static{
        sEpisodesByIdQueryBuilder = new SQLiteQueryBuilder();
        sEpisodesByIdQueryBuilder.setTables( SeriesContract.EpisodesEntry.TABLE_NAME );


    }

    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String autorithy = SeriesContract.CONTENT_AUTHORITY;

        matcher.addURI(autorithy, SeriesContract.PATH_SERIES, SERIES);
        matcher.addURI(autorithy, SeriesContract.PATH_SERIES + "/*", SERIES_ID);

        matcher.addURI(autorithy, SeriesContract.PATH_EPISODES, EPISODES);
        matcher.addURI(autorithy, SeriesContract.PATH_EPISODES + "/*", EPISODES_ID);
        matcher.addURI(autorithy, SeriesContract.PATH_EPISODES + "/*", EPISODES_SERIEID);

        return matcher;
    }


    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case SERIES:
                return SeriesContract.SeriesEntry.CONTENT_TYPE;
            case EPISODES:
                return SeriesContract.EpisodesEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    private Cursor getSeriesByID(Uri uri, String[] projection, String sortOrder) {
        String locationSetting = SeriesContract.SeriesEntry.getIDFromUri(uri);


        String[] selectionArgs = new String[]{locationSetting};
        String selection = sSeriesIDSelection;
        return sSeriesByIdQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getEpisodesByID(Uri uri, String[] projection, String sortOrder) {
        String episodeSetting = SeriesContract.EpisodesEntry.getEpisodeIDFromUri(uri);

        String[] selectionArgs = new String[]{episodeSetting};
        String selection = sEpisodesIDSelection;
        return sSeriesByIdQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getEpisodesBySerie(Uri uri, String[] projection, String sortOrder) {
        String episodeSetting = SeriesContract.EpisodesEntry.getSerieIDFromUri(uri);

        String[] selectionArgs = new String[]{episodeSetting};
        String selection = sEpisodesSerieSelection;
        return sSeriesByIdQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case SERIES: {
                    retCursor = mOpenHelper.getReadableDatabase().query(
                            SeriesContract.SeriesEntry.TABLE_NAME,
                            projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case SERIES_ID: {
                retCursor = getSeriesByID(uri, projection, sortOrder);
                break;
            }
            case EPISODES: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        SeriesContract.EpisodesEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case EPISODES_ID: {
                retCursor = getEpisodesByID(uri, projection, sortOrder);
                break;
            }
            case EPISODES_SERIEID: {
                retCursor = getEpisodesBySerie(uri, projection, sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case SERIES: {
                long _id = db.insert(SeriesContract.SeriesEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = SeriesContract.SeriesEntry.buildSeriesUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case EPISODES: {
                long _id = db.insert(SeriesContract.EpisodesEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = SeriesContract.EpisodesEntry.buildEpisodesUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Student: Start by getting a writable database
        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        if (selection == null) selection = "1";
        int count;
        switch (match) {
            case SERIES: {
                count = db.delete(SeriesContract.SeriesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case EPISODES: {
                count = db.delete(SeriesContract.EpisodesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (count > 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }


    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;

        switch (match) {
            case SERIES: {
                count = db.update(SeriesContract.SeriesEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case EPISODES: {
                count = db.update(SeriesContract.EpisodesEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (count > 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

}
