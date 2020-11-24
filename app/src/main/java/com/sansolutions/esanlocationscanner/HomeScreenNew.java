/*
package com.sansolutions.esanlocationscanner;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.*;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.WindowManager;
import android.widget.*;
import com.sansolutions.esanlocationscanner.db.LocationDAO;
import com.sansolutions.esanblescanner.db.BLEEntity;
import com.sansolutions.esanblescanner.db.DatabaseHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeScreenNew extends AppCompatActivity {

    private ImageView startImageView,settingsImageView;
    private TextView locationTextView,locationValueTextView;

    private MediaPlayer mediaPlayer = null;
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private DatabaseHandler databaseHandler;
    private LocationDAO bleDao;
    private List<BLEEntity> storedDevices;

    private List<BLEEntity> addBLEDevices;
    private BluetoothLeScanner scanner;
    boolean scanning =false;
    private boolean isMediaPlaying = false;


    private String baseFilePath = Environment.getExternalStorageDirectory() + "/Download/ESAN_MUSIC_FILES/";
    private String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN};


    public static final int MULTIPLE_PERMISSIONS = 101;
    public static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    public static final int REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "ESANBLEScanner::WAKELOCK");
        wakeLock.acquire();
        initIds();
        createFolder();
        databaseHandler = DatabaseHandler.Companion.getDatabase(this);
        bleDao = databaseHandler.bleDao();
        storedDevices = new ArrayList();
        storedDevices = bleDao.getAllBLEsFromDb();

        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)){
            Toast.makeText(this,"BLE Not Supported",Toast.LENGTH_SHORT).show();
            enableBluetooth();
        }

        bluetoothManager= (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        bluetoothAdapter=bluetoothManager.getAdapter();

        if(bluetoothAdapter == null )
        {
            Toast.makeText(this,"BLE Not Supported",Toast.LENGTH_SHORT).show();
            enableBluetooth();
            return;
        }

        if(bluetoothAdapter == null || !bluetoothAdapter.isEnabled())
        {
            Toast.makeText(this,"BLE Not Enabled",Toast.LENGTH_SHORT).show();
            enableBluetooth();
            return;
        }

    }

    private void stopMedia()
    {
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
                if(scanner!=null && scanCallback!=null)
                {
                    Log.e("SCAN","STOPPED");
                    scanning=false;
                    startImageView.setImageResource(R.drawable.start_btn);
                    locationValueTextView.setVisibility(View.GONE);
                    scanner.stopScan(scanCallback);
                }
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        stopMedia();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMedia();
    }

    private void initIds()
    {
        startImageView=findViewById(R.id.start_button);
        settingsImageView=findViewById(R.id.settings_button);
        locationTextView=findViewById(R.id.locataion_tv);
        locationValueTextView=findViewById(R.id.location_text_value);

        startImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBLEDevices=new ArrayList<>();
                if(!scanning)
                {
                    startImageView.setImageResource(R.drawable.stop_btn);
                    scanner=bluetoothAdapter.getBluetoothLeScanner();
                    scanner.startScan(getScanFilter(),getScanSettings(),scanCallback);
                    Log.e("BLUETOOTH" ,"STARTED");

                }
                else
                {
                    Log.e("BLUETOOTH" ,"STOPPED");
                    addBLEDevices.clear();
                    //startImageView.setImageResource(R.drawable.start_btn);
                    //scanner.stopScan(scanCallback);
                    stopMedia();


                }
                scanning=!scanning;
            }
        });
        settingsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showPasswordLayout();
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean arePermissionsEnabled() {
        for (String permission : permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestMultiplePermissions() {
        List<String> remainingPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                remainingPermissions.add(permission);
            }
        }
        requestPermissions(remainingPermissions.toArray(new String[remainingPermissions.size()]), MULTIPLE_PERMISSIONS);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissionsList[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    String permissionsDenied = "";
                    for (String per : permissionsList) {
                        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                            permissionsDenied = "\n" + per;

                        }
                    }
                }
                return;
            }
            case REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION: {
                if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION) {
                    int grantResultsLength = grantResults.length;
                    if (grantResultsLength > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + "/Download/ESAN_MUSIC_FILES/");
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

    @Override
    protected void onResume() {
        super.onResume();
        enableBluetooth();
    }

    private void enableBluetooth() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (arePermissionsEnabled()) {
                if (!bluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
            } else {
                requestMultiplePermissions();
            }
        }
    }
    private ScanSettings getScanSettings() {
        return new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).setReportDelay(300).build();
    }

    private List<ScanFilter> getScanFilter() {
        return Collections.emptyList();
    }

    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {

            Log.e("scanCallback :::::" ,"onScanResult CALLED");
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            Log.e("scanCallback" ,"onBatchScanResults CALLED");
            if(storedDevices!=null)
            {
                storedDevices = bleDao.getAllBLEsFromDb();
            }
            else
            {
                storedDevices=new ArrayList<>();
                storedDevices = bleDao.getAllBLEsFromDb();
            }

            for (ScanResult scanResult : results) {

                if(scanResult.getScanRecord().getDeviceName()!=null && scanResult.getScanRecord().getDeviceName().length()>0)
                {
                int rssi = scanResult.getRssi();
                    Log.e("DEVNAME" ,""+scanResult.getScanRecord().getDeviceName()+"==="+rssi);
                //addDeviceCall(scanResult.getScanRecord().getDeviceName(), rssi);
                for (BLEEntity bleEntity : storedDevices) {

                    if (bleEntity.getName().equals(scanResult.getScanRecord().getDeviceName()) &&
                            rssi >= bleEntity.getRSS() && !bleDao.getDeviceDetails(scanResult.getScanRecord().getDeviceName()).isShown()) {
                        Log.e("DEVNAME STORED" ,""+scanResult.getScanRecord().getDeviceName()+"==="+rssi);
                        if(!isMediaPlaying) {
                            scanner.stopScan(scanCallback);
                            playMedia(bleEntity.getName(), rssi, true);
                        }
                    }
                }
                }
                */
