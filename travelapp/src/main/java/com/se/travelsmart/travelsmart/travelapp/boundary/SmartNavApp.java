package com.se.travelsmart.travelsmart.travelapp.boundary;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.se.travelsmart.travelsmart.travelapp.R;

import com.se.travelsmart.travelsmart.travelapp.entity.IncidentSet;
import com.se.travelsmart.travelsmart.travelapp.logic.DrivingPlannerCtrl;
import com.se.travelsmart.travelsmart.travelapp.logic.LTATaskInterface;
import com.se.travelsmart.travelsmart.travelapp.logic.LTA_API;

import java.util.ArrayList;


public class SmartNavApp extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMapLongClickListener, View.OnClickListener, LTATaskInterface {

    private GoogleMap mMap;
    private ToggleButton btnMode;
    private Button btnGo, btnUI, btnLTA;
    private EditText txtDestination;
    private boolean isUIVisible = true;
    private boolean isLTAUpdating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        initialiseUIVariables();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initialiseUIVariables() {
        txtDestination = findViewById(R.id.txtDestination);
        btnGo = findViewById(R.id.btnGo);
        btnMode = findViewById(R.id.btnMode);
        btnUI = findViewById(R.id.btnUI);
        btnLTA = findViewById(R.id.btnLTA);

        btnGo.setOnClickListener(this);
        btnMode.setOnClickListener(this);
        btnUI.setOnClickListener(this);
        btnLTA.setOnClickListener(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(1.3483, 103.6831);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15.0f));
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        //Clearing all the markers
        mMap.clear();

        //Adding a new marker to the current pressed position we are also making the draggable true
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(true));
    }

    @Override
    public void onClick(View view) {
        // No longer using switch, ID are no longer stored as final constants
        int i = view.getId();
        if (i == R.id.btnGo) {
            if (btnMode.isChecked()) {
                // True for Private Mode
                Toast.makeText(this, "Private Navigation", Toast.LENGTH_SHORT).show();

                DrivingPlannerCtrl drivingPlannerCtrl = new DrivingPlannerCtrl(this);
                drivingPlannerCtrl.startDefaultRoute();
            } else {
                // False for Public Mode
                Toast.makeText(this, "Public Navigation", Toast.LENGTH_SHORT).show();

            }


        } else if (i == R.id.btnMode) {
            if (btnMode.isChecked()) {
                Toast.makeText(this, "Change to Private", Toast.LENGTH_SHORT).show();
                // False for Public Mode
                btnMode.setChecked(true);
                btnMode.setText(R.string.txtPrivate);
            } else {
                Toast.makeText(this, "Change to Public", Toast.LENGTH_SHORT).show();
                // True for Private Mode
                btnMode.setChecked(false);
                btnMode.setText(R.string.txtPublic);
            }

        } else if (i == R.id.btnUI) {
            if (isUIVisible) {
                txtDestination.setVisibility(View.INVISIBLE);
                btnGo.setVisibility(View.INVISIBLE);
                btnMode.setVisibility(View.INVISIBLE);
                isUIVisible = false;
                btnUI.setText(R.string.txtUIHide);
            } else {
                txtDestination.setVisibility(View.VISIBLE);
                btnGo.setVisibility(View.VISIBLE);
                btnMode.setVisibility(View.VISIBLE);
                isUIVisible = true;
                btnUI.setText(R.string.txtUIShow);
            }
        } else if (i == R.id.btnLTA) {
            if (isLTAUpdating == false){
                isLTAUpdating = true;
                LTA_API lta_api = new LTA_API(this);
                lta_api.execute();
            } else {
                isLTAUpdating = false;
            }
        }
    }


    @Override
    public void onLTATaskComplete(ArrayList<IncidentSet> listIncidentSet) {
        Toast.makeText(this,"LTA Fetch Complete!",Toast.LENGTH_SHORT).show();
        Log.e("TravelSmart","listIncidentSet: " + listIncidentSet);
    }
}
