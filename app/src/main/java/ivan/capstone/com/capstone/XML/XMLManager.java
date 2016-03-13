package ivan.capstone.com.capstone.XML;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ivan.capstone.com.capstone.DataObjects.Actor;
import ivan.capstone.com.capstone.DataObjects.Episode;
import ivan.capstone.com.capstone.DataObjects.Serie;

/**
 * Created by Ivan on 25/02/2016.
 */
public class XMLManager {



    private static final String URL = "http://thetvdb.com//banners/";
    private static final String TAG_SERIES = "Series";
    private static final String TAG_SERIE_ID = "seriesid";
    private static final String TAG_SERIE_NAME = "SeriesName";
    private static final String TAG_SERIE_NETWORK = "Network";
    private static final String TAG_SERIE_BANNER_URL = "banner";
    private static final String TAG_RELEASE_DATE = "FirstAired";
    private static final String TAG_RATING = "Rating";
    private static final String TAG_VOTES = "RatingCount";
    private static final String TAG_SERIE_POSTER_URL = "poster";
    private static final String TAG_SERIE_GENRE = "Genre";
    private static final String TAG_OVERVIEW = "Overview";
    private static final String TAG_ID = "id";
    // This method manage the series in XML format that are brought with the query by name
    public static List<Serie> GetSeriesFromXML(XmlPullParser parser) throws XmlPullParserException,IOException
    {
        ArrayList<Serie> series = null;
        int eventType = parser.getEventType();
        Serie currentSerie = null;
        while (eventType != XmlPullParser.END_DOCUMENT){
            String name;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                    series = new ArrayList();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equals(TAG_SERIES) ){
                        currentSerie = new Serie();
                    } else if (currentSerie != null){
                        if (name.equals(TAG_SERIE_ID)){
                            currentSerie.setId(parser.nextText());
                        } else if (name.equals(TAG_SERIE_NAME)){
                            currentSerie.setName(parser.nextText());
                        } else if (name.equals(TAG_SERIE_NETWORK)){
                            currentSerie.setNetwork(parser.nextText());
                        } else if (name.equals(TAG_SERIE_BANNER_URL)){
                            String banner = parser.nextText();
                            if (!banner.equals((""))) currentSerie.setImage_url(URL + banner);
                        } else if (name.equals(TAG_RELEASE_DATE)){
                            currentSerie.setDateReleased(parser.nextText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase(TAG_SERIES) && currentSerie != null){
                        series.add(currentSerie);
                    }
                    break;
            }
            eventType = parser.next();
        }
        return series;
    }

