package com.sansolutions.esanlocationscanner

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sansolutions.esanblescanner.db.DatabaseHandler
import com.sansolutions.esanlocationscanner.db.LocationDAO
import com.sansolutions.esanlocationscanner.adapters.RouteListAdapter
import kotlinx.android.synthetic.main.activity_route_list.*
import kotlinx.android.synthetic.main.activity_stored_beacon_list.back
import kotlinx.android.synthetic.main.activity_stored_beacon_list.no_ble_devices
import kotlinx.android.synthetic.main.activity_stored_beacon_list.toolbar

class RouteActivityList : AppCompatActivity()
{
    var db : DatabaseHandler?=null
    var bleDao : LocationDAO?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_list)
        back.setOnClickListener { finish() }
        toolbar.title = "";
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(View.OnClickListener {
            finish()
        })
        db = DatabaseHandler.getDatabase(this)
        bleDao = db?.locationDao();
        checkStoredBle()
    }

    private fun checkStoredBle() {
        if(bleDao?.getAllBLEsFromDb()!!.isNotEmpty())
        {
            route_list.visibility= View.VISIBLE
            route_list.layoutManager= androidx.recyclerview.widget.LinearLayoutManager(this)
            route_list.addItemDecoration(
                    androidx.recyclerview.widget.DividerItemDecoration(
                        this,
                        androidx.recyclerview.widget.DividerItemDecoration.HORIZONTAL
                    )
                )
            route_list.adapter=RouteListAdapter(bleDao?.getAllBLEsFromDb(),this)
        }
        else
        {
            route_list.visibility= View.GONE
            no_ble_devices.visibility= View.VISIBLE
        }
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when(requestCode){
            1->{
                route_list.adapter=RouteListAdapter(bleDao?.getAllBLEsFromDb(),this)
            }}
        super.onActivityResult(requestCode, resultCode, data)
    }

}