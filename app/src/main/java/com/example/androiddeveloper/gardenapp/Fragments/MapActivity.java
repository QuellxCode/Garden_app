package com.example.androiddeveloper.gardenapp.Fragments;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.service.BeaconManager;
import com.example.androiddeveloper.gardenapp.LocationAlertIntentService;
import com.example.androiddeveloper.gardenapp.Models.NotifyLatLng;
import com.example.androiddeveloper.gardenapp.Models.Places;
import com.example.androiddeveloper.gardenapp.R;
import com.example.androiddeveloper.gardenapp.mHandler;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends Fragment implements OnMapReadyCallback {
    private static final int MAX_SIZE = 1024;
    // private GoogleMap mMap;
    ArrayList<Places> places;
    PolylineOptions options1;
    PolylineOptions options2;
    PolylineOptions options3;
    ArrayList<LatLng> route1;
    ArrayList<LatLng> route2;
    ArrayList<LatLng> route3;
    private static final int LOC_PERM_REQ_CODE = 1;
    //meters
    private static final int GEOFENCE_RADIUS = 100;
    //in milli seconds
    private static final int GEOFENCE_EXPIRATION = 6000;

    private GoogleMap mMap;
    boolean isshowing = false;
    private GeofencingClient geofencingClient;
    LocationListener lis;
    TextView loc;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LatLng currentlatlng;
    MarkerOptions markerOptions;
    private BeaconManager beaconManager;
    private BeaconRegion region;


    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MyInfoWindowAdapter() {
            myContentsView = getLayoutInflater().inflate(R.layout.custom_info_contents, null);

        }

        @Override
        public View getInfoContents(Marker marker) {


            TextView tvTitle = ((TextView) myContentsView.findViewById(R.id.title));
            tvTitle.setText(marker.getTitle());
            TextView tvSnippet = ((TextView) myContentsView.findViewById(R.id.snippet));
            tvSnippet.setText(marker.getSnippet());
            int a = Integer.parseInt(marker.getSnippet());
            ImageView ivIcon = ((ImageView) myContentsView.findViewById(R.id.icon));
            ivIcon.setImageDrawable(getResources().getDrawable(a));
            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_map, container, false);
        Button selectRoute = (Button) rootView.findViewById(R.id.selectRoute);
        selectRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRadioButtonDialog();
            }
        });
      /*  final Button showplaces = (Button) rootView.findViewById(R.id.showPlaces);
        showplaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isshowing==false){
                for (int i = 0; i < places.size(); i++) {
                    LatLng locallatLng = new LatLng(places.get(i).plat, places.get(i).plng);

                    markerOptions= new MarkerOptions().position(locallatLng).title(places.get(i).pname).snippet(String.valueOf(places.get(i).pimg));
                    mMap.addMarker(markerOptions);

                }
                showplaces.setVisibility(View.GONE);
                isshowing = true;
                }
                else{

                    mMap.addMarker(markerOptions);
                    showplaces.setText("Show Places");
                    isshowing = false;
                }
            }
        });*/

        // loc= (TextView) rootView.findViewById(R.id.loc);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // mapFragment.getMapAsync(this);
        geofencingClient = LocationServices.getGeofencingClient(getActivity());


        return rootView;


    }

    @Override
    public void onPause() {
        super.onPause();


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final LatLng map_center_position = new LatLng(51.314841, -0.475654);


        Drawable Image = null;


        prepareMarkers();


      /*  ArrayList<LatLng> route1 = new ArrayList<>();
        route1.add(new LatLng(places.get(20).plat, places.get(20).plng));
        route1.add(new LatLng(places.get(27).plat, places.get(27).plng));
        route1.add(new LatLng(places.get(26).plat, places.get(26).plng));
        route1.add(new LatLng(places.get(29).plat, places.get(29).plng));
        route1.add(new LatLng(places.get(20).plat, places.get(20).plng)); */
        route1 = new ArrayList<>();
        route1.add(new LatLng(51.313261, -0.472465));
        route1.add(new LatLng(51.313468, -0.473050));
        route1.add(new LatLng(51.313404, -0.473778));
        route1.add(new LatLng(51.313303, -0.474464));
        route1.add(new LatLng(51.312938, -0.474378));
        route1.add(new LatLng(51.313017, -0.473774));
        route1.add(new LatLng(51.312449, -0.473549));
        route1.add(new LatLng(51.312363, -0.474236));
        route1.add(new LatLng(51.312642, -0.474327));
        route1.add(new LatLng(51.312461, -0.475927));
        route1.add(new LatLng(51.313129, -0.476095));
        route1.add(new LatLng(51.313052, -0.476528));
        route1.add(new LatLng(51.312989, -0.477060));
        route1.add(new LatLng(51.312850, -0.477511));
        route1.add(new LatLng(51.312399, -0.477301));
        route1.add(new LatLng(51.312119, -0.477102));
        route1.add(new LatLng(51.312060, -0.477013));
        route1.add(new LatLng(51.311925, -0.477388));
        route1.add(new LatLng(51.311866, -0.477526));
        route1.add(new LatLng(51.312060, -0.477013));
        route1.add(new LatLng(51.311645, -0.475860));

      /*  ArrayList<LatLng> route2 = new ArrayList<>();
        route2.add(new LatLng(places.get(20).plat, places.get(20).plng));
        route2.add(new LatLng(places.get(11).plat, places.get(11).plng));
        route2.add(new LatLng(places.get(10).plat, places.get(10).plng));
        route2.add(new LatLng(places.get(9).plat, places.get(9).plng));
        route2.add(new LatLng(places.get(7).plat, places.get(7).plng));
        route2.add(new LatLng(places.get(5).plat, places.get(5).plng));*/

        route2 = new ArrayList<>();
        route2.add(new LatLng(51.313261, -0.472465));
        route2.add(new LatLng(51.313478, -0.473034));
        route2.add(new LatLng(51.313737, -0.473117));
        route2.add(new LatLng(51.313735, -0.473338));
        route2.add(new LatLng(51.313782, -0.473522));
        route2.add(new LatLng(51.313910, -0.473564));
        route2.add(new LatLng(51.313849, -0.474144));
        route2.add(new LatLng(51.313757, -0.475028));
        route2.add(new LatLng(51.313869, -0.475059));
        route2.add(new LatLng(51.313743, -0.475881));
        route2.add(new LatLng(51.313526, -0.477246));
        route2.add(new LatLng(51.313335, -0.477873));
        route2.add(new LatLng(51.313301, -0.478165));
        route2.add(new LatLng(51.313523, -0.477738));
        route2.add(new LatLng(51.313740, -0.477533));
        route2.add(new LatLng(51.314017, -0.477494));
        route2.add(new LatLng(51.314099, -0.477548));
        route2.add(new LatLng(51.314159, -0.477678));
        route2.add(new LatLng(51.314214, -0.477808));
        route2.add(new LatLng(51.314299, -0.477839));
        route2.add(new LatLng(51.314404, -0.477855));
        route2.add(new LatLng(51.314505, -0.477849));
        route2.add(new LatLng(51.314481, -0.478053));
        route2.add(new LatLng(51.314505, -0.477849));
        route2.add(new LatLng(51.314811, -0.477852));
        route2.add(new LatLng(51.314839, -0.478118));
        route2.add(new LatLng(51.315023, -0.477988));
        route2.add(new LatLng(51.314883, -0.477686));
        route2.add(new LatLng(51.314472, -0.477538));
        route2.add(new LatLng(51.314532, -0.477231));
        route2.add(new LatLng(51.314604, -0.476982));
        route2.add(new LatLng(51.314692, -0.476775));
        route2.add(new LatLng(51.314793, -0.476425));
        route2.add(new LatLng(51.314837, -0.476176));
        route2.add(new LatLng(51.314891, -0.475894));
        route2.add(new LatLng(51.314799, -0.475442));
        route2.add(new LatLng(51.314708, -0.475362));
        route2.add(new LatLng(51.314625, -0.475263));
        route2.add(new LatLng(51.314460, -0.475133));
        route2.add(new LatLng(51.314335, -0.475044));
        route2.add(new LatLng(51.314382, -0.474873));
        route2.add(new LatLng(51.314415, -0.474491));
        route2.add(new LatLng(51.314394, -0.474132));
        route2.add(new LatLng(51.314360, -0.473867));
        route2.add(new LatLng(51.314269, -0.473808));



       /* ArrayList<LatLng> route3 = new ArrayList<>();
        route3.add(new LatLng(places.get(20).plat, places.get(20).plng));
        route3.add(new LatLng(places.get(18).plat, places.get(18).plng));
        route3.add(new LatLng(places.get(17).plat, places.get(17).plng));
        route3.add(new LatLng(places.get(13).plat, places.get(13).plng));
        route3.add(new LatLng(places.get(23).plat, places.get(23).plng));
        route3.add(new LatLng(places.get(22).plat, places.get(22).plng));
        route3.add(new LatLng(places.get(21).plat, places.get(21).plng));
        route3.add(new LatLng(places.get(24).plat, places.get(24).plng));*/
        route3 = new ArrayList<>();
        route3.add(new LatLng(51.313261, -0.472465));
        route3.add(new LatLng(51.313478, -0.473034));
        route3.add(new LatLng(51.313737, -0.473117));
        route3.add(new LatLng(51.313735, -0.473338));
        route3.add(new LatLng(51.313782, -0.473522));
        route3.add(new LatLng(51.313910, -0.473564));
        route3.add(new LatLng(51.313849, -0.474144));
        route3.add(new LatLng(51.313757, -0.475028));
        route3.add(new LatLng(51.313786, -0.475872));
        route3.add(new LatLng(51.314136, -0.476106));
        route3.add(new LatLng(51.314262, -0.476255));
        route3.add(new LatLng(51.314599, -0.476277));
        route3.add(new LatLng(51.314876, -0.476236));
        route3.add(new LatLng(51.314853, -0.476059));
        route3.add(new LatLng(51.314802, -0.475686));
        route3.add(new LatLng(51.314771, -0.475364));
        route3.add(new LatLng(51.314696, -0.474851));
        route3.add(new LatLng(51.314663, -0.474464));
        route3.add(new LatLng(51.314560, -0.474297));
        route3.add(new LatLng(51.314399, -0.474070));
        route3.add(new LatLng(51.314363, -0.473840));
        route3.add(new LatLng(51.314749, -0.473633));
        route3.add(new LatLng(51.314975, -0.473616));
        route3.add(new LatLng(51.315095, -0.473706));
        route3.add(new LatLng(51.315352, -0.473792));
        route3.add(new LatLng(51.315755, -0.473946));
        route3.add(new LatLng(51.315667, -0.473661));


        GroundOverlayOptions newarkMap;

        if (Build.VERSION.SDK_INT > 23) {
            newarkMap = new GroundOverlayOptions()
                    .image(BitmapDescriptorFactory.fromResource(R.drawable.nougatmap)).bearing(-10)
                    .position(map_center_position, 990f, 1600f);

        } else {


            try {
                Image = createLargeDrawable(R.drawable.mapnoback);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap Image_bitmap = drawableToBitmap(Image);
            newarkMap = new GroundOverlayOptions()
                    .image(BitmapDescriptorFactory.fromBitmap(Image_bitmap)).bearing(-10)
                    .position(map_center_position, 990f, 1600f);
        }
        mMap.setOnGroundOverlayClickListener(new GoogleMap.OnGroundOverlayClickListener() {
            @Override
            public void onGroundOverlayClick(GroundOverlay groundOverlay) {
                groundOverlay.getPosition();
            }
        });


        mMap.addGroundOverlay(newarkMap);

        final LatLngBounds latLngBounds = new LatLngBounds(
                new LatLng(51.307489, -0.482965), new LatLng(51.321919, -0.469704));

        CameraPosition cameraPosition = new CameraPosition.Builder().
                target(map_center_position).
                build();

        options1 = new PolylineOptions().width(6).color(Color.BLUE).geodesic(true);
        options2 = new PolylineOptions().width(6).color(Color.CYAN).geodesic(true);
        options3 = new PolylineOptions().width(6).color(Color.DKGRAY).geodesic(true);
        for (int z = 0; z < route1.size(); z++) {
            LatLng point = new LatLng(route1.get(z).latitude, route1.get(z).longitude);
            options1.add(point);
        }
        for (int z = 0; z < route2.size(); z++) {
            LatLng point = new LatLng(route2.get(z).latitude, route2.get(z).longitude);
            options2.add(point);
        }
        for (int z = 0; z < route3.size(); z++) {
            LatLng point = new LatLng(route3.get(z).latitude, route3.get(z).longitude);
            options3.add(point);
        }

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        // mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition[0]));
        mMap.setMinZoomPreference(14.5f);
        mMap.setMaxZoomPreference(17.5f);
        mMap.setLatLngBoundsForCameraTarget(latLngBounds);

        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
        // mMap.setMyLocationEnabled(true);
        //     mMap.setMapType(MAP_TYPE_NONE);

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {


                if (marker.getTitle().equalsIgnoreCase("Laboratory")) {


                    String url = "https://www.rhs.org.uk/gardens/wisley/garden-highlights/entrance-and-jellicoe-canal";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);

                } else if (marker.getTitle().equalsIgnoreCase("Model Gardens")) {


                    String url = "https://www.rhs.org.uk/gardens/wisley/garden-highlights/the-exotic-garden";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);

                } else if (marker.getTitle().equalsIgnoreCase("GlassHouse Borders")) {

                    String url = "https://www.rhs.org.uk/gardens/wisley/garden-highlights/glasshouse-borders";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);

                } else if (marker.getTitle().equalsIgnoreCase("Seven Acres")) {


                    String url = "https://www.rhs.org.uk/gardens/wisley/garden-highlights/seven-acres";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);

                } else if (marker.getTitle().equalsIgnoreCase("Battleston Hill")) {


                    String url = "https://www.rhs.org.uk/gardens/wisley/garden-highlights/battleston-hill?ext=";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);

                } else if (marker.getTitle().equalsIgnoreCase("Battleston Hill East")) {


                    String url = "https://www.rhs.org.uk/gardens/wisley/garden-highlights/battleston-hill?ext=";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);

                } else if (marker.getTitle().equalsIgnoreCase("Glass House Cafe")) {


                    String url = "https://www.rhs.org.uk/gardens/wisley/garden-highlights/the-glasshouse";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);

                } else if (marker.getTitle().equalsIgnoreCase("Trial Fields")) {


                    String url = "https://www.rhs.org.uk/gardens/wisley/garden-highlights/the-trials-field";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);

                } else if (marker.getTitle().equalsIgnoreCase("Fruit Garden")) {


                    String url = "https://www.rhs.org.uk/gardens/wisley/garden-highlights/vegetable-fruit-herb-gardens";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);

                } else if (marker.getTitle().equalsIgnoreCase("Herb Garden")) {


                    String url = "https://www.rhs.org.uk/gardens/wisley/garden-highlights/vegetable-fruit-herb-gardens";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);

                } else if (marker.getTitle().equalsIgnoreCase("Mixed Borders")) {


                    String url = "https://www.rhs.org.uk/gardens/wisley/garden-highlights/mixed-borders";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);

                } else if (marker.getTitle().equalsIgnoreCase("Rose Garden")) {


                    String url = "https://www.rhs.org.uk/gardens/wisley/garden-highlights/bowes-lyon-rose-garden";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);

                } else if (marker.getTitle().equalsIgnoreCase("Orchard")) {


                    String url = "https://www.rhs.org.uk/gardens/wisley/garden-highlights/the-orchard";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);

                } else if (marker.getTitle().equalsIgnoreCase("The PineTum")) {


                    String url = "https://www.rhs.org.uk/gardens/wisley/garden-highlights/the-pinetum";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);

                } else if (marker.getTitle().equalsIgnoreCase("Alpine House")) {


                    String url = "https://www.rhs.org.uk/gardens/wisley/garden-highlights/the-alpine-meadow";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);

                }
            }
        });

        //
