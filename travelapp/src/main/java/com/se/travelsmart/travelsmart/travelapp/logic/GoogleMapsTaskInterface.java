package com.se.travelsmart.travelsmart.travelapp.logic;

import org.w3c.dom.Document;

/**
 * This interface serves as a callback when the asych task is done
 * to pass the document object back from
 * GoogleMaps_API to DrivingPlannerCtrl
 */

public interface GoogleMapsTaskInterface {
    void onGoogleTaskComplete(Document doc);
}
