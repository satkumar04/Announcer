package com.sansolutions.esanlocationscanner

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.sansolutions.esanlocationscanner.adapters.BeaconListAdapter
import com.sansolutions.esanlocationscanner.db.LocationDAO
import com.sansolutions.esanblescanner.db.DatabaseHandler
import com.sansolutions.esanlocationscanner.db.LocationEntity
import kotlinx.android.synthetic.main.activity_stored_beacon_list.*

class StoredBLEActivityList : AppCompatActivity()
{
    var db : DatabaseHandler?=null
    var bleDao : LocationDAO?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stored_beacon_list)
        back.setOnClickListener { finish() }
        setSupportActionBar(toolbar)
        db = DatabaseHandler.getDatabase(this)
        bleDao = db?.locationDao();
        toolbar.title = "";
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(View.OnClickListener {
            finish()
        })
        fab.setOnClickListener { view ->
            if(no_ble_devices.visibility==View.GONE) {
                callConfirmDialogue().show()
            }
            else {
                startActivityForResult(Intent(this,AddBLEActivity::class.java),1)
            finish()
            }
        }
        checkStoredBle();
    }
    private fun callConfirmDialogue():AlertDialog {
        this?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton("YES",
                    DialogInterface.OnClickListener { dialog, id ->
                        val intent = Intent(context, AddBLEActivity::class.java)
                        startActivityForResult(intent, 1)
                        finish()
                    })
                setNegativeButton("NO",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                        dialog.cancel()
                    })
            }
            builder.setCancelable(false)
            builder.setMessage("Do you want to update?")
            // Set other dialog properties

            // Create the AlertDialog
            return builder.create()
        }
    }
    private fun checkStoredBle() {
        if(bleDao?.getAllBLEsFromDb()!!.isNotEmpty())
        {
            beacon_list.visibility= View.VISIBLE
            beacon_list.layoutManager= androidx.recyclerview.widget.LinearLayoutManager(this)
                beacon_list.addItemDecoration(
                    androidx.recyclerview.widget.DividerItemDecoration(
                        this,
                        androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
                    )
                )
            beacon_list.adapter=BeaconListAdapter(bleDao?.getAllBLEsFromDb(),this,
                {partItem: LocationEntity -> partItemClicked(partItem)},{ partItem: LocationEntity -> deleteItemClicked(partItem)})
        }
        else
        {
            beacon_list.visibility= View.GONE
            no_ble_devices.visibility= View.VISIBLE
        }
    }

    fun partItemClicked(bleEntity: LocationEntity)
    {
        startActivityForResult(Intent(this,EditaBLEInformationActivity::class.java).putExtra("edit",bleEntity),1)
    }


    fun deleteItemClicked(bleEntity: LocationEntity)
    {
        bleDao?.deletaBLE(bleEntity)
        checkStoredBle()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when(requestCode){
            1->{
                beacon_list.adapter=BeaconListAdapter(bleDao?.getAllBLEsFromDb(),this,
                    {partItem: LocationEntity -> partItemClicked(partItem)},{ partItem: LocationEntity -> deleteItemClicked(partItem)})
            }}
        super.onActivityResult(requestCode, resultCode, data)
    }

}