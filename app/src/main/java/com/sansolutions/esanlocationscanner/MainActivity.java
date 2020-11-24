package com.sansolutions.esanlocationscanner;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.Settings;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.sansolutions.esanblescanner.db.DatabaseHandler;
import com.sansolutions.esanlocationscanner.db.LocationDAO;
import com.sansolutions.esanlocationscanner.db.LocationEntity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private static final String TAG = "MainActivity";
    private TextView mLatitudeTextView;
    private TextView mLongitudeTextView;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;

    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    private String baseFilePath = Environment.getExternalStorageDirectory() + "/Download/ESAN_GEO_MUSIC_FILES/";
    public static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1989;

    private MediaPlayer mediaPlayer = null;
    private DatabaseHandler databaseHandler;
    private LocationDAO bleDao;
    private List<LocationEntity> storedDevices;


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissionsList[], int[] grantResults) {
        switch (requestCode) {

            case REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION: {
                if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION) {
                    int grantResultsLength = grantResults.length;
                    if (grantResultsLength > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + "/Download/ESAN_GEO_MUSIC_FILES/");
                        mediaStorageDir.mkdir();
                        if (!mediaStorageDir.exists()) {
                            if (!mediaStorageDir.mkdirs()) {

                                Log.d("App", "failed to create directory");
                            }
                        }
                    } else {

                        Toast.makeText(getApplicationContext(), "You denied write external storage permission", Toast.LENGTH_LONG).show();
                    }
                }
                return;
            }
        }
    }

    public static boolean isExternalStorageMounted() {
        String dirState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(dirState))
            return true;
        else
            return false;
    }


    private void locationPermission() {
        try {
                int writeExternalStoragePermission =
                        ContextCompat.checkSelfPermission(MainActivity.this,
                                Manifest.permission.ACCESS_COARSE_LOCATION);

                if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new
                                    String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            211);
                } else {
                    initGoogleClient();
                }

        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Save to private external storage failed. Error message is " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    private void locationPermission1() {
        try {
            int writeExternalStoragePermission =
                    ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION);

            if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new
                                String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        2221);
            } else {
                initGoogleClient();
            }

        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Save to private external storage failed. Error message is " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    private void createFolder() {
        try {
            if (isExternalStorageMounted()) {

                int writeExternalStoragePermission =
                        ContextCompat.checkSelfPermission(MainActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new
                                    String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
                } else {
                    File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + "/Download/ESAN_GEO_MUSIC_FILES/");

                    mediaStorageDir.mkdir();
                    if (mediaStorageDir.exists()) {
                        if (!mediaStorageDir.mkdirs()) {
                            Log.d("App", "failed to create directory");
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Save to private external storage failed. Error message is " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private MediaPlayer selectMediaFileDatabase(String name) {
        List<LocationEntity> bleEntities = bleDao.getAllBLEsFromDb();
        if (bleEntities != null && bleEntities.size() > 0) {
            for (LocationEntity bleEntity : bleEntities) {
                if (name.equals(bleEntity.getLatitude())) {

                    try {
                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setDataSource(baseFilePath + bleEntity.getLatitude() + ".mp3");
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }
        }
        return mediaPlayer;
    }

    public void playMedia(String deviceName, int rssi, boolean isStatus) {
        Log.e("MEDIAPLAYING","deviceName");
        mediaPlayer = selectMediaFileDatabase(deviceName);
        mediaPlayer.start();
       // updateALLBLEStatus(deviceName, isStatus);
      /*  locationValueTextView.setVisibility(View.VISIBLE);
        locationValueTextView.setText(deviceName + "==" + rssi);
        updateALLBLEStatus(deviceName, isStatus);*/
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.e("MEDIAPLAYING","stopped");
           /*     isMediaPlaying=false;
                locationValueTextView.setVisibility(View.GONE);*/
            }
        });
    }


    /*@Override
    protected void onPause() {
        super.onPause();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            mLocationManager.removeUpdates(m);
        }
    }*/


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "ESANGEOBLEScanner::WAKELOCK");
        wakeLock.acquire();
        locationPermission();
        locationPermission1();
        createFolder();
        databaseHandler = DatabaseHandler.Companion.getDatabase(this);
        bleDao = databaseHandler.locationDao();
        storedDevices = new ArrayList();
        storedDevices = bleDao.getAllBLEsFromDb();

        findViewById(R.id.settings_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,StoredBLEActivityList.class));
            }
        });
        mLatitudeTextView = (TextView) findViewById((R.id.latitude_textview));
        mLongitudeTextView = (TextView) findViewById((R.id.longitude_textview));
        initGoogleClient();


    }

    private void initGoogleClient()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        checkLocation(); //check whether location service is enable or not in your  phone

    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

       // startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLocation == null){
            startLocationUpdates();
        }
        if (mLocation != null) {

            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    protected void startLocationUpdates() {
        // Create the location request

        Log.e("PERMISSION","startLocationUpdates");
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.e("PERMISSION","onLocationChanged");

        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        mLatitudeTextView.setText(String.valueOf(location.getLatitude()));
        mLongitudeTextView.setText(String.valueOf(location.getLongitude() ));
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        //getDistance()
               // getDistance()
        if(storedDevices!=null)
        {
            storedDevices = bleDao.getAllBLEsFromDb();
        }
        else
        {
            storedDevices=new ArrayList<>();
            storedDevices = bleDao.getAllBLEsFromDb();
        }



        if(storedDevices!=null && storedDevices.size()>0) {
            for (LocationEntity bleEntity : storedDevices) {

                LatLng desttLng = new LatLng(Double.valueOf(bleEntity.getLatitude()), Double.valueOf(bleEntity.getLongitude()));
                Log.e("From First", getDistance(latLng, desttLng));

                float currentDistance= Float.valueOf(getDistance(latLng, desttLng));
                float savedDistance=Float.valueOf(bleEntity.getLocationmeters());
                if(currentDistance<=savedDistance)
                {
                    playMedia(bleEntity.getLocationname(), 100, true);
                }

                /*float lat = (float) location.getLatitude();
                float lon = (float) location.getLongitude();

                Log.e("From SECond", getDistance(lat, lon, Float.valueOf(bleEntity.getLatitude()), Float.valueOf(bleEntity.getLatitude())));*/
            /*if (bleEntity.getLatitude().equals(scanResult.getScanRecord().getDeviceName()) &&
                    rssi >= bleEntity.getRSS() && !bleDao.getDeviceDetails(scanResult.getScanRecord().getDeviceName()).isShown()) {
                Log.e("DEVNAME STORED" ,""+scanResult.getScanRecord().getDeviceName()+"==="+rssi);
                if(!isMediaPlaying) {
                    playMedia(bleEntity.getName(), rssi, true);
                }
            }*/
            }
        }
    }

    private boolean checkLocation() {
        if(!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public static String getDistance(LatLng latlngA, LatLng latlngB) {
        Location locationA = new Location("point A");

        locationA.setLatitude(latlngA.latitude);
        locationA.setLongitude(latlngA.longitude);

        Location locationB = new Location("point B");

        locationB.setLatitude(latlngB.latitude);
        locationB.setLongitude(latlngB.longitude);

        //float distance = locationA.distanceTo(locationB)/1000;//To convert Meter in Kilometer
        float distance = locationA.distanceTo(locationB);//To convert Meter in Kilometer
        return String.format("%.2f", distance);
    }

    public static String getDistance(float lat_a, float lng_a, float lat_b, float lng_b) {
        // earth radius is in mile
        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat_b - lat_a);
        double lngDiff = Math.toRadians(lng_b - lng_a);
        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2)
                + Math.cos(Math.toRadians(lat_a))
                * Math.cos(Math.toRadians(lat_b)) * Math.sin(lngDiff / 2)
                * Math.sin(lngDiff / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;

        int meterConversion = 1609;
        double kmConvertion = 1.6093;

        return String.format("%.2f", new Float(distance * meterConversion).floatValue()) + " meters";
        //return new Float(distance * meterConversion).floatValue();
        //return String.format("%.2f", new Float(distance * kmConvertion).floatValue()) + " km";
        // return String.format("%.2f", distance)+" m";
    }
}
