package com.ichsanfirdaus.crudsiswa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ichsanfirdaus.crudsiswa.domain.Siswa;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ichsan on 31/10/15.
 */
public class DBAdapter extends SQLiteOpenHelper {

    private static final String DB_NAME = "biodata";
    private static final String TABLE_NAME = "m_siswa";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "nama";
    private static final String COL_KELAS = "kelas";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS" + TABLE_NAME + ";";
    private SQLiteDatabase sqLiteDatabase = null;

    public DBAdapter(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
    }

    public void openDB() {
        if (sqLiteDatabase== null) {
            sqLiteDatabase = getWritableDatabase();
        }
    }

    public void closeDB() {
        if (sqLiteDatabase != null) {
            if (sqLiteDatabase.isOpen()) {
                sqLiteDatabase.close();
            }
        }
    }

    public void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+"("+COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+COL_NAME+" TEXT,"+COL_KELAS+" TEXT);");
    }

    public void updateSiswa(Siswa siswa) {
        sqLiteDatabase = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, siswa.getNama());
        cv.put(COL_KELAS, siswa.getKelas());
        String whereClause = COL_ID + "==?";
        String whereArgs[] = new String[] { siswa.getId() };
        sqLiteDatabase.update(TABLE_NAME, cv, whereClause, whereArgs);
        sqLiteDatabase.close();
    }

    public void save(Siswa siswa) {
        sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME, siswa.getNama());
        contentValues.put(COL_KELAS, siswa.getKelas());
        sqLiteDatabase.insertWithOnConflict(TABLE_NAME, null, contentValues, sqLiteDatabase.CONFLICT_IGNORE);
        sqLiteDatabase.close();
    }

    public void delete(Siswa siswa) {
        sqLiteDatabase = getWritableDatabase();
        String whereClause = COL_ID + "==?";
        String[] whereArgs = new String[] { String.valueOf(siswa.getId()) };
        sqLiteDatabase.delete(TABLE_NAME, whereClause, whereArgs);
        sqLiteDatabase.close();
    }

    public void deleteAll() {
        sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME, null, null);
        sqLiteDatabase.close();
    }

    public List<Siswa> getAllSiswa() {
        sqLiteDatabase = getWritableDatabase();
        Cursor cursor = this.sqLiteDatabase.query(TABLE_NAME, new String[] { COL_ID, COL_NAME, COL_KELAS }, null, null, null, null, null);
        List<Siswa> siswas = new ArrayList<Siswa>();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Siswa siswa = new Siswa();
                siswa.setId(cursor.getString(cursor.getColumnIndex(COL_ID)));
                siswa.setId(cursor.getString(cursor.getColumnIndex(COL_NAME)));
                siswa.setId(cursor.getString(cursor.getColumnIndex(COL_KELAS)));
                siswas.add(siswa);
            }
            sqLiteDatabase.close();
            return siswas;
        } else {
            sqLiteDatabase.close();
            return new ArrayList<Siswa>();
        }
    }

}
