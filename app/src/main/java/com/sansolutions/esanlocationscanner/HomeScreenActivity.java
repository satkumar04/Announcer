/*
package com.sansolutions.esanlocationscanner;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sansolutions.esanlocationscanner.db.LocationDAO;
import com.sansolutions.esanblescanner.db.BLEEntity;
import com.sansolutions.esanblescanner.db.DatabaseHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeScreenActivity extends AppCompatActivity implements CallMedia {


    private MediaPlayer mediaPlayer = null;
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private DatabaseHandler databaseHandler;
    private LocationDAO bleDao;
    private List<BLEEntity> storedDevices;
    private Button button, buttonSettings;

    private ListView listView;
    private TextView playingBleName;
    private BleList bleList = null;
    private BluetoothLeScanner scanner;


    boolean scanning =false;


    private String baseFilePath = Environment.getExternalStorageDirectory() + "/Download/ESAN_MUSIC_FILES/";


    private String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN};


    public static final int MULTIPLE_PERMISSIONS = 101;
    public static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    public static final int REQUEST_ENABLE_BT = 1;


    static class ViewHolder {
        TextView deviceName;
        TextView deviceRssi;
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


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_screen_list_activity);
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

        bleList=new BleList(this,this);
        listView=findViewById(R.id.listView);
        playingBleName=findViewById(R.id.playing_ble_view);
        listView.setAdapter(bleList);

        button=findViewById(R.id.button);
        buttonSettings=findViewById(R.id.button_settings);
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreenActivity.this, StoredBLEActivityList.class));
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!scanning)
                {
                    button.setText("Stop Scan");
                    scanner=bluetoothAdapter.getBluetoothLeScanner();
                    scanner.startScan(getScanFilter(),getScanSettings(),scanCallback);
                }
                else
                {
                    button.setText("Start Scan");
                    scanner.stopScan(scanCallback);
                    bleList.clear();
                    bleList.notifyDataSetChanged();
                }
                scanning=!scanning;
            }
        });

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
    @Override
    public void playMedia(String deviceName, int rssi, boolean isStatus) {
        mediaPlayer = selectMediaFileDatabase(deviceName);
        mediaPlayer.start();
        playingBleName.setVisibility(View.VISIBLE);
        playingBleName.setText(deviceName + "/n/n" + rssi);
        listView.setVisibility(View.GONE);

        updateALLBLEStatus(deviceName, isStatus);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playingBleName.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                scanner.startScan(getScanFilter(), getScanSettings(), scanCallback);
            }
        });
    }


    private void createFolder() {
        try {
            if (isExternalStorageMounted()) {

                int writeExternalStoragePermission =
                        ContextCompat.checkSelfPermission(HomeScreenActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(HomeScreenActivity.this, new
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

    private void addDeviceCall(String device, int rssi) {
        if (device != null) {

            bleList.addDevice(device, rssi, storedDevices);
            bleList.notifyDataSetChanged();
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
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {

            for (ScanResult scanResult : results) {
                int rssi = scanResult.getRssi();
                addDeviceCall(scanResult.getScanRecord().getDeviceName(), rssi);
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
        }
    };

    public static boolean isExternalStorageMounted() {
        String dirState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(dirState))
            return true;
        else
            return false;
    }

    private class BleList extends BaseAdapter {
        private ArrayList<String> devices;
        private ArrayList<Integer> RSSIs;
        private LayoutInflater inflater;
        private List<BLEEntity> bleEntitiesList;
        private boolean isMediaPlaying = false;
        private Context context;
        private CallMedia callMedia;

        public BleList(Context context, CallMedia callMedia) {
            super();
            this.context = context;
            this.callMedia = callMedia;
            devices = new ArrayList<String>();
            RSSIs = new ArrayList<Integer>();
            inflater = ((Activity) HomeScreenActivity.this).getLayoutInflater();
        }

        public void addDevice(String device, int rssi, List<BLEEntity> bleEntities) {

            bleEntitiesList = bleEntities;
            if (!devices.contains(device)) {
                devices.add(device);
                RSSIs.add(rssi);
                Log.e("playMedia from Adapter", "Started Name :: " + "" + device);
            } else {
                RSSIs.set(devices.indexOf(device), rssi);

            }
        }

        public void clear() {
            devices.clear();
            RSSIs.clear();
        }

        @Override
        public int getCount() {
            return devices.size();
        }

        @Override
        public Object getItem(int position) {
            return devices.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;

            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(android.R.layout.two_line_list_item, null);
                viewHolder.deviceName = (TextView) convertView.findViewById(android.R.id.text1);
                viewHolder.deviceRssi = (TextView) convertView.findViewById(android.R.id.text2);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
//counter-counter+1;
            if (devices != null && devices.size() > 0) {

                final String deviceName = devices.get(position);
                final int rssi = RSSIs.get(position);
                viewHolder.deviceName.setText(" DEVICE NAME ==== " + deviceName);
                viewHolder.deviceRssi.setText(" DEVICE RSSI ==== " + String.valueOf(rssi));
                isRSSIMatched(deviceName, rssi);
            }
            return convertView;
        }

        private boolean isRSSIMatched(String deviceName, int rssi) {
            boolean isTrue = false;
            for (BLEEntity bleEntity : bleEntitiesList) {

                if (deviceName.equals(bleEntity.getName()) &&
                        rssi >= bleEntity.getRSS() && !bleDao.getDeviceDetails(deviceName).isShown()) {
                    Log.e("RSSI MATCHED", "startMEDIA");
                    scanner.stopScan(scanCallback);
                    bleList.clear();
                    bleList.notifyDataSetChanged();
                    callMedia.playMedia(deviceName, rssi, true);
                    isMediaPlaying = true;
                    isTrue = true;
                }
            }
            return isTrue;
        }
    }
}*/
