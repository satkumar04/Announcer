package com.sansolutions.esanlocationscanner

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sansolutions.esanlocationscanner.db.LocationDAO
import com.sansolutions.esanblescanner.db.DatabaseHandler
import com.sansolutions.esanlocationscanner.db.LocationEntity
import kotlinx.android.synthetic.main.activity_add_beacon.*


class EditaBLEInformationActivity : AppCompatActivity() {

    var db : DatabaseHandler? = null
    var bleDao: LocationDAO? = null
    lateinit var locationName :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_beacon)
        back.setOnClickListener { finish() }
        val bleEditEntity =intent?.getSerializableExtra("edit")
        if(bleEditEntity!=null) {
            bleEditEntity as LocationEntity
            locationName=bleEditEntity.locationname
            txt_locName.text = locationName
            edit_latitude.setText(bleEditEntity.latitude)
            edit_longitude.setText(bleEditEntity.longitude)
            edit_location_meters.setText(bleEditEntity.locationmeters)
            edit_arrived_meters.setText(bleEditEntity.arrivedmeters)
        }
        db=DatabaseHandler.getDatabase( this)
        bleDao= db?.locationDao()
        save.setOnClickListener {
            var bleEntity: LocationEntity = LocationEntity()
            bleEntity.locationname = txt_locName.text.toString()
            bleEntity.latitude = edit_latitude.text.toString()
            bleEntity.longitude =edit_longitude.text.toString()
            bleEntity.locationmeters =edit_location_meters.text.toString()
            bleEntity.arrivedmeters =edit_arrived_meters.text.toString()
            if (bleEditEntity != null)
                bleDao?.updateBLE(locationName,bleEntity.latitude, bleEntity.longitude, bleEntity.locationmeters,bleEntity.arrivedmeters)
            else {
                bleEntity.locationname ="Default"
                bleDao?.insertBLE(bleEntity)
            }
            finish()
        }
    }
}