package com.example.androiddeveloper.gardenapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;
import com.example.androiddeveloper.gardenapp.Activities.MainActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by QuellX on 5/14/2018.
 */

public class BeaconService extends Service {

    private BeaconManager beaconManager;
    private BeaconRegion region;
    Context context;
    private static Map<String, List<String>> PLACES_BY_BEACONS;
    String lastplace= "nothing";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

        context = getApplicationContext();

       // showNotification("Beacon", "Hello");
       // showDialog("Beacon", "Hello");
        beaconManager = new BeaconManager(this);
        beaconManager.setRangingListener(new BeaconManager.BeaconRangingListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onBeaconsDiscovered(BeaconRegion region, List<Beacon> list) {
                Log.e("Message", "I am here");
                if (!list.isEmpty()) {
                    Beacon nearestBeacon = list.get(0);

                    List<String> places = placesNearBeacon(nearestBeacon);
                  //  lastplace = places.get(0);
                    if(lastplace.equalsIgnoreCase("nothing")){
                        showNotification("Beacon", places.get(0));
                        lastplace=places.get(0);
                        startbeacon();

                    }else if(lastplace.equalsIgnoreCase(places.get(0))){
                        startbeacon();
                    }else if(!lastplace.equalsIgnoreCase(places.get(0))){
                        showNotification("Beacon", places.get(0));
                        lastplace=places.get(0);
                        startbeacon();
                    }

                 //   showDialog("Beacon", places.get(0));
                    Log.d("Airport", "Nearest places: " + places);


                }
            }
        });
        region = new BeaconRegion("ranged region",
                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);

       // SystemRequirementsChecker.checkWithDefaultDialogs(MainActivity.class);
        startbeacon();

        return START_STICKY;
    }

    public void startbeacon(){
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {

                beaconManager.startRanging(region);

            }
        });
    }

    public void stopbeacon(){
        beaconManager.stopRanging(region);
    }

    // TODO: replace "<major>:<minor>" strings to match your own beacons.
    static {
        Map<String, List<String>> placesByBeacons = new HashMap<>();

        placesByBeacons.put("50870:53525", new ArrayList<String>() {{
            add("You are approaching GlassHouse");


        }});
        placesByBeacons.put("374:60017", new ArrayList<String>() {{
            add("You are approaching Library");


        }});
        placesByBeacons.put("557:34175", new ArrayList<String>() {{
            add("On your right approaching canal");

        }});
        PLACES_BY_BEACONS = Collections.unmodifiableMap(placesByBeacons);
    }
    private List<String> placesNearBeacon(Beacon beacon) {
        String beaconKey = String.format("%d:%d", beacon.getMajor(), beacon.getMinor());
        if (PLACES_BY_BEACONS.containsKey(beaconKey)) {
            return PLACES_BY_BEACONS.get(beaconKey);
        }
        return Collections.emptyList();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void showNotification(String title, String message) {


        String CHANNEL_ID = "Zoftino";
        android.support.v4.app.NotificationCompat.Builder builder =
                new android.support.v4.app.NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.logorhs)
                        .setContentTitle(title).
                        setStyle(new android.support.v4.app.NotificationCompat.BigTextStyle()
                                .bigText(message))
                        .setContentText(message).setDefaults(Notification.DEFAULT_SOUND);


        builder.setAutoCancel(true);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(0, builder.build());
        stopbeacon();

    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}