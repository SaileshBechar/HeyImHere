package ca.saileshbechar.imhere;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;



import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.Arrays;
import java.util.Map;

import ca.saileshbechar.imhere.models.PersonObject;

public class MainActivity extends AppCompatActivity {
    LinearLayout lower, upper;
    public Button addfriend1, addfriend2, addfriend3, addfriend4, info;
    Animation uptodown, downtoup;
    private static final String TAG = "MainActivity";
    boolean[] active = new boolean[4];
    String[] names;

    private static final int ERROR_DIALOG_REQUEST = 9001;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static final int MY_PERMISSIONS_REQUEST_SMS = 98;
    public static final int SUCCESSFUL_RESPONSE = 10;
    MyDbHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addfriend1 = (Button) findViewById(R.id.friend_1);
        addfriend2 = (Button) findViewById(R.id.friend_2);
        addfriend3 = (Button) findViewById(R.id.friend_3);
        addfriend4 = (Button) findViewById(R.id.friend_4);
        info = (Button) findViewById(R.id.info);
        upper = (LinearLayout) findViewById(R.id.upperpanel);
        lower = (LinearLayout) findViewById(R.id.lowerpanel);
        uptodown = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        downtoup = AnimationUtils.loadAnimation(this, R.anim.downtoup);
        upper.setAnimation(uptodown);
        lower.setAnimation(downtoup);
        dbHandler = new MyDbHandler(this);
        checkPermissions();
        if (dbHandler.printData() != ""){
            refreshButtons();
        }
        addfriend1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            checkPermissions();
            if (active[0]) {
                Intent openMaps = new Intent(MainActivity.this, MapsActivity.class);
                openMaps.putExtra("personNumber", dbHandler.printNumber(names[0]));
                openMaps.putExtra("active", active[0]);
                openMaps.putExtra("personLat", Double.parseDouble(dbHandler.printLat(names[0])));
                openMaps.putExtra("personLong", Double.parseDouble(dbHandler.printLong(names[0])));
                openMaps.putExtra("personRad", Integer.parseInt(dbHandler.printRadius(names[0])));
                startActivity(openMaps);
            }
            else{
                Intent openMaps = new Intent(MainActivity.this, MapsActivity.class);
                openMaps.putExtra("active", active[0]);
                startActivityForResult(openMaps, SUCCESSFUL_RESPONSE);
                Log.d("DB", "After:" + dbHandler.printData() + "!");
                Log.d("Active", "After: " + active[0]);
                refreshButtons();
            }
                }
        });
        addfriend2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            checkPermissions();
            if (active[1]) {
                Intent openMaps = new Intent(MainActivity.this, MapsActivity.class);
                openMaps.putExtra("personNumber", dbHandler.printNumber(names[1]));
                openMaps.putExtra("active", active[1]);
                openMaps.putExtra("personLat", Double.parseDouble(dbHandler.printLat(names[1])));
                openMaps.putExtra("personLong", Double.parseDouble(dbHandler.printLong(names[1])));
                openMaps.putExtra("personRad", Integer.parseInt(dbHandler.printRadius(names[1])));
                startActivity(openMaps);
                refreshButtons();
            }
            else{
                Intent openMaps = new Intent(MainActivity.this, MapsActivity.class);
                openMaps.putExtra("active", active[1]);
                startActivityForResult(openMaps, SUCCESSFUL_RESPONSE);
            }
            }
        });
        addfriend3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            checkPermissions();
            if (active[2]) {
                Intent openMaps = new Intent(MainActivity.this, MapsActivity.class);
                openMaps.putExtra("personNumber", dbHandler.printNumber(names[2]));
                openMaps.putExtra("active", active[2]);
                openMaps.putExtra("personLat", Double.parseDouble(dbHandler.printLat(names[2])));
                openMaps.putExtra("personLong", Double.parseDouble(dbHandler.printLong(names[2])));
                openMaps.putExtra("personRad", Integer.parseInt(dbHandler.printRadius(names[2])));
                startActivity(openMaps);
                refreshButtons();
            }
            else{
                Intent openMaps = new Intent(MainActivity.this, MapsActivity.class);
                openMaps.putExtra("active", active[2]);
                startActivityForResult(openMaps, SUCCESSFUL_RESPONSE);
            }
            }
        });
        addfriend4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            checkPermissions();
            if (active[3]) {
                Intent openMaps = new Intent(MainActivity.this, MapsActivity.class);
                openMaps.putExtra("personNumber", dbHandler.printNumber(names[3]));
                openMaps.putExtra("active", active[3]);
                openMaps.putExtra("personLat", Double.parseDouble(dbHandler.printLat(names[3])));
                openMaps.putExtra("personLong", Double.parseDouble(dbHandler.printLong(names[3])));
                openMaps.putExtra("personRad", Integer.parseInt(dbHandler.printRadius(names[3])));
                startActivity(openMaps);
                refreshButtons();
            }
            else{
                Intent openMaps = new Intent(MainActivity.this, MapsActivity.class);
                openMaps.putExtra("active", active[3]);
                startActivityForResult(openMaps, SUCCESSFUL_RESPONSE);
            }
            }
        });

        addfriend1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (active[0]) {
                    createDeleteDialog(names[0]);
                    return true;
                }
                else{
                    return false;
                }
            }
        });
        addfriend2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (active[1]) {
                    createDeleteDialog(names[1]);
                    return true;
                }
                else{
                    return false;
                }
            }
        });
        addfriend3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (active[2]) {
                    createDeleteDialog(names[2]);
                    return true;
                }
                else{
                    return false;
                }
            }
        });
        addfriend4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (active[3]) {
                    createDeleteDialog(names[3]);
                    return true;
                }
                else{
                    return false;
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            refreshButtons();
            //locationManager.requestLocationUpdates(provider, 400, 1, this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            //locationManager.removeUpdates(this);
        }
    }

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("Need location to text your friends when you're nearby!")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                            checkPermissions();
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }

    private void requestSMSPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.SEND_SMS)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("Need SMS to text your friends when you're nearby!")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[] {Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SMS);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SMS);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == MY_PERMISSIONS_REQUEST_SMS)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED for SMS", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public static Intent makeNotificationIntent(Context geofenceService, String msg)
    {
        Log.d(TAG,msg);
        return new Intent(geofenceService,MainActivity.class);
    }

    public void checkPermissions(){
        if ((ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)){
            Log.d(TAG, "Location Permission already enabled!");
        } else {
            requestLocationPermission();
            Log.d(TAG, "Requested Location Permissions");
        }
        if ((ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)) {
            Log.d(TAG, "Location Permission already enabled!");
        }else{
            requestSMSPermission();
            Log.d(TAG, "Requested SMS Permissions");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case SUCCESSFUL_RESPONSE:
                if (resultCode == Activity.RESULT_OK){
                    boolean insertdata = dbHandler.addPerson((PersonObject) data.getSerializableExtra("personObject"));
                    if (insertdata){
                        Toast.makeText(this, "Successfully Saved Geofence", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }

    private void refreshButtons(){
        names = dbHandler.printPerson();
        int i = 0;
        Arrays.fill(active, Boolean.FALSE);
        addfriend1.setText(R.string.friend_default_text);
        addfriend2.setText(R.string.friend_default_text);
        addfriend3.setText(R.string.friend_default_text);
        addfriend4.setText(R.string.friend_default_text);
        while (names[i] != null){
            switch (i) {
                case 0:
                    addfriend1.setText(String.format("Send to %s", names[0]));
                    active[0] = true;
                    break;
                case 1:
                    addfriend2.setText(String.format("Send to %s", names[1]));
                    active[1] = true;
                    break;
                case 2:
                    addfriend3.setText(String.format("Send to %s", names[2]));
                    active[2] = true;
                    break;
                case 3:
                    addfriend4.setText(String.format("Send to %s", names[3]));
                    active[3] = true;
                    break;
            }
            i++;
        }
    }

    public void createDeleteDialog(final String buttonname){
        new AlertDialog.Builder(this)
                .setTitle("Delete Geofence")
                .setMessage("Would you like to delete this geofence?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHandler.deletePerson(buttonname);
                        Log.d("DB", "Deleted:" + dbHandler.printData() + "!");
                        Log.d("DB", "Name: " + buttonname + "!");
                        refreshButtons();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }

    public void infoButtonClick(View view){
        new AlertDialog.Builder(this)
                .setTitle("Help")
                .setMessage("To add a location: click \"Add Friend\" \n" +
                        "To delete: hold down on the name for a couple seconds! " +
                        "When adding a new location, enter the name, " +
                        "the phone number (no spaces) and the radius " +
                        "of how far away your friend will be notified " +
                        "before you arrive! \n\n"+
                        "After pressing send, go to Google Maps to track your current location" +
                        ", this app will work in the background. " +
                        "\n\nIf you are experiencing crashes, " +
                        "make sure you have location services turned on, as well as" +
                        " a stable internet connection.")
                .setPositiveButton("Thanks!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                    }
                })
                .create().show();
    }

}
