package com.sansolutions.esanlocationscanner.db

import androidx.room.Entity
import androidx.room. ColumnInfo
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "ESANLOCATION")
data class LocationEntity(
    @PrimaryKey(
        autoGenerate
        = true
    ) var id: Int?,
    @ColumnInfo (name ="latitude") var latitude: String,
    @ColumnInfo(name = "longitude") var longitude: String,
    @ColumnInfo (name ="locationname") var locationname: String,
    @ColumnInfo (name ="locationmeters") var locationmeters: String,
    @ColumnInfo (name ="arrivedmeters") var arrivedmeters: String,
    @ColumnInfo (name ="isArrived") var isArrived: Boolean,
    @ColumnInfo(name = "isShown") var isShown: Boolean):
Serializable{

    constructor(): this(null, "", "","","","" ,false,false)
}