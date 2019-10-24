package org.d3ifcool.utang.database;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class Helper extends SQLiteOpenHelper {
    public static final String LOG_TAG = Helper.class.getSimpleName();
    private static final String DATABASE_NAME = "Utang.db";
    private static final int DATABASE_VERSION = 1;
    
    public Helper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_UTANG_TABLE = "CREATE TABLE " + Contract.UtangEntry.TABLE_NAME + " ("
                + Contract.UtangEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Contract.UtangEntry.COLUMN_UTANG_TANGGAL_CREATE + " INTEGER NOT NULL, "
                + Contract.UtangEntry.COLUMN_UTANG_TANGGAL_DEADLINE + " INTEGER, "
                + Contract.UtangEntry.COLUMN_UTANG_TANGGAL_PEMBAYARAN + " INTEGER, "
                + Contract.UtangEntry.COLUMN_UTANG_NAMA + " TEXT NOT NULL, "
                + Contract.UtangEntry.COLUMN_UTANG_PHONE + " TEXT, "
                + Contract.UtangEntry.COLUMN_UTANG_JUMLAH + " INTEGER NOT NULL DEFAULT 0, "
                + Contract.UtangEntry.COLUMN_UTANG_KETERANGAN + " TEXT, "
                + Contract.UtangEntry.COLUMN_UTANG_KATEGORI + " INTEGER NOT NULL, "
                + Contract.UtangEntry.COLUMN_UTANG_STATUS + " INTEGER NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_UTANG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
//
//    class DatabaseContext extends ContextWrapper {
//        private static final String DEBUG_CONTEXT = "DatabaseContext";
//
//        public DatabaseContext(Context base) {
//            super(base);
//        }
//
//        @Override
//        public File getDatabasePath(String name) {
//            File sdcard = Environment.getExternalStorageDirectory();
//            String dbFile = sdcard.getAbsolutePath() + File.separator + "databases" + File.separator + name;
//
//            if (!dbFile.endsWith(".db")) {
//                dbFile += ".db";
//            }
//
//            File result = new File(dbFile);
//
//            if (!result.getParentFile().exists()) {
//                result.getParentFile().mkdirs();
//            }
//
//            if (Log.isLoggable(DEBUG_CONTEXT, Log.WARN)) {
//                Log.w(DEBUG_CONTEXT, "getDatabasePath(" + name + ") = " + result.getAbsolutePath());
//            }
//
//            return result;
//        }
//
//        @Override
//        public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
//            return openOrCreateDatabase(name, mode, factory);
//        }
//    }
}
