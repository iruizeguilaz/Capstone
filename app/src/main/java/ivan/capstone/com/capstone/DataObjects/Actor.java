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
public class Actor implements Parcelable {

    private long _id;
    private String serie_id;
    private String actor_id;
    private String name;
    private String role;
    private String image_url;


    public Actor() {
        _id = 0;
        serie_id = "";
        actor_id = "";
        name = "";
        role = "";
        image_url = "";
    }

    protected Actor(Parcel in) {
        _id = in.readLong();
        serie_id = in.readString();
        actor_id = in.readString();
        name = in.readString();
        role = in.readString();
        image_url = in.readString();
    }

    public Actor(int _id, String serie_id, String actor_id, String name, String role, String image_url) {
        this._id = _id;
        this.serie_id = serie_id;
        this.actor_id = actor_id;
        this.name = name;
        this.role = role;
        this.image_url= image_url;
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(_id);
        dest.writeString(serie_id);
        dest.writeString(actor_id);
        dest.writeString(name);
        dest.writeString(role);
        dest.writeString(image_url);
    }


    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Actor> CREATOR = new Parcelable.Creator<Actor>() {
        @Override
        public Actor createFromParcel(Parcel in) {
            return new Actor(in);
        }

        @Override
        public Actor[] newArray(int size) {
            return new Actor[size];
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

    public String getActor_id() {
        return actor_id;
    }

    public void setActor_id(String actor_id) {
        this.actor_id = actor_id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public boolean IsSaved() {
        if (actor_id.equals("")) return false;
        Cursor seriesCursor = MyApplication.getContext().getContentResolver().query(
                SeriesContract.ActorsEntry.CONTENT_URI,
                new String[]{SeriesContract.EpisodesEntry._ID},
                SeriesContract.ActorsEntry.COLUMN_ACTOR_ID + " = ?",
                new String[]{actor_id},
                null);
        if (seriesCursor != null && seriesCursor.moveToFirst()) return true;
        else return false;
    }


    public void Save() {
        if (!IsSaved()) {
            ContentValues locationValues = new ContentValues();
            // Then add the data, along with the corresponding name of the data type,
            // so the content provider knows what kind of value is being inserted.
            locationValues.put(SeriesContract.ActorsEntry.COLUMN_ACTOR_ID, actor_id);
            locationValues.put(SeriesContract.ActorsEntry.COLUMN_SERIE_ID, serie_id);
            locationValues.put(SeriesContract.ActorsEntry.COLUMN_NAME, name);
            locationValues.put(SeriesContract.ActorsEntry.COLUMN_ROLE, role);
            locationValues.put(SeriesContract.ActorsEntry.COLUMN_IMAGE_URL, image_url);

            // Finally, insert episode data into the database.
            Uri insertedUri = MyApplication.getContext().getContentResolver().insert(
                    SeriesContract.ActorsEntry.CONTENT_URI,
                    locationValues
            );
            _id = ContentUris.parseId(insertedUri);
        }
    }

    public void Update() {
        if (!IsSaved()) {
            ContentValues locationValues = new ContentValues();
            // Then add the data, along with the corresponding name of the data type,
            // so the content provider knows what kind of value is being inserted.
            locationValues.put(SeriesContract.ActorsEntry.COLUMN_ACTOR_ID, actor_id);
            locationValues.put(SeriesContract.ActorsEntry.COLUMN_SERIE_ID, serie_id);
            locationValues.put(SeriesContract.ActorsEntry.COLUMN_NAME, name);
            locationValues.put(SeriesContract.ActorsEntry.COLUMN_ROLE, role);
            locationValues.put(SeriesContract.ActorsEntry.COLUMN_IMAGE_URL, image_url);

            // Finally, insert episode data into the database.
            MyApplication.getContext().getContentResolver().update(
                    SeriesContract.ActorsEntry.CONTENT_URI,
                    locationValues,  SeriesContract.ActorsEntry.COLUMN_ACTOR_ID +"=?", new String[]{actor_id}
            );

        }
    }

    public void Delete() {
        if (IsSaved()) {
            MyApplication.getContext().getContentResolver().delete(SeriesContract.ActorsEntry.CONTENT_URI, SeriesContract.ActorsEntry.COLUMN_ACTOR_ID +"=?", new String[]{actor_id});
            _id = 0;
        }
    }
}
