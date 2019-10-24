package org.d3ifcool.utang.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Provider extends ContentProvider {
    public static final String LOG_TAG = Provider.class.getSimpleName();

    private static final int UTANG = 100;
    private static final int UTANG_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_UTANG, UTANG);

        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_UTANG + "/#", UTANG_ID);
    }

    private Helper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new Helper(getContext());
        // sebelumnya return nya false, tapi di ganti jadi true
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection,  String selection,  String[] selectionArgs,  String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case UTANG:
                cursor = database.query(Contract.UtangEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case UTANG_ID:
                selection = Contract.UtangEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = database.query(Contract.UtangEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case UTANG:
                return Contract.UtangEntry.CONTENT_LIST_TYPE;
            case  UTANG_ID:
                return Contract.UtangEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case UTANG:
                return insertUtang(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertUtang(Uri uri, ContentValues values){
        Integer tanggal_create = values.getAsInteger(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_CREATE);
        if (tanggal_create == null){
            throw new IllegalArgumentException("Masukkan Tanggal Yang Valid");
        }

        String nama = values.getAsString(Contract.UtangEntry.COLUMN_UTANG_NAMA);
        if (nama == null){
            throw new IllegalArgumentException("Masukkan Nama Yang Valid");
        }

        Integer jumlah = values.getAsInteger(Contract.UtangEntry.COLUMN_UTANG_JUMLAH);
        if (jumlah != null && jumlah < 0){
            throw new IllegalArgumentException("Masukkan Jumlah Yang Valid");
        }

        Integer status = values.getAsInteger(Contract.UtangEntry.COLUMN_UTANG_STATUS);
        if (status == null ||!Contract.UtangEntry.isValidStatusUtang(status)){
            throw new IllegalArgumentException("Masukkan Status Yang Valid");
        }

        Integer kategori = values.getAsInteger(Contract.UtangEntry.COLUMN_UTANG_KATEGORI);
        if (kategori == null ||!Contract.UtangEntry.isValidKategoriUtang(kategori)){
            throw new IllegalArgumentException("Masukkan Kategori Yang Valid");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(Contract.UtangEntry.TABLE_NAME, null, values);

        if (id == -1){
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri,  String selection,  String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsDeleted;
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case UTANG:
                rowsDeleted = database.delete(Contract.UtangEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case UTANG_ID:
                selection = Contract.UtangEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(Contract.UtangEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted !=0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri,  ContentValues values,  String selection,  String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case UTANG:
                return updateUtang(uri, values, selection, selectionArgs);
            case UTANG_ID:
                selection = Contract.UtangEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateUtang(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateUtang(Uri uri, ContentValues values, String selection, String[] selectionArgs){
        if (values.containsKey(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_CREATE)){
            Integer tanggal_create = values.getAsInteger(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_CREATE);
            if (tanggal_create == null){
                throw new IllegalArgumentException("Masukkan Tanggal create Yang Valid");
            }
        }

        if (values.containsKey(Contract.UtangEntry.COLUMN_UTANG_NAMA)){
            String nama = values.getAsString(Contract.UtangEntry.COLUMN_UTANG_NAMA);
            if (nama == null){
                throw new IllegalArgumentException("Masukkan Nama Yang Valid");
            }
        }

        if (values.containsKey(Contract.UtangEntry.COLUMN_UTANG_JUMLAH)){
            Integer jumlah = values.getAsInteger(Contract.UtangEntry.COLUMN_UTANG_JUMLAH);
            if (jumlah != null && jumlah < 0){
                throw new IllegalArgumentException("Masukkan Jumlah Yang Valid");
            }
        }

        if (values.containsKey(Contract.UtangEntry.COLUMN_UTANG_STATUS)){
            Integer status = values.getAsInteger(Contract.UtangEntry.COLUMN_UTANG_STATUS);
            if (status == null || !Contract.UtangEntry.isValidStatusUtang(status)){
                throw new IllegalArgumentException("Masukkan Status Yang Valid");
            }
        }

        if (values.containsKey(Contract.UtangEntry.COLUMN_UTANG_KATEGORI)){
            Integer kategori = values.getAsInteger(Contract.UtangEntry.COLUMN_UTANG_KATEGORI);
            if (kategori == null || !Contract.UtangEntry.isValidStatusUtang(kategori)){
                throw new IllegalArgumentException("Masukkan kategori Yang Valid");
            }
        }

        if (values.size() == 0){
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsUpdated = database.update(Contract.UtangEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }
}
