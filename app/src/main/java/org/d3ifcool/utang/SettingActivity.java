package org.d3ifcool.utang;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import de.siegmar.fastcsv.reader.CsvContainer;
import de.siegmar.fastcsv.reader.CsvParser;
import de.siegmar.fastcsv.reader.CsvReader;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.d3ifcool.utang.base.CsvWritter;
import org.d3ifcool.utang.base.MethodeFunction;
import org.d3ifcool.utang.database.Contract;
import org.d3ifcool.utang.database.Helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class SettingActivity extends AppCompatActivity {

    public static String DB_FILEPATH = "/data/data/org.d3ifcool.utang/databases/Utang.db";

    MethodeFunction methodeFunction = new MethodeFunction();

    private TextView mCloseWarningTextView;
    private TextView mExportPhoneTextView;
    private TextView mExportCsvTextView;
    private TextView mImportTextView;
    private TextView mAboutAplicationTextView;
    private TextView mCriticismSuggestionTextView;

    private ConstraintLayout mWarningTextConstrainLayout;

    private static final int FILE_SELECT_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mCloseWarningTextView = findViewById(R.id.textView_detele_button_warning_text);
        mExportPhoneTextView = findViewById(R.id.textView_export_button_phone);
        mExportCsvTextView = findViewById(R.id.textView_export_button_csv);
        mImportTextView = findViewById(R.id.textView_import_button);
        mAboutAplicationTextView = findViewById(R.id.textView_about_aplication);
        mCriticismSuggestionTextView = findViewById(R.id.textView_about_criticism_suggestions);
        mWarningTextConstrainLayout = findViewById(R.id.constraitlayout_setting_warning);

        mButtonSettingClicked();
    }

    private void mButtonSettingClicked() {
        mCloseWarningTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWarningTextConstrainLayout.setVisibility(View.GONE);
            }
        });

        mExportPhoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mExportToPhone();
            }
        });

        mExportCsvTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mExportToCsv(getApplicationContext());
            }
        });

        mImportTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOpenFilePicker();
            }
        });

        mAboutAplicationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TentangActivity.class);
                startActivity(intent);
            }
        });

        mCriticismSuggestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto: " + "aldiwahyu.saragih@gmail.com"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Masukkan Dan Saran Utang Apps");
                intent.putExtra(Intent.EXTRA_TEXT, "\n\n\n\n\nDikirim Melalui " + methodeFunction.getDeviceName());

                startActivity(Intent.createChooser(intent, "Send Feedback"));
            }
        });
    }

    private void mExportToPhone() {
        if (ContextCompat.checkSelfPermission(SettingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //jika permisi belum disetujui
            if (ActivityCompat.shouldShowRequestPermissionRationale(SettingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                methodeFunction.readStoragePermission(this);
                methodeFunction.writeStoragePermission(this);
            } else {
                methodeFunction.readStoragePermission(this);
                methodeFunction.writeStoragePermission(this);
            }
        } else {
            // jika export telah disetujui
            try {
                File exportFolder = new File(Environment.getExternalStorageDirectory() + "/" + "Utang");

                if (!exportFolder.exists()) {
                    exportFolder.mkdir();
                }

                File sd = Environment.getExternalStorageDirectory();
                File data = Environment.getDataDirectory();
//
                if (sd.canWrite()) {
                    int num = 0;

                    String currentDBPath = "/data/" + "org.d3ifcool.utang"
                            + "/databases/" + "Utang.db";

                    String backupDBPath = "/Utang/Utang" + ".db";

                    File currentDB = new File(data, currentDBPath);
                    File backupDB = new File(sd, backupDBPath);

                    final FileChannel src = new FileInputStream(currentDB).getChannel();
                    final FileChannel dst = new FileOutputStream(backupDB).getChannel();

                    if (backupDB.exists() == true) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);

                        builder.setTitle(R.string.setting_activity_warning_export_title);
                        builder.setMessage(R.string.setting_activity_warning_export_message);

                        builder.setPositiveButton(R.string.overwrite, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    dst.transferFrom(src, 0, src.size());
                                    src.close();
                                    dst.close();
                                    methodeFunction.toastMessage(getApplicationContext(), getString(R.string.export_phone_success));
                                } catch (IOException e) {
                                    methodeFunction.toastMessage(getApplicationContext(), e.toString());
                                }
                            }
                        });

                        builder.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();
                    }
                } else {
                    methodeFunction.toastMessage(this, getString(R.string.export_phone_failed));
                }
            } catch (Exception e) {
                methodeFunction.toastMessage(this, e.toString());
            }

        }
    }

    private void mOpenFilePicker() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.setting_activity_import_title);
        builder.setPositiveButton(R.string.choose, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mPickerFile();
                dialogInterface.cancel();
            }
        });

        builder.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }

    private void mPickerFile() {
        if (ContextCompat.checkSelfPermission(SettingActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //jika permisi belum disetujui
            if (ActivityCompat.shouldShowRequestPermissionRationale(SettingActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                methodeFunction.readStoragePermission(this);
            } else {
                methodeFunction.readStoragePermission(this);
            }
        } else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("text/csv");
//            intent.setType("*/*");

            try {
                startActivityForResult(Intent.createChooser(intent, "Select a file to upload"), FILE_SELECT_CODE);
            } catch (android.content.ActivityNotFoundException e) {
                methodeFunction.toastMessage(SettingActivity.this, getString(R.string.warning_open_file_chooser));
            }

        }
    }

    private void mExportToCsv(Context context) {
        if (ContextCompat.checkSelfPermission(SettingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //jika permisi belum disetujui
            if (ActivityCompat.shouldShowRequestPermissionRationale(SettingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                methodeFunction.readStoragePermission(this);
                methodeFunction.writeStoragePermission(this);
            } else {
                methodeFunction.readStoragePermission(this);
                methodeFunction.writeStoragePermission(this);
            }
        } else {
            Helper helper = new Helper(context);

            File exportFolder = new File(Environment.getExternalStorageDirectory() + "/" + "Utang");

            if (!exportFolder.exists()) {
                exportFolder.mkdir();
            }

            String fileName = "data.csv";
            File file = new File(exportFolder, fileName);

            try {
                file.createNewFile();
                CsvWritter csvWritter = new CsvWritter(new FileWriter(file));
                SQLiteDatabase database = helper.getReadableDatabase();
                Cursor cursor = database.rawQuery("SELECT * FROM utang", null);
                csvWritter.writeNext(cursor.getColumnNames());

                while (cursor.moveToNext()) {
                    String arrStr[] = {
                            cursor.getString(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getString(6),
                            cursor.getString(7),
                            cursor.getString(8),
                            cursor.getString(9),
                    };

                    csvWritter.writeNext(arrStr);
                }
                methodeFunction.toastMessage(context, getString(R.string.export_csv_success));
                csvWritter.close();
                cursor.close();

            } catch (Exception e) {
                methodeFunction.toastMessage(context, e.toString());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    Uri path = data.getData();
                    BufferedReader bufferedReader = null;
                    String line = "";
                    ContentValues values = new ContentValues();

                    try {
                        InputStream inputStream = getContentResolver().openInputStream(path);
                        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        String checkPath = methodeFunction.checkFilePath(path.getPath().toString());

                        if (checkPath == "true") {
                            int iteration = 0;

                            methodeFunction.deleteAll(this);

                            while ((line = bufferedReader.readLine()) != null) {
                                String[] str = line.split(",");

                                if (str.length != 10) {
                                    methodeFunction.toastMessage(this, getString(R.string.wrong_file));
                                    continue;
                                }

                                if (iteration == 0) {
                                    iteration++;
                                    continue;
                                }

                                String create = str[1].toString().replace("\"", "");
                                String deadline = str[2].toString().replace("\"", "");
                                String pembayaran = str[3].toString().replace("\"", "");
                                String nama = str[4].toString().replace("\"", "");
                                String phone = str[5].toString().replace("\"", "");
                                String jumlah = str[6].toString().replace("\"", "");
                                String keterangan = str[7].toString().replace("\"", "");
                                String kategori = str[8].toString().replace("\"", "");
                                String status = str[9].toString().replace("\"", "");

                                values.put(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_CREATE, Integer.parseInt(create));
                                values.put(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_DEADLINE, deadline);
                                values.put(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_PEMBAYARAN, pembayaran);
                                values.put(Contract.UtangEntry.COLUMN_UTANG_NAMA, nama);
                                values.put(Contract.UtangEntry.COLUMN_UTANG_PHONE, phone);
                                values.put(Contract.UtangEntry.COLUMN_UTANG_JUMLAH, Integer.parseInt(jumlah));
                                values.put(Contract.UtangEntry.COLUMN_UTANG_KETERANGAN, keterangan);
                                values.put(Contract.UtangEntry.COLUMN_UTANG_KATEGORI, Integer.parseInt(kategori));
                                values.put(Contract.UtangEntry.COLUMN_UTANG_STATUS, Integer.parseInt(status));

                                getContentResolver().insert(Contract.UtangEntry.CONTENT_URI, values);

                            }


                            methodeFunction.toastMessage(this, getString(R.string.setting_activity_success_import));

                        } else {
                            Toast.makeText(this, getString(R.string.wrong_extension), Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void importTest(String path) throws FileNotFoundException {
        File file = new File(path);
        String filename = file.getPath().replace("/external_files/", "");
        FileReader fileReader = new FileReader(filename);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        Toast.makeText(this, (CharSequence) fileReader, Toast.LENGTH_LONG).show();
    }

    private String checkExists(String nama, String jumlah, String tangga_peminjaman) {
        SQLiteDatabase database = new Helper(this).getReadableDatabase();
        String query = "SELECT * FROM utang WHERE utang_tanggal_create = "+tangga_peminjaman+" AND utang_jumlah = "+jumlah+ " AND utang_nama = " + nama;

        Cursor cursor = database.rawQuery(query, null);

        if (cursor.getCount() <=0 ) {
            cursor.close();
            return "0";
        }
        cursor.close();
        return "1";
    }
}
