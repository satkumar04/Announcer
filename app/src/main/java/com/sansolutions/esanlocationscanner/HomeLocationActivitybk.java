package com.sansolutions.esanlocationscanner;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
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

public class HomeLocationActivitybk extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener{

    private Location location;
    private GoogleApiClient googleApiClient;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private LocationRequest locationRequest;
    private static final long UPDATE_INTERVAL = 5000, FASTEST_INTERVAL = 5000; // = 5 seconds
    // lists for permissions
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    // integer for permissions results request
    private static final int ALL_PERMISSIONS_RESULT = 1011;
    //private TextView mLatitudeTextView,playing_nameTextView;
   // private TextView mLongitudeTextView;
    private String baseFilePath = Environment.getExternalStorageDirectory() + "/Download/ESAN_GEO_MUSIC_FILES/";
    public static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1989;
    private MediaPlayer mediaPlayer = null;
    private DatabaseHandler databaseHandler;
    private LocationDAO bleDao;
    private List<LocationEntity> storedDevices;

    private boolean isMediaPlaying=false;
    boolean scanning =false;
    private ImageView startImageView;
    private LocationManager mLocationManager;
    private TextView locationTextView,locationValueTextView;
    private AudioManager audioManager;
    private int volume =5;
    private ImageView volumeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landscape_home_screen);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "ESANGEOBLEScanner::WAKELOCK");
        wakeLock.acquire();
        mLocationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        createFolder();
        databaseHandler = DatabaseHandler.Companion.getDatabase(this);
        bleDao = databaseHandler.locationDao();
        storedDevices = new ArrayList();
        storedDevices = bleDao.getAllBLEsFromDb();

        audioManager=(AudioManager) getSystemService(Context.AUDIO_SERVICE);
        startImageView=findViewById(R.id.start_button);
        volumeView=findViewById(R.id.volume_button);

        volumeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,volume++,0);
                audioManager.adjustVolume(AudioManager.ADJUST_RAISE,AudioManager.FLAG_PLAY_SOUND);
            }});
        startImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeLocationActivitybk.this,RouteActivityList.class));
            /*    if(!scanning)
                {
                    startLocationUpdates();
                    startImageView.setImageResource(R.drawable.stop_btn);
                    Log.e("BLUETOOTH" ,"STARTED");

                }
                else
                {
                    Log.e("BLUETOOTH" ,"STOPPED");
                    stopMedia();
                }
                scanning=!scanning;*/
            }
        });

        findViewById(R.id.settings_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPasswordLayout();            }
        });
       /* mLatitudeTextView = (TextView) findViewById((R.id.latitude_textview));
        mLongitudeTextView = (TextView) findViewById((R.id.longitude_textview));
        playing_nameTextView = (TextView) findViewById((R.id.playing_name));*/
        locationTextView=findViewById(R.id.locataion_tv);
        locationValueTextView=findViewById(R.id.location_text_value);


        // we add permissions we need to request location of the users
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionsToRequest = permissionsToRequest(permissions);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (permissionsToRequest.size() > 0) {
                        requestPermissions(permissionsToRequest.toArray(
                                new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
                    }
                }
            }
        });



        // we build google api client
        googleApiClient = new GoogleApiClient.Builder(this).
                addApi(LocationServices.API).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).build();
    }


    private void stopMedia()
    {

        if (googleApiClient != null  &&  googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
        new Handler().post(new Runnable() {
            @Override
            public void run() {

                if(mediaPlayer!=null && mediaPlayer.isPlaying())
                {
                    Log.e("MEDIA","STOPPED");
                    mediaPlayer.stop();
                    mediaPlayer=null;
                    isMediaPlaying=false;
                }
                    updateALLBLEStatusToDefault();
                    Log.e("SCAN","STOPPED");
                    scanning=false;
                    startImageView.setImageResource(R.drawable.start_btn);
                    locationValueTextView.setVisibility(View.GONE);

            }
        });


    }


    private void updateALLBLEStatusToDefault()
    {
        List<LocationEntity> bleEntityList=new ArrayList<>();
        bleEntityList=bleDao.getAllBLEsFromDb();
        if(bleEntityList!=null && bleEntityList.size()>0)
        {
            List<LocationEntity> updatedBleEntitiesList=new ArrayList<>();
            for (LocationEntity bleEntity : bleEntityList)
            {
                bleEntity.setShown(false);
                updatedBleEntitiesList.add(bleEntity);
            }
            bleDao.updateBLEStatusFalse(updatedBleEntitiesList);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMedia();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
      /*  new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (permissionsToRequest.size() > 0) {
                        requestPermissions(permissionsToRequest.toArray(
                                new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
                    }
                }
            }
        });
        checkLocation();*/ //check whether location service is enable or not in your  phone
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
    private ArrayList<String> permissionsToRequest(ArrayList<String> wantedPermissions) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wantedPermissions) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkLocation();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.toArray(
                        new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }
        }
        if (googleApiClient != null) {
            googleApiClient.connect();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!checkPlayServices()) {
            locationValueTextView.setText("You need to install Google Play Services to use the App properly");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    protected void onStop() {
        super.onStop();
        // stop location updates
        if (googleApiClient != null  &&  googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }

        stopMedia();
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                finish();
            }

            return false;
        }

        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&  ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Permissions ok, we get last location
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        /*if (location != null) {
            mLatitudeTextView.setText(String.valueOf(location.getLatitude()));
            mLongitudeTextView.setText(String.valueOf(location.getLongitude() ));
        }*/
    }


    private void startLocationUpdates() {

        if (googleApiClient != null && !googleApiClient.isConnected())
        {
            googleApiClient.connect();
            delayLocationUpdatesForSeconds();
        }
        else if(googleApiClient != null && googleApiClient.isConnected()) {
            requestLocationUpdates();
        }
        else if(googleApiClient==null)
        {
            googleApiClient = new GoogleApiClient.Builder(this).
                    addApi(LocationServices.API).
                    addConnectionCallbacks(this).
                    addOnConnectionFailedListener(this).build();
            delayLocationUpdatesForSeconds();
        }
    }

    private void delayLocationUpdatesForSeconds()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestLocationUpdates();
            }
        },8000);
    }

    private void requestLocationUpdates()
    {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You need to enable permissions to display location !", Toast.LENGTH_SHORT).show();
        }
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if(googleApiClient!=null && googleApiClient.isConnected()) {
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }
    }
    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.e("PERMISSION","onLocationChanged");

            String msg = "Updated Location: " +
                    Double.toString(location.getLatitude()) + "," +
                    Double.toString(location.getLongitude());

            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

          // You can now create a LatLng Object for use with maps
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

            //getDistance()
            // getDistance()
            if (storedDevices == null) {
                storedDevices = new ArrayList<>();
            }
            storedDevices = bleDao.getAllBLEsFromDb();


            if(storedDevices!=null && storedDevices.size()>0) {
                for (LocationEntity bleEntity : storedDevices) {

                    LatLng desttLng = new LatLng(Double.valueOf(bleEntity.getLatitude()), Double.valueOf(bleEntity.getLongitude()));
                    Log.e("From First", getDistance(latLng, desttLng));

                    float currentDistance= Float.valueOf(getDistance(latLng, desttLng));
                    float savedDistance=Float.valueOf(bleEntity.getLocationmeters());
                    if(currentDistance<=savedDistance && !bleDao.getDeviceDetails(bleEntity.getLocationname()).isShown())
                    {
                        if(!isMediaPlaying) {
                            playMedia(bleEntity.getLocationname(), 100, true);
                        }
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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {


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

            case ALL_PERMISSIONS_RESULT:
                for (String perm : permissionsToRequest) {
                    if (!hasPermission(perm)) {
                        permissionsRejected.add(perm);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            new AlertDialog.Builder(HomeLocationActivitybk.this).
                                    setMessage("These permissions are mandatory to get your location. You need to allow them.").
                                    setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.
                                                        toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    }).setNegativeButton("Cancel", null).create().show();

                            return;
                        }
                    }
                } else {
                    if (googleApiClient != null) {
                        googleApiClient.connect();
                    }
                }



                break;
        }
    }


    private void createFolder() {
        try {
            if (isExternalStorageMounted()) {

                int writeExternalStoragePermission =
                        ContextCompat.checkSelfPermission(HomeLocationActivitybk.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(HomeLocationActivitybk.this, new
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

    private MediaPlayer selectMediaFileDatabase(String name) {
        List<LocationEntity> bleEntities = bleDao.getAllBLEsFromDb();
        if (bleEntities != null && bleEntities.size() > 0) {
            for (LocationEntity bleEntity : bleEntities) {
                if (name.equals(bleEntity.getLocationname())) {

                    try {
                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setDataSource(baseFilePath + bleEntity.getLocationname() + ".mp3");
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return mediaPlayer;
    }
    public static boolean isExternalStorageMounted() {
        String dirState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(dirState))
            return true;
        else
            return false;
    }

    public void playMedia(String deviceName, int rssi, boolean isStatus) {
        Log.e("MEDIAPLAYING","deviceName");
        mediaPlayer = selectMediaFileDatabase(deviceName);
        mediaPlayer.start();
        isMediaPlaying=true;
        locationValueTextView.setVisibility(View.VISIBLE);
        locationValueTextView.setText(deviceName);
         updateALLBLEStatus(deviceName, isStatus);
      /*  locationValueTextView.setVisibility(View.VISIBLE);
        locationValueTextView.setText(deviceName + "==" + rssi);
        updateALLBLEStatus(deviceName, isStatus);*/
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.e("MEDIAPLAYING","stopped");
                isMediaPlaying=false;
                locationValueTextView.setVisibility(View.GONE);

            }
        });
    }
    private void updateALLBLEStatus(String deviceName,boolean isStatus)
    {
        List<LocationEntity> bleEntityList=new ArrayList<>();
        bleEntityList=bleDao.getAllBLEsFromDb();
        List<LocationEntity> updatedBleEntitiesList=new ArrayList<>();
        for (LocationEntity bleEntity : bleEntityList)
        {
            if(bleEntity.getLocationname().equals(deviceName))
                bleEntity.setShown(isStatus);
            else
                bleEntity.setShown(false);

            updatedBleEntitiesList.add(bleEntity);
        }
        bleDao.updateBLEStatusFalse(updatedBleEntitiesList);
    }

    private void showPasswordLayout()
    {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.password_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText passwordEditText = (EditText) dialogView.findViewById(R.id.password_et);
        Button cancelButton = (Button) dialogView.findViewById(R.id.cancel_dialog_button);
        Button proceedButton = (Button) dialogView.findViewById(R.id.proceed_dialog_button);

        final android.app.AlertDialog alertDialog = dialogBuilder.create();
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alertDialog!=null && alertDialog.isShowing())
                    alertDialog.dismiss();
            }
        });
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(passwordEditText!=null && passwordEditText.getText().toString().equals("9184"))
                {
                    alertDialog.dismiss();
                    stopMedia();
                    startActivity(new Intent(HomeLocationActivitybk.this,StoredBLEActivityList.class));

                }
                else
                {
                    Toast.makeText(HomeLocationActivitybk.this,"Please enter valid password",Toast.LENGTH_LONG).show();
                }

            }
        });

        alertDialog.show();
    }
}


