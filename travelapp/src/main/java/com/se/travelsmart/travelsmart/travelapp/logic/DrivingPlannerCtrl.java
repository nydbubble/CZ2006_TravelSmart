package com.se.travelsmart.travelsmart.travelapp.logic;

import android.content.Context;
import android.util.Log;


import com.se.travelsmart.travelsmart.travelapp.entity.IncidentSet;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;


// NTU
// Lat: 1.3483, Long: 103.6831

// Bedok CC
// Lat: 1.324349, Long: 103.93589900000006

public class DrivingPlannerCtrl implements GoogleMapsTaskInterface {

    // Format output can be in json or xml
    //private static final String strRequestHead = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String strRequestHead = "https://maps.googleapis.com/maps/api/directions/xml?";
    private static final String strKey = "AIzaSyAxvyEnqTMEhgvysBQblpQse4MmApQ1ch0";


    public DrivingPlannerCtrl(Context context) {

    }

    public DrivingPlannerCtrl(String source, String destination) {
        String userOrigin = source;
        String userDestination = destination;
    }

    public void startDefaultRoute() {
        String strOrigin = "origin=" + "1.3483,103.6831";
        String strDestination = "destination=" + "1.324349,103.93589900000006";
        String strRequest = strRequestHead + "&" + strOrigin + "&" +
                strDestination + "&" + strKey;
        sendGoogleRequest(strRequest);
    }

    private void sendGoogleRequest(String request) {
        GoogleMaps_API googleMapsAPI = new GoogleMaps_API(this);
        googleMapsAPI.execute(request);
    }

    @Override
    public void onGoogleTaskComplete(Document doc) {
        if (doc != null) {
            NodeList nodeList = doc.getElementsByTagName("overview_polyline");
            Node node = nodeList.item(0);
            Log.e("TravelSmart", "Points: " + node.getTextContent());

        } else {
            Log.e("TravelSmart", "Returned doc is empty omg");
        }

    }




}