/*
        for (int i = 0; i < places.size(); i++) {
            LatLng locallatLng = new LatLng(places.get(i).plat, places.get(i).plng);
            mMap.addMarker(new MarkerOptions().position(locallatLng).title(places.get(i).pname).snippet(String.valueOf(places.get(i).pimg)));

        }*/

        //  mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMinZoomPreference(15);

        showCurrentLocationOnMap();

    /*    mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
            //    loc.setText(latLng.latitude+","+ latLng.longitude);
                addLocationAlert(latLng.latitude, latLng.longitude);
            }
        });*/
        removeLocationAlert();
        mHandler.notLatlng = new ArrayList<>();
        mHandler.notLatlng.add(new NotifyLatLng(51.313468, -0.473050, "You are now passing Wisley Library"));
        mHandler.notLatlng.add(new NotifyLatLng(51.312938, -0.474378, "You are now approaching the Model Garden"));
        mHandler.notLatlng.add(new NotifyLatLng(51.312363, -0.474236, "On your right-hand side, you have County Garden"));
        mHandler.notLatlng.add(new NotifyLatLng(51.313129, -0.476095, "On your right hand side you will now be approaching the Rock Garden"));
        mHandler.notLatlng.add(new NotifyLatLng(51.311925, -0.477388, "In about 10 yards, we will be approaching to fruit garden"));
        mHandler.notLatlng.add(new NotifyLatLng(51.311645, -0.475860, "We are approaching The sausage café, where our tour ends"));
        mHandler.notLatlng.add(new NotifyLatLng(51.313735, -0.473338, "On your right approaching canal"));
        mHandler.notLatlng.add(new NotifyLatLng(51.313849, -0.474144, "In 10 metres you will be approaching oakwood"));
        mHandler.notLatlng.add(new NotifyLatLng(51.313743, -0.475881, "You are now passing by oakwood"));
        mHandler.notLatlng.add(new NotifyLatLng(51.313301, -0.478165, "On your left is the glasshouse borders"));
        mHandler.notLatlng.add(new NotifyLatLng(51.314017, -0.477494, "You are now approaching glasshouse"));
        mHandler.notLatlng.add(new NotifyLatLng(51.314793, -0.476425, "On your right you can see Wisleys Seven acres"));
        mHandler.notLatlng.add(new NotifyLatLng(51.314269, -0.473808, "You are now approaching Wisley Laboratory"));
        mHandler.notLatlng.add(new NotifyLatLng(51.313782, -0.473522, "You are now approaching Wisley Laboratory if you keep following the route ahead you will be passing by the canal"));
        mHandler.notLatlng.add(new NotifyLatLng(51.313849, -0.474144, "You are about to enter the Oakwood"));
        mHandler.notLatlng.add(new NotifyLatLng(51.314399, -0.474070, "You were just passing by the Wisleys Seven acres"));
        mHandler.notLatlng.add(new NotifyLatLng(51.315095, -0.473706, "Look at our new constructions!"));
        mHandler.notLatlng.add(new NotifyLatLng(51.315755, -0.473946, "You are about to enjoy food at Wisleys FoodHall"));


        addLocationAlert(51.313468, -0.473050);
        addLocationAlert(51.312938, -0.474378);
        addLocationAlert(51.312363, -0.474236);
        addLocationAlert(51.313129, -0.476095);
        addLocationAlert(51.311925, -0.477388);
        addLocationAlert(51.311645, -0.475860);

        addLocationAlert(51.313735, -0.473338);
        addLocationAlert(51.313849, -0.474144);
        addLocationAlert(51.313743, -0.475881);
        addLocationAlert(51.313301, -0.478165);
        addLocationAlert(51.314017, -0.477494);
        addLocationAlert(51.314793, -0.476425);
        addLocationAlert(51.314269, -0.473808);
        addLocationAlert(51.313782, -0.473522);
        addLocationAlert(51.313849, -0.474144);
        addLocationAlert(51.314399, -0.474070);
        addLocationAlert(51.315095, -0.473706);
        addLocationAlert(51.315755, -0.473946);


    }


    private Drawable createLargeDrawable(int resId) throws IOException {

        InputStream is = getResources().openRawResource(resId);
        BitmapRegionDecoder brd = BitmapRegionDecoder.newInstance(is, true);

        try {
            if (brd.getWidth() <= MAX_SIZE && brd.getHeight() <= MAX_SIZE) {
                return new BitmapDrawable(getResources(), is);
            }

            int rowCount = (int) Math.ceil((float) brd.getHeight() / (float) MAX_SIZE);
            int colCount = (int) Math.ceil((float) brd.getWidth() / (float) MAX_SIZE);

            BitmapDrawable[] drawables = new BitmapDrawable[rowCount * colCount];

            for (int i = 0; i < rowCount; i++) {

                int top = MAX_SIZE * i;
                int bottom = i == rowCount - 1 ? brd.getHeight() : top + MAX_SIZE;

                for (int j = 0; j < colCount; j++) {

                    int left = MAX_SIZE * j;
                    int right = j == colCount - 1 ? brd.getWidth() : left + MAX_SIZE;

                    Bitmap b = brd.decodeRegion(new Rect(left, top, right, bottom), null);
                    BitmapDrawable bd = new BitmapDrawable(getResources(), b);
                    bd.setGravity(Gravity.TOP | Gravity.LEFT);
                    drawables[i * colCount + j] = bd;
                }
            }

            LayerDrawable ld = new LayerDrawable(drawables);
            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < colCount; j++) {
                    ld.setLayerInset(i * colCount + j, MAX_SIZE * j, MAX_SIZE * i, 0, 0);
                }
            }

            return ld;
        } finally {
            brd.recycle();
        }
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    private void showRadioButtonDialog() {

        // custom dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        dialog.setContentView(R.layout.radiobutton_dialog);
        List<String> stringList = new ArrayList<>();  // here is list

        stringList.add("Gardens");
        stringList.add("Glass House");
        stringList.add("Wild Garden");

        RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radio_group);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int childCount = group.getChildCount();

                for (int x = 0; x < childCount; x++) {
                    RadioButton btn = (RadioButton) group.getChildAt(x);
                    if (btn.getId() == checkedId) {
                        Log.e("selected RadioButton->", btn.getText().toString());

                        if (btn.getText().toString().equalsIgnoreCase("Gardens")) {
                            mMap.clear();
                            onMapReady(mMap);
                            mMap.addPolyline(options1);
                            for (int i = 0; i < places.size(); i++) {
                                LatLng locallatLng = new LatLng(places.get(i).plat, places.get(i).plng);

                                markerOptions = new MarkerOptions().position(locallatLng).title(places.get(i).pname).snippet(String.valueOf(places.get(i).pimg));
                                mMap.addMarker(markerOptions);

                            }
                            dialog.dismiss();
                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            for (int a = 0; a < route1.size(); a++) {
                                builder.include(route1.get(a));
                            }
                            LatLngBounds bounds = builder.build();
                            int padding = 0; // padding around start and end marker
                            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                            mMap.animateCamera(cu);
                        } else if (btn.getText().toString().equalsIgnoreCase("Glass House")) {
                            mMap.clear();
                            onMapReady(mMap);
                            mMap.addPolyline(options2);
                            for (int i = 0; i < places.size(); i++) {
                                LatLng locallatLng = new LatLng(places.get(i).plat, places.get(i).plng);

                                markerOptions = new MarkerOptions().position(locallatLng).title(places.get(i).pname).snippet(String.valueOf(places.get(i).pimg));
                                mMap.addMarker(markerOptions);

                            }
                            dialog.dismiss();
                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            for (int a = 0; a < route2.size(); a++) {
                                builder.include(route2.get(a));
                            }
                            LatLngBounds bounds = builder.build();
                            int padding = 0; // padding around start and end marker
                            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                            mMap.animateCamera(cu);
                        } else if (btn.getText().toString().equalsIgnoreCase("Wild Garden")) {
                            mMap.clear();
                            onMapReady(mMap);
                            mMap.addPolyline(options3);
                            for (int i = 0; i < places.size(); i++) {
                                LatLng locallatLng = new LatLng(places.get(i).plat, places.get(i).plng);

                                markerOptions = new MarkerOptions().position(locallatLng).title(places.get(i).pname).snippet(String.valueOf(places.get(i).pimg));
                                mMap.addMarker(markerOptions);

                            }
                            dialog.dismiss();
                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            for (int a = 0; a < route3.size(); a++) {
                                builder.include(route3.get(a));
                            }


                            LatLngBounds bounds = builder.build();
                            int padding = 0; // padding around start and end marker
                            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                            mMap.animateCamera(cu);
                        }

                    }
                }
            }
        });

        for (int i = 0; i < stringList.size(); i++) {
            RadioButton rb = new RadioButton(getActivity()); // dynamically creating RadioButton and adding to RadioGroup.
            rb.setText(stringList.get(i));
            rg.addView(rb);
        }

        dialog.show();

    }


    @SuppressLint("MissingPermission")
    private void showCurrentLocationOnMap() {
        if (isLocationAccessPermitted()) {
            requestLocationAccessPermission();
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }

    private boolean isLocationAccessPermitted() {
        if (ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestLocationAccessPermission() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                LOC_PERM_REQ_CODE);
    }

    @SuppressLint("MissingPermission")
    private void addLocationAlert(double lat, double lng) {
        if (isLocationAccessPermitted()) {
            requestLocationAccessPermission();
        } else {
            String key = "" + lat + "-" + lng;
            Geofence geofence = getGeofence(lat, lng, key);
            geofencingClient.addGeofences(getGeofencingRequest(geofence),
                    getGeofencePendingIntent())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                               /* Toast.makeText(getActivity(),
                                        "Location alter has been added",
                                        Toast.LENGTH_SHORT).show();*/
                            } else {
                                Toast.makeText(getActivity(),
                                        "Please Turn On Your Location and Wifi!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void removeLocationAlert() {
        if (isLocationAccessPermitted()) {
            requestLocationAccessPermission();
        } else {
            geofencingClient.removeGeofences(getGeofencePendingIntent())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                               /* Toast.makeText(getActivity(),
                                        "Location alters have been removed",
                                        Toast.LENGTH_SHORT).show();*/
                            } else {
                                Toast.makeText(getActivity(),
                                        "Location alters could not be removed",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOC_PERM_REQ_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showCurrentLocationOnMap();
                    Toast.makeText(getActivity(),
                            "Location access permission granted, you try " +
                                    "add or remove location allerts",
                            Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }

    private PendingIntent getGeofencePendingIntent() {
        Intent intent = new Intent(getContext(), LocationAlertIntentService.class);
        return PendingIntent.getService(getContext(), 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private GeofencingRequest getGeofencingRequest(Geofence geofence) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL);
        builder.addGeofence(geofence);
        return builder.build();
    }

    private Geofence getGeofence(double lat, double lang, String key) {
        return new Geofence.Builder()
                .setRequestId(key)
                .setCircularRegion(lat, lang, GEOFENCE_RADIUS)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_DWELL)
                .setLoiteringDelay(10000)
                .build();
    }

    public void prepareMarkers() {
        places = new ArrayList<>();
        places.add(new Places(51.321487, -0.474528, "WoodSide House", "Not Entered", R.drawable.sample, 0));
        places.add(new Places(51.321126, -0.474798, "Bugs Garden", "Not Entered", R.drawable.sample, 1));
        places.add(new Places(51.320600, -0.475057, "Bird Hide", "Not Entered", R.drawable.sample, 2));
        places.add(new Places(51.319506, -0.474114, "The PineTum", "Not Entered", R.drawable.thepinetum, 3));
        places.add(new Places(51.317174, -0.474323, "RiverSide Garden", "Not Entered", R.drawable.sample, 4));
        places.add(new Places(51.315658, -0.473463, "Food Hall", "Not Entered", R.drawable.sample, 5));
        places.add(new Places(51.315018, -0.473503, "Exhibition Site Office", "Not Entered", R.drawable.foodhallwisley, 6));
        places.add(new Places(51.314985, -0.476818, "Seven Acres", "Not Entered", R.drawable.sevenacres, 7));
        places.add(new Places(51.315045, -0.478004, "Glass House Cafe", "Not Entered", R.drawable.glasshousecafe, 8));
        places.add(new Places(51.314249, -0.476422, "Wild Garden", "Not Entered", R.drawable.sample, 9));
        places.add(new Places(51.314100, -0.473493, "Wisley Pond", "Not Entered", R.drawable.canalpond, 10));
        places.add(new Places(51.314039, -0.472722, "Laboratory", "Not Entered", R.drawable.laboratory, 11));
        places.add(new Places(51.313803, -0.472379, "Shop & Cafe", "Not Entered", R.drawable.sample, 12));
        places.add(new Places(51.313212, -0.477336, "Rock Garden", "Not Entered", R.drawable.rockgarden, 13));
        places.add(new Places(51.313000, -0.479651, "Children's Play Area", "Not Entered", R.drawable.sample, 14));
        places.add(new Places(51.312437, -0.478103, "GlassHouse Borders", "Not Entered", R.drawable.glasshouseborders, 15));
        places.add(new Places(51.312793, -0.476345, "Alpine House", "Not Entered", R.drawable.alpinehouse, 16));
        places.add(new Places(51.313302, -0.474139, "Country Garden", "Not Entered", R.drawable.countrygarden, 17));
        places.add(new Places(51.313150, -0.473580, "Mixed Borders", "Not Entered", R.drawable.mixedborders, 18));
        places.add(new Places(51.313244, -0.472873, "Library", "Not Entered", R.drawable.library, 19));
        places.add(new Places(51.313300, -0.472030, "Garden's Gate", "Not Entered", R.drawable.sample, 20));
        places.add(new Places(51.311836, -0.478065, "Fruit Garden", "Not Entered", R.drawable.fruitgarden, 21));
        places.add(new Places(51.312094, -0.476362, "Herb Garden", "Not Entered", R.drawable.herbsgarden, 22));
        places.add(new Places(51.312146, -0.475699, "Model Gardens", "Not Entered", R.drawable.modelgarden, 23));
        places.add(new Places(51.311645, -0.475860, "The Honest Sausage", "Not Entered", R.drawable.honestsausage, 24));
        places.add(new Places(51.312087, -0.473860, "Rose Garden", "Not Entered", R.drawable.rosegarden, 25));
        places.add(new Places(51.312243, -0.471812, "Battleston Hill", "Not Entered", R.drawable.battlestonhill, 26));
        places.add(new Places(51.311516, -0.470060, "Battleston Hill East", "Not Entered", R.drawable.battlestonhilleast, 27));
        places.add(new Places(51.310534, -0.473082, "Trial Fields", "Not Entered", R.drawable.trialsfield, 28));
        places.add(new Places(51.311264, -0.474895, "New Centre For Horticultural Science & Learning", "Not Entered", R.drawable.newhorticulturalscience, 29));
        places.add(new Places(51.309468, -0.478797, "Orchard", "Not Entered", R.drawable.theorchard, 30));
    }

    /* @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         MenuInflater inflater = rootView.getMenuInflater();
         inflater.inflate(R.menu.menu, menu);
         return true;
     }*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accept:
                removeLocationAlert();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}