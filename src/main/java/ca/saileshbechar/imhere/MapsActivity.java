package ca.saileshbechar.imhere;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ca.saileshbechar.imhere.models.PersonObject;
import ca.saileshbechar.imhere.models.PlaceInfo;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, GoogleApiClient.OnConnectionFailedListener, OnCompleteListener<Void> {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocation;
    private AutoCompleteTextView mSearchText;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "MapActivity";
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));
    private PlaceInfo mPlace;
    private static final float DEFAULT_ZOOM = 15f;
    public Button locationButton;
    private Context mContext;
    private PendingIntent mGeofencePendingIntent;
    private int GEOFENCE_RADIUS = 100;
    public double currentLatitude;
    public double currentLongitude;
    public String currentPlace;
    private GeofencingClient mGeofencingClient;
    private PersonObject mPerson;
    private String currentNumber;
    private Geofence newGeofence;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
        mSearchText = (AutoCompleteTextView) findViewById(R.id.input_search);
        locationButton = (Button) findViewById(R.id.location_button);
        mSearchText.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, LAT_LNG_BOUNDS, null);
        mSearchText.setAdapter(mPlaceAutocompleteAdapter);
        mGeofencingClient = LocationServices.getGeofencingClient(this);
        mGeofencePendingIntent = null;
        Bundle extras = getIntent().getExtras();
        Boolean active = extras.getBoolean("active");
        if (active){
            usePreviousGeofence(extras);
            finish();
        }
        else {
            locationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addGeofences();
                }
            });
        }
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

//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            getDeviceLocation();
        } else {
            Toast.makeText(this, "Need location to use this feature", Toast.LENGTH_LONG).show();
            return;
        }
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
    }
    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    public void getDeviceLocation(){
        mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
        @SuppressLint("MissingPermission") Task location = mFusedLocation.getLastLocation();
        final Task task = location.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    Location currentlocation = (Location) task.getResult();
                    currentPlace = "My Location";
                    moveCamera(new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude()), 15f, currentPlace);
                    currentLatitude = currentlocation.getLatitude();
                    currentLongitude = currentlocation.getLongitude();

                }
            }
        });
    }

    public void moveCamera(LatLng latLng, float zoom, String name){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        mMap.clear();
        if(!name.equals("My Location")){
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(name);
            mMap.addMarker(options);
        }

        hideSoftKeyboard();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //Google Places
    private void hideSoftKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            hideSoftKeyboard();

            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(i);
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };


    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>(){
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if(!places.getStatus().isSuccess()){
                Log.d(TAG, "onResult: Place query did not complete successfully: " + places.getStatus().toString());
                places.release();
                return;
            }
            final Place place = places.get(0);

            try{
                mPlace = new PlaceInfo();
                mPlace.setName(place.getName().toString());
                Log.d(TAG, "onResult: name: " + place.getName());
                mPlace.setAddress(place.getAddress().toString());
                Log.d(TAG, "onResult: address: " + place.getAddress());
                mPlace.setId(place.getId());
                Log.d(TAG, "onResult: id:" + place.getId());
                mPlace.setLatlng(place.getLatLng());
                Log.d(TAG, "onResult: latlng: " + place.getLatLng());

                Log.d(TAG, "onResult: place: " + mPlace.toString());
            }catch (NullPointerException e){
                Log.e(TAG, "onResult: NullPointerException: " + e.getMessage() );
            }
            currentLatitude = place.getLatLng().latitude;
            currentLongitude = place.getLatLng().longitude;
            currentPlace = place.getName().toString();

            moveCamera(new LatLng(place.getViewport().getCenter().latitude,
                    place.getViewport().getCenter().longitude), DEFAULT_ZOOM, mPlace.getName());

            places.release();
        }
    };

    //GeoFencing



    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofence(newGeofence);

        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionService.class);
        String tempPhone = currentNumber;
        intent.putExtra("temphoneNumber", tempPhone);
        mGeofencePendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;
    }

    private void createGeofence(){
        newGeofence =
        (new Geofence.Builder()
                // Set the request ID of the geofence. This is a string to identify this
                // geofence.
                .setRequestId(currentNumber)

                .setCircularRegion(
                        currentLatitude,
                        currentLongitude,
                        GEOFENCE_RADIUS
                )
                .setExpirationDuration(3600*1000)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .build());
    }

    @SuppressWarnings("MissingPermission")
    private void addGeofences(){
        mPerson = new PersonObject();
        getPhoneNumber();
    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {
        if (task.isSuccessful()) {
            Log.w(TAG, "on Complete Successful");
        } else {
            // Get the status code for the error and log it using a user-friendly message.

            Log.w(TAG, "on Complete Unsuccessful");
        }
    }

    @SuppressWarnings("MissingPermission")
    public void getPhoneNumber() {

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.add_phone, null);

        final EditText mName = (EditText) mView.findViewById(R.id.name_input);
        final EditText mPhoneNumber = (EditText) mView.findViewById(R.id.phone_input);
        final EditText mRadius = (EditText) mView.findViewById(R.id.radius_input);
        Button mConfirm = (Button) mView.findViewById(R.id.confirm_button);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        alert.setView(mView);
        final AlertDialog dialog = alert.create();
        dialog.show();
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mName.getText().toString().isEmpty() && !mPhoneNumber.getText().toString().isEmpty()){
                    Toast.makeText(MapsActivity.this,
                            "Successfully entered info",
                            Toast.LENGTH_SHORT).show();
                    String phoneNumber = mPhoneNumber.getText().toString();
                    String name = mName.getText().toString();
                    GEOFENCE_RADIUS = Integer.parseInt(mRadius.getText().toString());
                    if (GEOFENCE_RADIUS < 100){
                        GEOFENCE_RADIUS = 100;
                    }
                    if (GEOFENCE_RADIUS > 5000){
                        GEOFENCE_RADIUS = 1000;
                    }
                    mPerson.setPhoneNumber(phoneNumber);
                    mPerson.setName(name);
                    mPerson.setLat(currentLatitude);
                    mPerson.setLng(currentLongitude);
                    mPerson.setRadius(GEOFENCE_RADIUS);
                    currentNumber = phoneNumber;
                    dialog.dismiss();
                    sendIntent();

                }else{
                    Toast.makeText(MapsActivity.this,
                            "Please enter a name and phone number!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void sendSMS(String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(currentNumber, null, message, null, null);
    }

    @SuppressWarnings("MissingPermission")
    public void usePreviousGeofence(Bundle extras){
        currentLatitude = extras.getDouble("personLat");
        currentLongitude = extras.getDouble("personLong");
        GEOFENCE_RADIUS = extras.getInt("personRad");
        currentNumber = extras.getString("personNumber");
        createGeofence();
        sendSMS("Leaving now!");
        Toast.makeText(this, "You are now en route!", Toast.LENGTH_LONG).show();
        mGeofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                .addOnCompleteListener(MapsActivity.this);
    }

    public void sendIntent(){
        Intent resultIntent = new Intent();
        resultIntent.putExtra("personObject", mPerson);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }


}
