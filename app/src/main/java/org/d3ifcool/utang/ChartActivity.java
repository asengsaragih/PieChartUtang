package org.d3ifcool.utang;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.kal.rackmonthpicker.RackMonthPicker;
import com.kal.rackmonthpicker.listener.DateMonthDialogListener;
import com.kal.rackmonthpicker.listener.OnCancelMonthDialogListener;

import org.d3ifcool.utang.base.MethodeFunction;
import org.d3ifcool.utang.database.Contract;
import org.d3ifcool.utang.database.Helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ChartActivity extends AppCompatActivity {

//    private static final int UTANG_CHART_LOADER = 0;
//    private static final int RESTART_UTANG_CHART_LOADER = 1;

//    private SQLiteDatabase database = new Helper(this).getReadableDatabase();

    MethodeFunction methodeFunction = new MethodeFunction();

    private static final int CHART_LOADER = 0;


    private PieChart mPaidOffChart;
    private PieChart mNotPaidOffChart;
    private TextView mPickerMonthTextView;
    private TextView mTotalDipinjamPaidOffTextView;
    private TextView mTotalMeminjamPaidOffTextView;
    private TextView mTotalDipinjamNotPaidOffTextView;
    private TextView mTotalMeminjamNotPaidOffTextView;
    private SQLiteDatabase database;
    private Cursor cursor;

    private String CURRENT_MONTH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        mPaidOffChart = findViewById(R.id.pie_chart_paid_off);
        mNotPaidOffChart = findViewById(R.id.pie_chart_not_paid_off);
        mPickerMonthTextView = findViewById(R.id.textView_date_picker_chart);
        mTotalDipinjamPaidOffTextView = findViewById(R.id.textView_paid_off_dipinjam);
        mTotalMeminjamPaidOffTextView = findViewById(R.id.textView_paid_off_meminjam);
        mTotalDipinjamNotPaidOffTextView = findViewById(R.id.textView_not_paid_off_dipinjam);
        mTotalMeminjamNotPaidOffTextView = findViewById(R.id.textView_not_paid_off_meminjam);

        mShowCurrentMonth();
        mSetPickerButton();

        mSetTextContent();

//        getLoaderManager().initLoader(CHART_LOADER, null, MainActivity.this);
    }

    private String getCurrentMonth() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMMM");
        String formattedDate = simpleDateFormat.format(c.getTime());

        return formattedDate;
    }

    private void mShowCurrentMonth() {
        String currentMonth = getCurrentMonth();
        int yearIndex = Integer.parseInt(currentMonth.substring(0, 4));
        String monthIndex = currentMonth.substring(4);
//        String yearString;
        String monthString = null;
        int monthInt;
//        int number = Integer.parseInt();
//
//        if (monthIndex == "January") {
//            monthString = getString(R.string.month_jan);
//        } else if (monthIndex == "February") {
//            monthString = getString(R.string.month_feb);
//        } else if (monthIndex == "March") {
//            monthString = getString(R.string.month_mar);
//        } else if (monthIndex == "April") {
//            monthString = getString(R.string.month_apr);
//        } else if (monthIndex == "May") {
//            monthString = getString(R.string.month_may);
//        } else if (monthIndex == "June") {
//            monthString = getString(R.string.month_jun);
//        } else if (monthIndex == "July") {
//            monthString = getString(R.string.month_jul);
//        } else if (monthIndex == "August") {
//            monthString = getString(R.string.month_aug);
//        } else if (monthIndex == "September") {
//            monthString = getString(R.string.month_sep);
//        } else if (monthIndex == "October") {
//            monthString = getString(R.string.month_oct);
//        } else if (monthIndex == "November") {
//            monthString = getString(R.string.month_nov);
//        } else if (monthIndex == "December") {
//            monthString = getString(R.string.month_dec);
//        }

        CURRENT_MONTH = monthIndex + " " + yearIndex;

        mPickerMonthTextView.setText(CURRENT_MONTH);
    }

    private void mShowMonthPicker() {
        new RackMonthPicker(this)
                .setLocale(new Locale("in", "ID"))
                .setPositiveButton(new DateMonthDialogListener() {
                    @Override
                    public void onDateMonth(int month, int startDate, int endDate, int year, String monthLabel) {
                        String monthString = null;
                        if (month == 1) {
                            monthString = getString(R.string.month_jan);
                        } else if (month == 2) {
                            monthString = getString(R.string.month_feb);
                        } else if (month == 3) {
                            monthString = getString(R.string.month_mar);
                        } else if (month == 4) {
                            monthString = getString(R.string.month_apr);
                        } else if (month == 5) {
                            monthString = getString(R.string.month_may);
                        } else if (month == 6) {
                            monthString = getString(R.string.month_jun);
                        } else if (month == 7) {
                            monthString = getString(R.string.month_jul);
                        } else if (month == 8) {
                            monthString = getString(R.string.month_aug);
                        } else if (month == 9) {
                            monthString = getString(R.string.month_sep);
                        } else if (month == 10) {
                            monthString = getString(R.string.month_oct);
                        } else if (month == 11) {
                            monthString = getString(R.string.month_nov);
                        } else if (month == 12) {
                            monthString = getString(R.string.month_dec);
                        }
                        CURRENT_MONTH = monthString + " " + year;
                        mPickerMonthTextView.setText(CURRENT_MONTH);

                        mTotalDipinjamNotPaidOffTextView.setText(getString(R.string.total_dipinjam) + " " +  methodeFunction.currencyIndonesian(mNotPaidOffDipinjam(month, year)));
                        mTotalMeminjamNotPaidOffTextView.setText(getString(R.string.total_meminjam) + " " +  methodeFunction.currencyIndonesian(mNotPaidOffMeminjam(month, year)));
                        mTotalDipinjamPaidOffTextView.setText(getString(R.string.total_dipinjam) + " " +  methodeFunction.currencyIndonesian(mPaidOffDipinjam(month, year)));
                        mTotalMeminjamPaidOffTextView.setText(getString(R.string.total_meminjam) + " " +  methodeFunction.currencyIndonesian(mPaidOffMeminjam(month, year)));
                    }
                })
                .setNegativeButton(new OnCancelMonthDialogListener() {
                    @Override
                    public void onCancel(AlertDialog alertDialog) {
                        alertDialog.dismiss();
                    }
                })
                .setColorTheme(R.color.colorPrimaryDarkForPicker)
                .show();
    }

    private void mSetPickerButton() {
        mPickerMonthTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowMonthPicker();
            }
        });
    }

    private void check() {
        int fillter;
        String yearString = CURRENT_MONTH.substring(CURRENT_MONTH.length() - 4);
        String monthString = CURRENT_MONTH.replace(" " + yearString, "");

//        methodeFunction.toastMessage(this, yearString + " " + monthString);
    }

    private void mSetTextContent() {
        String yearString = CURRENT_MONTH.substring(CURRENT_MONTH.length() - 4);
        String monthString = CURRENT_MONTH.replace(" " + yearString, "");
//
        int month = methodeFunction.changeMonthString(monthString);
        int year = Integer.parseInt(yearString);

        mTotalDipinjamNotPaidOffTextView.setText(getString(R.string.total_dipinjam) + " " +  methodeFunction.currencyIndonesian(mNotPaidOffDipinjam(month, year)));
        mTotalMeminjamNotPaidOffTextView.setText(getString(R.string.total_meminjam) + " " +  methodeFunction.currencyIndonesian(mNotPaidOffMeminjam(month, year)));
        mTotalDipinjamPaidOffTextView.setText(getString(R.string.total_dipinjam) + " " +  methodeFunction.currencyIndonesian(mPaidOffDipinjam(month, year)));
        mTotalMeminjamPaidOffTextView.setText(getString(R.string.total_meminjam) + " " +  methodeFunction.currencyIndonesian(mPaidOffMeminjam(month, year)));
    }

    private int mNotPaidOffDipinjam(int month, int year) {
//        String yearString = CURRENT_MONTH.substring(CURRENT_MONTH.length() - 4);
//        String monthString = CURRENT_MONTH.replace(" " + yearString, "");
//
//        int month = methodeFunction.changeMonthString(monthString);
//        int year = Integer.parseInt(yearString);
//
//        String filter = String.valueOf(month) + String.valueOf(year) + "01";
        String monthString;
        if (month <= 9) {
            monthString = "0" + String.valueOf(month);
        } else {
            monthString = String.valueOf(month);
        }
        String filter = String.valueOf(year) + monthString + "01";

        database = new Helper(this).getReadableDatabase();
        cursor = database.rawQuery(
                "SELECT SUM(" +
                        Contract.UtangEntry.COLUMN_UTANG_JUMLAH +
                        ") FROM " + Contract.UtangEntry.TABLE_NAME +
                        " WHERE " + Contract.UtangEntry.COLUMN_UTANG_STATUS +
                        " = 1" + " AND " +
                        Contract.UtangEntry.COLUMN_UTANG_KATEGORI + " = " + Contract.UtangEntry.UTANG_KATEGORI_DIPINJAM +
                        " AND " + Contract.UtangEntry.COLUMN_UTANG_TANGGAL_CREATE + " >= " + filter +
                        " OR " + Contract.UtangEntry.COLUMN_UTANG_TANGGAL_CREATE + " <= " + filter, null);
        int total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getInt(0);
        }
        while (cursor.moveToNext());
        cursor.close();
        return total;
    }

    private int mNotPaidOffMeminjam(int month, int year) {
        String monthString;
        if (month <= 9) {
            monthString = "0" + String.valueOf(month);
        } else {
            monthString = String.valueOf(month);
        }
        String filter = String.valueOf(year) + monthString + "01";

        database = new Helper(this).getReadableDatabase();
        cursor = database.rawQuery(
                "SELECT SUM(" +
                        Contract.UtangEntry.COLUMN_UTANG_JUMLAH +
                        ") FROM " + Contract.UtangEntry.TABLE_NAME +
                        " WHERE " + Contract.UtangEntry.COLUMN_UTANG_STATUS +
                        " = 1" + " AND " +
                        Contract.UtangEntry.COLUMN_UTANG_KATEGORI + " = " + Contract.UtangEntry.UTANG_KATEGORI_MEMINJAM +
                        " AND " + Contract.UtangEntry.COLUMN_UTANG_TANGGAL_CREATE + " >= " + filter +
                        " OR " + Contract.UtangEntry.COLUMN_UTANG_TANGGAL_CREATE + " <= " + filter, null);
        int total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getInt(0);
        }
        while (cursor.moveToNext());
        return total;
    }

    private int mPaidOffDipinjam(int month, int year) {
        String monthString;
        if (month <= 9) {
            monthString = "0" + String.valueOf(month);
        } else {
            monthString = String.valueOf(month);
        }
        String filter = String.valueOf(year) + monthString + "01";

        database = new Helper(this).getReadableDatabase();
        cursor = database.rawQuery(
                "SELECT SUM(" +
                        Contract.UtangEntry.COLUMN_UTANG_JUMLAH +
                        ") FROM " + Contract.UtangEntry.TABLE_NAME +
                        " WHERE " + Contract.UtangEntry.COLUMN_UTANG_STATUS +
                        " = 2" + " AND " +
                        Contract.UtangEntry.COLUMN_UTANG_KATEGORI + " = " + Contract.UtangEntry.UTANG_KATEGORI_DIPINJAM +
                        " AND " + Contract.UtangEntry.COLUMN_UTANG_TANGGAL_CREATE + " >= " + filter +
                        " OR " + Contract.UtangEntry.COLUMN_UTANG_TANGGAL_CREATE + " <= " + filter, null);
        int total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getInt(0);
        }
        while (cursor.moveToNext());
        return total;
    }

    private int mPaidOffMeminjam(int month, int year) {
        String monthString;
        if (month <= 9) {
            monthString = "0" + String.valueOf(month);
        } else {
            monthString = String.valueOf(month);
        }
        String filter = String.valueOf(year) + monthString + "01";

        database = new Helper(this).getReadableDatabase();
        cursor = database.rawQuery(
                "SELECT SUM(" +
                        Contract.UtangEntry.COLUMN_UTANG_JUMLAH +
                        ") FROM " + Contract.UtangEntry.TABLE_NAME +
                        " WHERE " + Contract.UtangEntry.COLUMN_UTANG_STATUS +
                        " = 2" + " AND " +
                        Contract.UtangEntry.COLUMN_UTANG_KATEGORI + " = " + Contract.UtangEntry.UTANG_KATEGORI_MEMINJAM +
                        " AND " + Contract.UtangEntry.COLUMN_UTANG_TANGGAL_CREATE + " >= " + filter +
                        " OR " + Contract.UtangEntry.COLUMN_UTANG_TANGGAL_CREATE + " <= " + filter, null);
        int total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getInt(0);
        }
        while (cursor.moveToNext());
        return total;
    }

    private void refresh() {
        database = new Helper(this).getReadableDatabase();
//        cursor = database.
    }
}
