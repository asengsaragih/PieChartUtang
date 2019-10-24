package org.d3ifcool.utang.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class Contract {

    private Contract() {
    }

    public static final String CONTENT_AUTHORITY = "org.d3ifcool.utang";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_UTANG = "utang";

    public static final class UtangEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_UTANG);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_UTANG;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_UTANG;

        public final static String TABLE_NAME = "utang";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_UTANG_TANGGAL_CREATE = "utang_tanggal_create";
        public final static String COLUMN_UTANG_TANGGAL_DEADLINE = "utang_tanggal_deadline";
        public final static String COLUMN_UTANG_TANGGAL_PEMBAYARAN = "utang_tanggal_pembayaran";
        public final static String COLUMN_UTANG_NAMA = "utang_nama";
        public final static String COLUMN_UTANG_PHONE = "utang_phone";
        public final static String COLUMN_UTANG_JUMLAH = "utang_jumlah";
        public final static String COLUMN_UTANG_KETERANGAN = "utang_keterangan";
        public final static String COLUMN_UTANG_KATEGORI = "utang_kategori";
        public final static String COLUMN_UTANG_STATUS = "utang_status";

        public static final int UTANG_KATEGORI_UNKNOW = 0;
        public static final int UTANG_KATEGORI_DIPINJAM = 1;
        public static final int UTANG_KATEGORI_MEMINJAM = 2;

        public static final int UTANG_STATUS_UNKNOW = 0;
        public static final int UTANG_STATUS_BELUMLUNAS = 1;
        public static final int UTANG_STATUS_LUNAS = 2;

        public static boolean isValidKategoriUtang(int kategoriUtang){
            if (kategoriUtang == UTANG_KATEGORI_UNKNOW || kategoriUtang == UTANG_KATEGORI_DIPINJAM || kategoriUtang == UTANG_KATEGORI_MEMINJAM){
                return true;
            }
            return false;
        }

        public static boolean isValidStatusUtang(int statusUtang){
            if (statusUtang == UTANG_STATUS_UNKNOW || statusUtang == UTANG_STATUS_BELUMLUNAS || statusUtang == UTANG_STATUS_LUNAS){
                return true;
            }
            return false;
        }
    }
}
