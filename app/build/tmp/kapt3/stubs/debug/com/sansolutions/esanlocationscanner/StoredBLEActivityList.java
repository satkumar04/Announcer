package com.sansolutions.esanlocationscanner;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u000f\u001a\u00020\u0010H\u0002J\b\u0010\u0011\u001a\u00020\u0012H\u0002J\u000e\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u0015J\"\u0010\u0016\u001a\u00020\u00122\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00182\b\u0010\u001a\u001a\u0004\u0018\u00010\u001bH\u0014J\u0012\u0010\u001c\u001a\u00020\u00122\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0014J\u000e\u0010\u001f\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u0015R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001c\u0010\t\u001a\u0004\u0018\u00010\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000e\u00a8\u0006 "}, d2 = {"Lcom/sansolutions/esanlocationscanner/StoredBLEActivityList;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "bleDao", "Lcom/sansolutions/esanlocationscanner/db/LocationDAO;", "getBleDao", "()Lcom/sansolutions/esanlocationscanner/db/LocationDAO;", "setBleDao", "(Lcom/sansolutions/esanlocationscanner/db/LocationDAO;)V", "db", "Lcom/sansolutions/esanblescanner/db/DatabaseHandler;", "getDb", "()Lcom/sansolutions/esanblescanner/db/DatabaseHandler;", "setDb", "(Lcom/sansolutions/esanblescanner/db/DatabaseHandler;)V", "callConfirmDialogue", "Landroidx/appcompat/app/AlertDialog;", "checkStoredBle", "", "deleteItemClicked", "bleEntity", "Lcom/sansolutions/esanlocationscanner/db/LocationEntity;", "onActivityResult", "requestCode", "", "resultCode", "data", "Landroid/content/Intent;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "partItemClicked", "app_debug"})
public final class StoredBLEActivityList extends androidx.appcompat.app.AppCompatActivity {
    @org.jetbrains.annotations.Nullable()
    private com.sansolutions.esanblescanner.db.DatabaseHandler db;
    @org.jetbrains.annotations.Nullable()
    private com.sansolutions.esanlocationscanner.db.LocationDAO bleDao;
    private java.util.HashMap _$_findViewCache;
    
    @org.jetbrains.annotations.Nullable()
    public final com.sansolutions.esanblescanner.db.DatabaseHandler getDb() {
        return null;
    }
    
    public final void setDb(@org.jetbrains.annotations.Nullable()
    com.sansolutions.esanblescanner.db.DatabaseHandler p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.sansolutions.esanlocationscanner.db.LocationDAO getBleDao() {
        return null;
    }
    
    public final void setBleDao(@org.jetbrains.annotations.Nullable()
    com.sansolutions.esanlocationscanner.db.LocationDAO p0) {
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final androidx.appcompat.app.AlertDialog callConfirmDialogue() {
        return null;
    }
    
    private final void checkStoredBle() {
    }
    
    public final void partItemClicked(@org.jetbrains.annotations.NotNull()
    com.sansolutions.esanlocationscanner.db.LocationEntity bleEntity) {
    }
    
    public final void deleteItemClicked(@org.jetbrains.annotations.NotNull()
    com.sansolutions.esanlocationscanner.db.LocationEntity bleEntity) {
    }
    
    @java.lang.Override()
    protected void onActivityResult(int requestCode, int resultCode, @org.jetbrains.annotations.Nullable()
    android.content.Intent data) {
    }
    
    public StoredBLEActivityList() {
        super();
    }
}