package com.sansolutions.esanlocationscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.sansolutions.esanlocationscanner.db.LocationDAO;
import com.sansolutions.esanblescanner.db.DatabaseHandler;
import com.sansolutions.esanlocationscanner.db.LocationEntity;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AddBLEActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText latitudeEditText, longitudeEditText, nameEditText, metersEditText;
    private Button btnShow, btnBack;
    private DatabaseHandler db;
    private LocationDAO bleDao;
    private String TAG = AddBLEActivity.class.getCanonicalName();
    private List<LocationEntity> locationList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_dev_lyt);


      /*  btnShow = findViewById(R.id.btnShow);
        btnShow.setOnClickListener(this);
        btnBack.setOnClickListener(this);*/
        db = DatabaseHandler.Companion.getDatabase(this);
        bleDao = db.locationDao();
        readExcelFileFromSdcard();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnShow:


                break;

        }
    }

    private void addDeviceInDB(String latitude, String longitude, String name, String meters,String arrivingMeter) {
        LocationEntity bleEntity = new LocationEntity();
        bleEntity.setLocationname(name);
        bleEntity.setLocationmeters(meters);
        bleEntity.setLatitude(latitude);
        bleEntity.setLatitude(latitude);
        bleEntity.setLongitude(longitude);
        bleEntity.setArrivedmeters(arrivingMeter);
        bleDao.insertBLE(bleEntity);
    }

    public void readExcelFileFromSdcard() {

        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), "Announcer.xls");
        if (!file.mkdirs()) {
            Log.e(TAG, "Directory not created");
        }
        try {
            //  open excel sheet
            // Create a POI File System object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(file);
            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

            // Get the first sheet from workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);
            // We now need something to iterate through the cells.
            Iterator<Row> rowIter = mySheet.rowIterator();
            int rowno = 0;

            while (rowIter.hasNext()) {
                LocationEntity defaultConfig = null;
                Log.e(TAG, " row no " + rowno);
                HSSFRow myRow = (HSSFRow) rowIter.next();
                if (rowno != 0) {
                    Iterator<Cell> cellIter = myRow.cellIterator();
                    int colno = 0;
                    defaultConfig = new LocationEntity();
                    while (cellIter.hasNext()) {
                        HSSFCell myCell = (HSSFCell) cellIter.next();
                        if (colno == 0) {
                            defaultConfig.setLocationname(myCell.toString());
                        } else if (colno == 1) {
                            try {
                                defaultConfig.setLatitude(myCell.toString());
                            } catch (Exception ex) {
                                defaultConfig.setLatitude("0");
                                Log.e(TAG, "error " + ex.toString());
                            }
                        } else if (colno == 2) {
                            try {
                                defaultConfig.setLongitude(myCell.toString());
                            } catch (Exception ex) {
                                defaultConfig.setLongitude("0");
                                Log.e(TAG, "error " + ex.toString());
                            }
                        } else if (colno == 3) {
                            try {
                                defaultConfig.setLocationmeters(myCell.toString());
                            } catch (Exception ex) {
                                defaultConfig.setLocationmeters("0");
                                Log.e(TAG, "error " + ex.toString());
                            }
                        }

                        else if (colno == 4) {
                            try {
                                defaultConfig.setArrivedmeters(myCell.toString());
                            } catch (Exception ex) {
                                defaultConfig.setLocationmeters("0");
                                Log.e(TAG, "error " + ex.toString());
                            }
                        }
                        colno++;
                        Log.e(TAG, " Index :" + myCell.getColumnIndex() + " -- " + myCell.toString());
                    }
                    locationList.add(defaultConfig);
                }
                rowno++;
            }

            if(!locationList.isEmpty()) {

                bleDao.deleteAllFromTable();
                for (LocationEntity entity : locationList) {
                    addDeviceInDB(entity.getLatitude(), entity.getLongitude(), entity.getLocationname(), entity.getLocationmeters(),entity.getArrivedmeters());
                }

                startActivity(new Intent(this, StoredBLEActivityList.class));
                finish();
            }
        } catch (Exception e) {
            Log.e(TAG, "error " + e.toString());
        }
    }
}
