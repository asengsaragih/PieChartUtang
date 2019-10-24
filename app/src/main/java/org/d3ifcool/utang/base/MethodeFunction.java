package org.d3ifcool.utang.base;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.d3ifcool.utang.R;
import org.d3ifcool.utang.database.Contract;
import org.d3ifcool.utang.database.Helper;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.core.app.ActivityCompat;

public class MethodeFunction {
    public String subStringTanggal(String rawTanggal) {
        String year = rawTanggal.substring(0,4);
        String month = rawTanggal.substring(4,6);
        String day = rawTanggal.substring(6);

        String subString = year + "/" + month + "/" + day;

        return subString;
    }

    public String currencyIndonesian(int total) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(localeID);

        return format.format(total);
    }

    public String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = simpleDateFormat.format(c.getTime());

        return formattedDate;
    }

    public TextWatcher jumlahChangeListener(final EditText jumlahEditText) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                jumlahEditText.removeTextChangedListener(this);

                try {
                    String originalJumlahString = editable.toString();

                    Long longval;
                    if (originalJumlahString.contains(",")) {
                        originalJumlahString = originalJumlahString.replace("," ,"");
                    }
                    longval = Long.parseLong(originalJumlahString);

                    DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    decimalFormat.applyPattern("#,###,###,###");
                    String formatedString = decimalFormat.format(longval);

                    jumlahEditText.setText(formatedString);
                    jumlahEditText.setSelection(jumlahEditText.getText().length());

                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                jumlahEditText.addTextChangedListener(this);
            }
        };
    }

    public void toastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void readContactPermission(Context context) {
        ActivityCompat.requestPermissions((Activity) context, new String[] {Manifest.permission.READ_CONTACTS}, 1);
    }

    public void callPermission(Context context) {
        ActivityCompat.requestPermissions((Activity) context, new String[] {Manifest.permission.CALL_PHONE}, 1);
    }

    public void readPhoneNumberPermission(Context context) {
        ActivityCompat.requestPermissions((Activity) context, new String[] {Manifest.permission.READ_PHONE_NUMBERS}, 1);
    }

    public void readStoragePermission(Context context) {
        ActivityCompat.requestPermissions((Activity) context, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
    }

    public void writeStoragePermission(Context context) {
        ActivityCompat.requestPermissions((Activity) context, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    public String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int columnIndex = cursor.getColumnIndexOrThrow("_data");

                if (cursor.moveToFirst()) {
                    return cursor.getString(columnIndex);
                }
            } catch (Exception e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public void copyFile(FileInputStream fromFile, FileInputStream toFile) throws IOException {
        FileChannel fromChannel = null;
        FileChannel toChannel = null;

        try {
            fromChannel = fromFile.getChannel();
            toChannel = toFile.getChannel();
            fromChannel.transferTo(0, fromChannel.size(), toChannel);
        } finally {
            try {
                if (fromChannel != null) {
                    fromChannel.close();
                }
            } finally {
                if (toChannel != null) {
                    toChannel.close();
                }
            }
        }
    }


    public String checkFilePath(String filename) {
        String result;
        if (filename.contains(".")) {
            int startIndex = filename.indexOf(0);
            int endIndex = filename.indexOf(".");



            String replacement = "";
            String toBeReplace = filename.substring(startIndex + 1, endIndex);

            String newFilename = filename.replace(toBeReplace, replacement);

            if (newFilename.contains(".csv")) {
//                toastMessage(context, newFilename);
                return result = "true";
            } else {
//                toastMessage(context, "File Salah");
                return result = "false";
            }
        } else {
//            toastMessage(context, "File Salah");
            return result = "false";
        }
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }

    public void deleteAll(Context context) {
        SQLiteDatabase database = new Helper(context).getWritableDatabase();
        database.execSQL("DELETE FROM " + Contract.UtangEntry.TABLE_NAME);
        database.close();
    }
    
    public int changeMonthString(String monthString) {
        int month = 0;

        if (monthString == "January") {
            month = 1;
        } else if (monthString == "February") {
            month = 2;
        } else if (monthString == "March") {
            month = 3;
        } else if (monthString == "April") {
            month = 4;
        } else if (monthString == "May") {
            month = 5;
        } else if (monthString == "June") {
            month = 6;
        } else if (monthString == "July") {
            month = 7;
        } else if (monthString == "August") {
            month = 8;
        } else if (monthString == "September") {
            month = 9;
        } else if (monthString == "October") {
            month = 10;
        } else if (monthString == "November") {
            month = 11;
        } else if (monthString == "December") {
            month = 12;
        }

        return month;
    }
}