    public static Serie GetSerieFromXML(XmlPullParser parser) throws XmlPullParserException,IOException
    {
        boolean hasFinishSerie = false;
        int eventType = parser.getEventType();
        Serie serie = new Serie();
        while (eventType != XmlPullParser.END_DOCUMENT && !hasFinishSerie){
            String name;
            switch (eventType){
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equals(TAG_ID)){
                        serie.setId(parser.nextText());
                    } else if (name.equals(TAG_SERIE_NAME)){
                        serie.setName(parser.nextText());
                    } else if (name.equals(TAG_SERIE_NETWORK)){
                        serie.setNetwork(parser.nextText());
                    } else if (name.equals(TAG_SERIE_BANNER_URL)){
                        String banner = parser.nextText();
                        if (!banner.equals((""))) serie.setImage_url(URL + banner);
                    } else if (name.equals(TAG_RELEASE_DATE)){
                        serie.setDateReleased(parser.nextText());
                    }
                    else if (name.equals(TAG_OVERVIEW)){
                        serie.setOverView(parser.nextText());
                    }
                    else if (name.equals(TAG_SERIE_GENRE)){
                        serie.setGenre(parser.nextText());
                    }
                    else if (name.equals(TAG_SERIE_POSTER_URL)){
                        String poster = parser.nextText();
                        if (!poster.equals((""))) serie.setPoster_url(URL + poster);
                    }
                    else if (name.equals(TAG_RATING)){
                        serie.setRating(parser.nextText());
                    }
                    else if (name.equals(TAG_VOTES)){
                        serie.setVotes(parser.nextText());
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase(TAG_SERIES)){
                        hasFinishSerie = true;
                    }
                    break;
            }
            eventType = parser.next();
        }
        return serie;
    }


    private static final String TAG_EPISODES= "Episode";
    private static final String TAG_EPISODE_ID = "id";
    private static final String TAG_EPISODE_SERIEID = "seriesid";
    private static final String TAG_EPISODE_SEASONID = "seasonid";

    private static final String TAG_EPISODE_NUMBER = "EpisodeNumber";
    private static final String TAG_SEASON_NUMBER = "SeasonNumber";

    private static final String TAG_EPISODE_NAME = "EpisodeName";
    private static final String TAG_EPISODE_IMAGE_URL = "filename";

    public static ArrayList<Episode> GetEpisodesFromXML(XmlPullParser parser) throws XmlPullParserException,IOException
    {
        ArrayList<Episode> episodes = new ArrayList();
        int eventType = parser.getEventType();
        Episode currentEpisode = null;
        while (eventType != XmlPullParser.END_DOCUMENT){
            String name;
            switch (eventType){
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equals(TAG_EPISODES) ){
                        currentEpisode = new Episode();
                    } else if (currentEpisode != null){
                        if (name.equals(TAG_EPISODE_ID)){
                            currentEpisode.setEpisode_id(parser.nextText());
                        } else if (name.equals(TAG_EPISODE_SERIEID)){
                            currentEpisode.setSerie_id(parser.nextText());
                        } else if (name.equals(TAG_EPISODE_SEASONID)){
                            currentEpisode.setSeason_id(parser.nextText());
                        } else if (name.equals(TAG_EPISODE_NUMBER)){
                            String number = parser.nextText();
                            currentEpisode.setEpisode_number(Integer.parseInt(number));
                        } else if (name.equals(TAG_SEASON_NUMBER)){
                            String number = parser.nextText();
                            currentEpisode.setSeason_number(Integer.parseInt(number));
                        } else if (name.equals(TAG_EPISODE_NAME)){
                            currentEpisode.setName(parser.nextText());
                        } else if (name.equals(TAG_RELEASE_DATE)){
                            currentEpisode.setDate(parser.nextText());
                        } else if (name.equals(TAG_OVERVIEW)){
                            currentEpisode.setOverview(parser.nextText());
                        } else if (name.equals(TAG_VOTES)){
                            currentEpisode.setVotes(parser.nextText());
                        } else if (name.equals(TAG_RATING)){
                            currentEpisode.setRating(parser.nextText());
                        } else if (name.equals(TAG_EPISODE_IMAGE_URL)) {
                            String image = parser.nextText();
                            if (!image.equals((""))) currentEpisode.setImage_url(URL + image);
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase(TAG_EPISODES) && currentEpisode != null && currentEpisode.getSeason_number() > 0){
                        episodes.add(currentEpisode);
                    }
                    break;
            }
            eventType = parser.next();
        }
        return episodes;
    }

    private static final String TAG_ACTORS = "Actors";
    private static final String TAG_ACTOR = "Actor";
    private static final String TAG_ACTOR_ID = "id";
    private static final String TAG_ACTOR_IMAGE = "Image";
    private static final String TAG_ACTOR_NAME= "Name";
    private static final String TAG_ACTOR_ROLE = "Role";

    // This method manage the series in XML format that are brought with the query by name
    public static ArrayList<Actor> GetActorsFromXML(XmlPullParser parser, String serie_id) throws XmlPullParserException,IOException
    {
        ArrayList<Actor> actors = new ArrayList();
        int eventType = parser.getEventType();
        Actor currentActor = null;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name;
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equals(TAG_ACTOR)) {
                        currentActor = new Actor();
                        currentActor.setSerie_id(serie_id);
                    } else if (currentActor != null) {
                        if (name.equals(TAG_ACTOR_ID)) {
                            currentActor.setActor_id(parser.nextText());
                        } else if (name.equals(TAG_ACTOR_NAME)) {
                            currentActor.setName(parser.nextText());
                        } else if (name.equals(TAG_ACTOR_ROLE)) {
                            currentActor.setRole(parser.nextText());
                        } else if (name.equals(TAG_ACTOR_IMAGE)) {
                            String image = parser.nextText();
                            if (!image.equals((""))) currentActor.setImage_url(URL + image);
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase(TAG_ACTOR) && currentActor != null && !currentActor.getActor_id().equals("")) {
                        actors.add(currentActor);
                    }
                    break;
            }
            eventType = parser.next();
        }
        return actors;
    }

}
