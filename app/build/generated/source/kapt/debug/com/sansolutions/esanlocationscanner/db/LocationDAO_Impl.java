package com.sansolutions.esanlocationscanner.db;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class LocationDAO_Impl implements LocationDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<LocationEntity> __insertionAdapterOfLocationEntity;

  private final EntityDeletionOrUpdateAdapter<LocationEntity> __deletionAdapterOfLocationEntity;

  private final EntityDeletionOrUpdateAdapter<LocationEntity> __updateAdapterOfLocationEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateBLE;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllFromTable;

  private final SharedSQLiteStatement __preparedStmtOfUpdateBLEArrivedStatus;

  private final SharedSQLiteStatement __preparedStmtOfUpdateBLEShownStatus;

  public LocationDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfLocationEntity = new EntityInsertionAdapter<LocationEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `ESANLOCATION` (`id`,`latitude`,`longitude`,`locationname`,`locationmeters`,`arrivedmeters`,`isArrived`,`isShown`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, LocationEntity value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindLong(1, value.getId());
        }
        if (value.getLatitude() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getLatitude());
        }
        if (value.getLongitude() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getLongitude());
        }
        if (value.getLocationname() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getLocationname());
        }
        if (value.getLocationmeters() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getLocationmeters());
        }
        if (value.getArrivedmeters() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getArrivedmeters());
        }
        final int _tmp;
        _tmp = value.isArrived() ? 1 : 0;
        stmt.bindLong(7, _tmp);
        final int _tmp_1;
        _tmp_1 = value.isShown() ? 1 : 0;
        stmt.bindLong(8, _tmp_1);
      }
    };
    this.__deletionAdapterOfLocationEntity = new EntityDeletionOrUpdateAdapter<LocationEntity>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `ESANLOCATION` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, LocationEntity value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindLong(1, value.getId());
        }
      }
    };
    this.__updateAdapterOfLocationEntity = new EntityDeletionOrUpdateAdapter<LocationEntity>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `ESANLOCATION` SET `id` = ?,`latitude` = ?,`longitude` = ?,`locationname` = ?,`locationmeters` = ?,`arrivedmeters` = ?,`isArrived` = ?,`isShown` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, LocationEntity value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindLong(1, value.getId());
        }
        if (value.getLatitude() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getLatitude());
        }
        if (value.getLongitude() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getLongitude());
        }
        if (value.getLocationname() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getLocationname());
        }
        if (value.getLocationmeters() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getLocationmeters());
        }
        if (value.getArrivedmeters() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getArrivedmeters());
        }
        final int _tmp;
        _tmp = value.isArrived() ? 1 : 0;
        stmt.bindLong(7, _tmp);
        final int _tmp_1;
        _tmp_1 = value.isShown() ? 1 : 0;
        stmt.bindLong(8, _tmp_1);
        if (value.getId() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindLong(9, value.getId());
        }
      }
    };
    this.__preparedStmtOfUpdateBLE = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE ESANLOCATION SET latitude=?,longitude=?,locationmeters=?,arrivedmeters=? WHERE locationname=?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllFromTable = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM ESANLOCATION";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateBLEArrivedStatus = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE ESANLOCATION SET isArrived= ? WHERE locationname=?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateBLEShownStatus = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE ESANLOCATION SET isShown= ? WHERE locationname=?";
        return _query;
      }
    };
  }

  @Override
  public void insertBLE(final LocationEntity bleEntity) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfLocationEntity.insert(bleEntity);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deletaBLE(final LocationEntity bleEntity) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfLocationEntity.handle(bleEntity);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateBLEStatusFalse(final List<LocationEntity> listaBle) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfLocationEntity.handleMultiple(listaBle);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updataBLEValues(final LocationEntity bleEntity) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfLocationEntity.handle(bleEntity);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateBLE(final String locationname, final String latitude, final String longitude,
      final String locationmeters, final String arrivedmeters) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateBLE.acquire();
    int _argIndex = 1;
    if (latitude == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, latitude);
    }
    _argIndex = 2;
    if (longitude == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, longitude);
    }
    _argIndex = 3;
    if (locationmeters == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, locationmeters);
    }
    _argIndex = 4;
    if (arrivedmeters == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, arrivedmeters);
    }
    _argIndex = 5;
    if (locationname == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, locationname);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateBLE.release(_stmt);
    }
  }

  @Override
  public void deleteAllFromTable() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllFromTable.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAllFromTable.release(_stmt);
    }
  }

  @Override
  public void updateBLEArrivedStatus(final String locationname, final boolean isArrived) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateBLEArrivedStatus.acquire();
    int _argIndex = 1;
    final int _tmp;
    _tmp = isArrived ? 1 : 0;
    _stmt.bindLong(_argIndex, _tmp);
    _argIndex = 2;
    if (locationname == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, locationname);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateBLEArrivedStatus.release(_stmt);
    }
  }

  @Override
  public void updateBLEShownStatus(final String locationname, final boolean isShown) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateBLEShownStatus.acquire();
    int _argIndex = 1;
    final int _tmp;
    _tmp = isShown ? 1 : 0;
    _stmt.bindLong(_argIndex, _tmp);
    _argIndex = 2;
    if (locationname == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, locationname);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateBLEShownStatus.release(_stmt);
    }
  }

  @Override
  public List<LocationEntity> getAllBLEsFromDb() {
    final String _sql = "Select * from ESANLOCATION";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfLocationname = CursorUtil.getColumnIndexOrThrow(_cursor, "locationname");
      final int _cursorIndexOfLocationmeters = CursorUtil.getColumnIndexOrThrow(_cursor, "locationmeters");
      final int _cursorIndexOfArrivedmeters = CursorUtil.getColumnIndexOrThrow(_cursor, "arrivedmeters");
      final int _cursorIndexOfIsArrived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArrived");
      final int _cursorIndexOfIsShown = CursorUtil.getColumnIndexOrThrow(_cursor, "isShown");
      final List<LocationEntity> _result = new ArrayList<LocationEntity>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final LocationEntity _item;
        final Integer _tmpId;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmpId = null;
        } else {
          _tmpId = _cursor.getInt(_cursorIndexOfId);
        }
        final String _tmpLatitude;
        _tmpLatitude = _cursor.getString(_cursorIndexOfLatitude);
        final String _tmpLongitude;
        _tmpLongitude = _cursor.getString(_cursorIndexOfLongitude);
        final String _tmpLocationname;
        _tmpLocationname = _cursor.getString(_cursorIndexOfLocationname);
        final String _tmpLocationmeters;
        _tmpLocationmeters = _cursor.getString(_cursorIndexOfLocationmeters);
        final String _tmpArrivedmeters;
        _tmpArrivedmeters = _cursor.getString(_cursorIndexOfArrivedmeters);
        final boolean _tmpIsArrived;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsArrived);
        _tmpIsArrived = _tmp != 0;
        final boolean _tmpIsShown;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsShown);
        _tmpIsShown = _tmp_1 != 0;
        _item = new LocationEntity(_tmpId,_tmpLatitude,_tmpLongitude,_tmpLocationname,_tmpLocationmeters,_tmpArrivedmeters,_tmpIsArrived,_tmpIsShown);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LocationEntity getDeviceDetails(final String locationname) {
    final String _sql = "Select * from ESANLOCATION WHERE locationname = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (locationname == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, locationname);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfLocationname = CursorUtil.getColumnIndexOrThrow(_cursor, "locationname");
      final int _cursorIndexOfLocationmeters = CursorUtil.getColumnIndexOrThrow(_cursor, "locationmeters");
      final int _cursorIndexOfArrivedmeters = CursorUtil.getColumnIndexOrThrow(_cursor, "arrivedmeters");
      final int _cursorIndexOfIsArrived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArrived");
      final int _cursorIndexOfIsShown = CursorUtil.getColumnIndexOrThrow(_cursor, "isShown");
      final LocationEntity _result;
      if(_cursor.moveToFirst()) {
        final Integer _tmpId;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmpId = null;
        } else {
          _tmpId = _cursor.getInt(_cursorIndexOfId);
        }
        final String _tmpLatitude;
        _tmpLatitude = _cursor.getString(_cursorIndexOfLatitude);
        final String _tmpLongitude;
        _tmpLongitude = _cursor.getString(_cursorIndexOfLongitude);
        final String _tmpLocationname;
        _tmpLocationname = _cursor.getString(_cursorIndexOfLocationname);
        final String _tmpLocationmeters;
        _tmpLocationmeters = _cursor.getString(_cursorIndexOfLocationmeters);
        final String _tmpArrivedmeters;
        _tmpArrivedmeters = _cursor.getString(_cursorIndexOfArrivedmeters);
        final boolean _tmpIsArrived;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsArrived);
        _tmpIsArrived = _tmp != 0;
        final boolean _tmpIsShown;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsShown);
        _tmpIsShown = _tmp_1 != 0;
        _result = new LocationEntity(_tmpId,_tmpLatitude,_tmpLongitude,_tmpLocationname,_tmpLocationmeters,_tmpArrivedmeters,_tmpIsArrived,_tmpIsShown);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
