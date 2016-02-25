package ivan.capstone.com.capstone.XML;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ivan.capstone.com.capstone.DataObjects.Serie;

/**
 * Created by Ivan on 25/02/2016.
 */
public class XMLManager {

     private static final String URL = "http://thetvdb.com//banners/";


    public static List<Serie> parseXML(XmlPullParser parser) throws XmlPullParserException,IOException
    {
        ArrayList<Serie> series = null;
        int eventType = parser.getEventType();
        Serie currentSerie = null;
        while (eventType != XmlPullParser.END_DOCUMENT){
            String name = null;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                    series = new ArrayList();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equals("Series") ){
                        currentSerie = new Serie();
                    } else if (currentSerie != null){
                        if (name.equals("seriesid")){
                            currentSerie.setId(parser.nextText());
                        } else if (name.equals("SeriesName")){
                            currentSerie.setName(parser.nextText());
                        } else if (name.equals("Network")){
                            currentSerie.setNetwork(parser.nextText());
                        } else if (name.equals("banner")){
                            currentSerie.setImage_url(URL + parser.nextText());
                        } else if (name.equals("FirstAired")){
                            currentSerie.setDateReleased(parser.nextText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("Series") && currentSerie != null){
                        series.add(currentSerie);
                    }
                    break;
            }
            eventType = parser.next();
        }
        return series;
    }
}
