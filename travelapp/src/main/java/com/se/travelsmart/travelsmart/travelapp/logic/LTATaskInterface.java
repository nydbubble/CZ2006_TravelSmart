package com.se.travelsmart.travelsmart.travelapp.logic;

import com.se.travelsmart.travelsmart.travelapp.entity.IncidentSet;

import java.util.ArrayList;

/**
 * Created by Tai on 27/3/2018.
 */

public interface LTATaskInterface {
    void onLTATaskComplete(ArrayList<IncidentSet> listIncidentSet);
}
