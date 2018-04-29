package ca.saileshbechar.imhere;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.NotificationCompat;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

public class GeofenceTransitionService extends IntentService {

    private static final String TAG = GeofenceTransitionService.class.getSimpleName();
    public static final int GEOFENCE_NOTIFICATION_ID = 0;
    private String tempphoneNumber = "1234567890";

    public GeofenceTransitionService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Retrieve the Geofencing intent
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        // Handling errors
        if ( geofencingEvent.hasError() ) {
            Log.e( TAG, "Geofence has error" );
            return;
        }

        tempphoneNumber = intent.getStringExtra("temphoneNumber");

        // Retrieve GeofenceTrasition
        int geoFenceTransition = geofencingEvent.getGeofenceTransition();
        // Check if the transition type

        if ( geoFenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geoFenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL  ) {
            // Get the geofence that were triggered
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
            // Create a detail message with Geofences received
            String geofenceTransitionDetails = getGeofenceTrasitionDetails(geoFenceTransition, triggeringGeofences );
            // Send notification details as a String
            //sendNotification( geofenceTransitionDetails );
        }
    }

    // Create a detail message with Geofences received
    private String getGeofenceTrasitionDetails(int geoFenceTransition, List<Geofence> triggeringGeofences) {
        // get the ID of each geofence triggered
        ArrayList<String> triggeringGeofencesList = new ArrayList<>();
        for ( Geofence geofence : triggeringGeofences ) {
            triggeringGeofencesList.add( geofence.getRequestId() );
        }

        String status = null;
        if ( geoFenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ) {
            status = "Entering ";
            sendSMS("Here!");
            createNotification();
        }
        else if ( geoFenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT )
            status = "Exiting ";
        return status + TextUtils.join( ", ", triggeringGeofencesList);
    }
    //Send text message
    public void sendSMS(String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(tempphoneNumber, null, message, null, null);
    }
    // Send a notification
    private void sendNotification( String msg ) {

        // Intent to start the main Activity
        Intent notificationIntent = MainActivity.makeNotificationIntent(
                getApplicationContext(), msg
        );

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Creating and sending Notification
//        NotificationManager notificatioMng =
//                (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
//        notificatioMng.notify(
//                GEOFENCE_NOTIFICATION_ID,
//                createNotification());

    }

    // Create a notification
    private void createNotification(){
        Intent notificationIntent = new Intent(this, MapsActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification =
                new Notification.Builder(this)
                        .setContentTitle("Hey, Im Here!")
                        .setContentText("You have arrived at the destination")
                        .setSmallIcon(R.drawable.ic_car)
                        .setContentIntent(pendingIntent)
                        .setTicker("Starting Up")
                        .setPriority(Notification.PRIORITY_HIGH)
                        .build();
        startForeground(123, notification);

        NotificationManager notificatioMng =
                (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
        notificatioMng.notify(
                GEOFENCE_NOTIFICATION_ID,
                notification);
    }


}