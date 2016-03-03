package ivan.capstone.com.capstone.DataObjects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ivan on 19/02/2016.
 */
public class Serie implements Parcelable {

    private String id;
    private String name;
    private String image_url;
    private String overView;
    private String dateReleased;
    private String network;
    private String rating;
    private String genre;
    private String poster_url;

    public Serie(){

    }

    protected Serie(Parcel in) {
        id = in.readString();
        name = in.readString();
        image_url = in.readString();
        overView = in.readString();
        dateReleased = in.readString();
        network = in.readString();
        rating = in.readString();
        genre = in.readString();
        poster_url = in.readString();
    }

    public Serie(String id, String name, String image_url, String overView, String dateReleased, String network, String rating, String genre, String poster_url) {

        this.id = id;
        this.name = name;
        this.image_url = image_url;
        this.overView = overView;
        this.dateReleased = dateReleased;
        this.network = network;
        this.rating = rating;
        this.genre = genre;
        this.poster_url = poster_url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(image_url);
        dest.writeString(overView);
        dest.writeString(dateReleased);
        dest.writeString(network);
        dest.writeString(rating);
        dest.writeString(genre);
        dest.writeString(poster_url);
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
}