/*else
                {
                    if (!addBLEDevices.contains(scanResult.getScanRecord().getDeviceName())) {
                        BLEEntity bleEntit=new BLEEntity();
                        bleEntit.setName(scanResult.getScanRecord().getDeviceName());
                        bleEntit.setRSS(rssi);
                        addBLEDevices.add(bleEntit);
                        //Log.e("playMedia from Adapter", "Started Name :: " + "" + device);
                    } else
                        {
                            addBLEDevices.remove(bleEntity);
                            BLEEntity bleEntity1=new BLEEntity();
                            bleEntity1.setName(scanResult.getScanRecord().getDeviceName());
                            bleEntity1.setRSS(rssi);
                            addBLEDevices.add(bleEntity1);
                        }
                }
                }*//*

            }
        }

        @Override
        public void onScanFailed(int errorCode) {

            Log.e("scanCallback :::::" ,"onScanFailed CALLED");

        }
    };

    private void addDeviceCall(String device, int rssi) {
        if (device != null) {

            if (!addBLEDevices.contains(device)) {
                BLEEntity bleEntity=new BLEEntity();
                bleEntity.setName(device);
                bleEntity.setRSS(rssi);
                addBLEDevices.add(bleEntity);
                Log.e("playMedia from Adapter", "Started Name :: " + "" + device);
            } else {
                for (BLEEntity bleEntity : addBLEDevices)
                {

                        if (bleEntity.getName().equals(device) &&
                                rssi >= bleEntity.getRSS() && !bleDao.getDeviceDetails(device).isShown()) {
                            Log.e("RSSI MATCHED", "startMEDIA");
                            scanner.stopScan(scanCallback);

                            playMedia(bleEntity.getName(), rssi, true);
                        }
                        else
                        {
                            addBLEDevices.remove(bleEntity);
                            BLEEntity bleEntity1=new BLEEntity();
                            bleEntity1.setName(device);
                            bleEntity1.setRSS(rssi);
                            addBLEDevices.add(bleEntity1);
                        }
                        */
/*addBLEDevices.remove(bleEntity);
                        BLEEntity bleEntity1=new BLEEntity();
                        bleEntity1.setName(device);
                        bleEntity1.setRSS(rssi);
                        addBLEDevices.add(bleEntity1);*//*


                }
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
    private void createFolder() {
        try {
            if (isExternalStorageMounted()) {

                int writeExternalStoragePermission =
                        ContextCompat.checkSelfPermission(HomeScreenNew.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(HomeScreenNew.this, new
                                    String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
                } else {
                    File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + "/Download/ESAN_MUSIC_FILES/");

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {

            Toast.makeText(this, "ENABLE BLUETOOTH TO CONTINUE", Toast.LENGTH_SHORT).show();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void playMedia(String deviceName, int rssi, boolean isStatus) {
        Log.e("MEDIAPLAYING","deviceName");
        isMediaPlaying=true;
        mediaPlayer = selectMediaFileDatabase(deviceName);
        mediaPlayer.start();
        locationValueTextView.setVisibility(View.VISIBLE);
        locationValueTextView.setText(deviceName + "==" + rssi);
        updateALLBLEStatus(deviceName, isStatus);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.e("MEDIAPLAYING","stopped");
                isMediaPlaying=false;
                locationValueTextView.setVisibility(View.GONE);
                scanner.startScan(getScanFilter(), getScanSettings(), scanCallback);
            }
        });
    }
    private void updateALLBLEStatus(String deviceName,boolean isStatus)
    {
        List<BLEEntity> bleEntityList=new ArrayList<>();
        bleEntityList=bleDao.getAllBLEsFromDb();
        List<BLEEntity> updatedBleEntitiesList=new ArrayList<>();
        for (BLEEntity bleEntity : bleEntityList)
        {
            if(bleEntity.getName().equals(deviceName))
                bleEntity.setShown(isStatus);
            else
                bleEntity.setShown(false);

            updatedBleEntitiesList.add(bleEntity);
        }
        bleDao.updateBLEStatusFalse(updatedBleEntitiesList);
    }
    private MediaPlayer selectMediaFileDatabase(String name) {
        List<BLEEntity> bleEntities = bleDao.getAllBLEsFromDb();
        if (bleEntities != null && bleEntities.size() > 0) {
            for (BLEEntity bleEntity : bleEntities) {
                if (name.equals(bleEntity.getName())) {

                    try {
                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setDataSource(baseFilePath + bleEntity.getName() + ".mp3");
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }
        }
        return mediaPlayer;
    }

private void showPasswordLayout()
{
    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
    LayoutInflater inflater = this.getLayoutInflater();
    View dialogView = inflater.inflate(R.layout.password_dialog, null);
    dialogBuilder.setView(dialogView);

    final EditText passwordEditText = (EditText) dialogView.findViewById(R.id.password_et);
    Button cancelButton = (Button) dialogView.findViewById(R.id.cancel_dialog_button);
    Button proceedButton = (Button) dialogView.findViewById(R.id.proceed_dialog_button);

    final AlertDialog alertDialog = dialogBuilder.create();
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
                startActivity(new Intent(HomeScreenNew.this, StoredBLEActivityList.class));

            }
            else
                {
                    Toast.makeText(HomeScreenNew.this,"Please enter valid password",Toast.LENGTH_LONG).show();
                }

        }
    });

    alertDialog.show();
}
}
*/
