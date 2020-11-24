package com.sansolutions.esanblescanner.db;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import com.sansolutions.esanlocationscanner.db.LocationDAO;
import com.sansolutions.esanlocationscanner.db.LocationDAO_Impl;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class DatabaseHandler_Impl extends DatabaseHandler {
  private volatile LocationDAO _locationDAO;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `ESANLOCATION` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `latitude` TEXT NOT NULL, `longitude` TEXT NOT NULL, `locationname` TEXT NOT NULL, `locationmeters` TEXT NOT NULL, `arrivedmeters` TEXT NOT NULL, `isArrived` INTEGER NOT NULL, `isShown` INTEGER NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '959239645105bbde7256890b234e87d6')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `ESANLOCATION`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsESANLOCATION = new HashMap<String, TableInfo.Column>(8);
        _columnsESANLOCATION.put("id", new TableInfo.Column("id", "INTEGER", false, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsESANLOCATION.put("latitude", new TableInfo.Column("latitude", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsESANLOCATION.put("longitude", new TableInfo.Column("longitude", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsESANLOCATION.put("locationname", new TableInfo.Column("locationname", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsESANLOCATION.put("locationmeters", new TableInfo.Column("locationmeters", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsESANLOCATION.put("arrivedmeters", new TableInfo.Column("arrivedmeters", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsESANLOCATION.put("isArrived", new TableInfo.Column("isArrived", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsESANLOCATION.put("isShown", new TableInfo.Column("isShown", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysESANLOCATION = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesESANLOCATION = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoESANLOCATION = new TableInfo("ESANLOCATION", _columnsESANLOCATION, _foreignKeysESANLOCATION, _indicesESANLOCATION);
        final TableInfo _existingESANLOCATION = TableInfo.read(_db, "ESANLOCATION");
        if (! _infoESANLOCATION.equals(_existingESANLOCATION)) {
          return new RoomOpenHelper.ValidationResult(false, "ESANLOCATION(com.sansolutions.esanlocationscanner.db.LocationEntity).\n"
                  + " Expected:\n" + _infoESANLOCATION + "\n"
                  + " Found:\n" + _existingESANLOCATION);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "959239645105bbde7256890b234e87d6", "9e1290b5626f234394192352f4cc8bc5");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "ESANLOCATION");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `ESANLOCATION`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public LocationDAO locationDao() {
    if (_locationDAO != null) {
      return _locationDAO;
    } else {
      synchronized(this) {
        if(_locationDAO == null) {
          _locationDAO = new LocationDAO_Impl(this);
        }
        return _locationDAO;
      }
    }
  }
}
