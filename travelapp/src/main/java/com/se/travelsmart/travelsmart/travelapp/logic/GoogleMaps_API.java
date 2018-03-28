package com.se.travelsmart.travelsmart.travelapp.logic;

import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class GoogleMaps_API extends AsyncTask<String, Integer, Document> {

    private GoogleMapsTaskInterface googleTaskCallback;

    public GoogleMaps_API(DrivingPlannerCtrl context) {
        this.googleTaskCallback = context;
    }

    protected Document doInBackground(String... urls) {

        try {
            Log.e("TravelSmart", "Try send google request: " + urls[0]);
            URL googleRequestURL = new URL(urls[0]);

            HttpURLConnection urlConnection = (HttpURLConnection) googleRequestURL.openConnection();
            InputStream is = urlConnection.getInputStream();
            int status = urlConnection.getResponseCode();

            Log.e("TravelSmart", "Google URL connection status: " + status);

            DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();

            return builder.parse(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(Document doc) {
        if (doc != null) {
            Log.e("TravelSmart", doc.toString());
            googleTaskCallback.onGoogleTaskComplete(doc);
        } else {
            Log.e("TravelSmart", "doc is empty omg");
        }


    }
}
