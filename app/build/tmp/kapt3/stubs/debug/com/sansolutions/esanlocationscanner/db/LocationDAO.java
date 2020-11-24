package com.sansolutions.esanlocationscanner.db;

import java.lang.System;

@androidx.room.Dao()
@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0005\bg\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\'J\b\u0010\u0006\u001a\u00020\u0003H\'J\u000e\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00050\bH\'J\u0010\u0010\t\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u000bH\'J\u0010\u0010\f\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\'J\u0010\u0010\r\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\'J0\u0010\u000e\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u000b2\u0006\u0010\u0011\u001a\u00020\u000b2\u0006\u0010\u0012\u001a\u00020\u000bH\'J\u0018\u0010\u0013\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0014\u001a\u00020\u0015H\'J\u0018\u0010\u0016\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0017\u001a\u00020\u0015H\'J\u0016\u0010\u0018\u001a\u00020\u00032\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00050\bH\'\u00a8\u0006\u001a"}, d2 = {"Lcom/sansolutions/esanlocationscanner/db/LocationDAO;", "", "deletaBLE", "", "bleEntity", "Lcom/sansolutions/esanlocationscanner/db/LocationEntity;", "deleteAllFromTable", "getAllBLEsFromDb", "", "getDeviceDetails", "locationname", "", "insertBLE", "updataBLEValues", "updateBLE", "latitude", "longitude", "locationmeters", "arrivedmeters", "updateBLEArrivedStatus", "isArrived", "", "updateBLEShownStatus", "isShown", "updateBLEStatusFalse", "listaBle", "app_debug"})
public abstract interface LocationDAO {
    
    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    public abstract void insertBLE(@org.jetbrains.annotations.NotNull()
    com.sansolutions.esanlocationscanner.db.LocationEntity bleEntity);
    
    @org.jetbrains.annotations.NotNull()
    @androidx.room.Query(value = "Select * from ESANLOCATION")
    public abstract java.util.List<com.sansolutions.esanlocationscanner.db.LocationEntity> getAllBLEsFromDb();
    
    @org.jetbrains.annotations.NotNull()
    @androidx.room.Query(value = "Select * from ESANLOCATION WHERE locationname = :locationname")
    public abstract com.sansolutions.esanlocationscanner.db.LocationEntity getDeviceDetails(@org.jetbrains.annotations.NotNull()
    java.lang.String locationname);
    
    @androidx.room.Query(value = "UPDATE ESANLOCATION SET latitude=:latitude,longitude=:longitude,locationmeters=:locationmeters,arrivedmeters=:arrivedmeters WHERE locationname=:locationname")
    public abstract void updateBLE(@org.jetbrains.annotations.NotNull()
    java.lang.String locationname, @org.jetbrains.annotations.NotNull()
    java.lang.String latitude, @org.jetbrains.annotations.NotNull()
    java.lang.String longitude, @org.jetbrains.annotations.NotNull()
    java.lang.String locationmeters, @org.jetbrains.annotations.NotNull()
    java.lang.String arrivedmeters);
    
    @androidx.room.Delete()
    public abstract void deletaBLE(@org.jetbrains.annotations.NotNull()
    com.sansolutions.esanlocationscanner.db.LocationEntity bleEntity);
    
    @androidx.room.Update()
    public abstract void updateBLEStatusFalse(@org.jetbrains.annotations.NotNull()
    java.util.List<com.sansolutions.esanlocationscanner.db.LocationEntity> listaBle);
    
    @androidx.room.Update()
    public abstract void updataBLEValues(@org.jetbrains.annotations.NotNull()
    com.sansolutions.esanlocationscanner.db.LocationEntity bleEntity);
    
    @androidx.room.Query(value = "DELETE FROM ESANLOCATION")
    public abstract void deleteAllFromTable();
    
    @androidx.room.Query(value = "UPDATE ESANLOCATION SET isArrived= :isArrived WHERE locationname=:locationname")
    public abstract void updateBLEArrivedStatus(@org.jetbrains.annotations.NotNull()
    java.lang.String locationname, boolean isArrived);
    
    @androidx.room.Query(value = "UPDATE ESANLOCATION SET isShown= :isShown WHERE locationname=:locationname")
    public abstract void updateBLEShownStatus(@org.jetbrains.annotations.NotNull()
    java.lang.String locationname, boolean isShown);
}