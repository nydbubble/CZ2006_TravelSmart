package com.se.travelsmart.travelsmart.travelapp.logic;


import android.os.AsyncTask;
import android.util.Log;

import com.se.travelsmart.travelsmart.travelapp.boundary.SmartNavApp;
import com.se.travelsmart.travelsmart.travelapp.entity.IncidentSet;

import org.json.JSONObject;
import org.json.JSONString;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class LTA_API extends
        AsyncTask<String, Integer, ArrayList<IncidentSet>> {

    private ArrayList<IncidentSet> listIncidentSet;

    private LTATaskInterface ltaTaskInterfaceCallback;

    // private static int skip = 0;
    private static final String INCIDENT_SET_URL_STRING = "http://datamall2.mytransport.sg/ltaodataservice/TrafficIncidents?";

    public LTA_API(SmartNavApp context) {
        this.ltaTaskInterfaceCallback = context;
        listIncidentSet = new ArrayList<IncidentSet>();
    }

    @Override
    protected ArrayList<IncidentSet> doInBackground(String... url) {

        try {

            return processIncidentSetData(downloadUrl(INCIDENT_SET_URL_STRING));
        } catch (IOException e) {
            return null;
        }

    }

    @Override
    protected void onPostExecute(ArrayList<IncidentSet> result) {
        // callback to main thread
        ltaTaskInterfaceCallback.onLTATaskComplete(result);
    }

    /**
     * @param passedData Uses the XML Data passed in as parameters Then separate them
     *                   into their individual data sets
     * @return ArrayList with Incident Data Sets.
     * <p>
     * Collates all the data sets and saves them into an ArrayList and
     * returns it to the caller
     */
    private ArrayList<IncidentSet> processIncidentSetData(String passedData) {
        // listIncidentSet = new ArrayList<IncidentSet>();
        listIncidentSet.clear();
        /*
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(passedData);
            Log.e("TravelSmart", "JSONObject: " + jsonObj);
            Log.e("TravelSmart", "jsonObj (Type): " + jsonObj.get("value"));
        } catch (Throwable t) {
            Log.e("TravelSmart", "Something went wrong with JSON !" + t.toString());
        }
        */

        StringBuilder sBuilder = new StringBuilder(passedData);
        StringBuilder sbIncident;
        int start, end;
        String[] incident;

        while (sBuilder.length() > 1) {
            // Log.e("TravelSmart","Builder length: "+sBuilder.length());
            // Log.e("TravelSmart","Builder content:");
            start = sBuilder.indexOf("{");
            end = sBuilder.indexOf("}");

            // Split into the 4 parts
            incident = sBuilder.substring(start, end + 1).split(",");

            // Type
            sbIncident = new StringBuilder(incident[0]);
            incident[0] = sbIncident.substring(9, sbIncident.length() - 1);

            // Latitude
            sbIncident = new StringBuilder(incident[1]);
            incident[1] = sbIncident.substring(11, sbIncident.length() - 1);

            // Longitude
            sbIncident = new StringBuilder(incident[2]);
            incident[2] = sbIncident.substring(12, sbIncident.length() - 1);

            // Message
            sbIncident = new StringBuilder(incident[3]);
            incident[3] = sbIncident.substring(11, sbIncident.length() - 1);


            IncidentSet is = new IncidentSet(incident[0], incident[1], incident[2], incident[2]);
            listIncidentSet.add(is);

            // Delete closing curly bracket and comma
            sBuilder.delete(0, end + 2);
        }

        Log.e("TravelSmart", "IncidentSet Size: " + listIncidentSet.size());
        Log.e("TravelSmart", "First Index: " + listIncidentSet.get(0).toString());
        return listIncidentSet;
    }

    /**
     * Sets up the Internet connection to the LTA Server to download the data as
     * XML After which, has to be formatted from XML to String The data is
     * String format then can be processed into individual data Sets by the
     * calling method
     * <p>
     * This method is used to download all LTA data, therefore caller has to
     * specify the correct download URL
     *
     * @param pURL The URL given as the parameters is used to connect to the LTA
     *             Server, it also contains what kind of data set is been
     *             requested
     * @return string
     * @throws IOException ioException
     */
    private String downloadUrl(String pURL) throws IOException {
        Log.e("TravelSmart", "downloadURL: " + pURL);
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        try {
            URL url = new URL(pURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);

            // Uses GET instead of POST, as following the LTA API Guide
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setRequestProperty("accept", "*/*");

			/* This is where the Account Key and ID sent by LTA is used */
            conn.addRequestProperty("AccountKey", "03S66ZLawoGZQmIop3xwLA==");
            conn.addRequestProperty("UniqueUserID",
                    "29db986e-c31d-42f7-babc-5c38946a61f9");
            conn.connect(); // Starts the query

            // int response = conn.getResponseCode();
            is = conn.getInputStream();

            Log.e("TravelSmart", "inputStream: " + is.toString());
            return streamToString(is);

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }

        }
    }

    /**
     * @param stream Puts the InputStream into a Reader Which outputs the contents
     *               as Strings
     * @return XML in String
     * <p>
     * Constructed into one long String and returned to caller
     * @throws IOException                  If the connection to the server has issue, throw IOException
     * @throws UnsupportedEncodingException Note: This method parses the incoming data are cannot be used
     *                                      before converting into Strings, which can be used. Another
     *                                      method is required to process the data in String format,
     *                                      processing methods are below.
     */
    private String streamToString(InputStream stream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        // InputStreamReader reader = new InputStreamReader(stream, "UTF-8");

        String line = null;
        StringBuilder sBuilder = new StringBuilder();

        while ((line = br.readLine()) != null) {
            sBuilder.append(line);
        }
        Log.e("TravelSmart", "sBuilder before: " + sBuilder.toString());
        // Remove the starting link sentence, leave only JSON data
        sBuilder.delete(0, 99);
        Log.e("TravelSmart", "sBuilder after:  " + sBuilder.toString());
        return sBuilder.toString();

    }


}