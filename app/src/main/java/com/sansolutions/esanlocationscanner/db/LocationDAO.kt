package com.sansolutions.esanlocationscanner.db

import androidx.room.*

@Dao
interface LocationDAO
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBLE(bleEntity: LocationEntity)

    @Query("Select * from ESANLOCATION")
    fun getAllBLEsFromDb():List<LocationEntity>

    @Query("Select * from ESANLOCATION WHERE locationname = :locationname")
    fun getDeviceDetails(locationname : String): LocationEntity

 @Query("UPDATE ESANLOCATION SET latitude=:latitude,longitude=:longitude,locationmeters=:locationmeters,arrivedmeters=:arrivedmeters WHERE locationname=:locationname")
    fun updateBLE(locationname :String, latitude :String,longitude :String,locationmeters :String,arrivedmeters :String)

    @Delete
    fun deletaBLE(bleEntity: LocationEntity)

    @Update
    fun updateBLEStatusFalse(listaBle:List<LocationEntity>)

    @Update
    fun updataBLEValues (bleEntity: LocationEntity)

    @Query("DELETE FROM ESANLOCATION")
    fun deleteAllFromTable()

    @Query("UPDATE ESANLOCATION SET isArrived= :isArrived WHERE locationname=:locationname")
    fun updateBLEArrivedStatus(locationname :String,isArrived:Boolean)

    @Query("UPDATE ESANLOCATION SET isShown= :isShown WHERE locationname=:locationname")
    fun updateBLEShownStatus(locationname :String,isShown:Boolean)
}