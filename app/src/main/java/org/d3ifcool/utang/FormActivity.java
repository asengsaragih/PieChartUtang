package org.d3ifcool.utang;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.d3ifcool.utang.base.MethodeFunction;
import org.d3ifcool.utang.database.Contract;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class FormActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private final Calendar mCalendar = Calendar.getInstance();

    private String mPhoneNumber = "";
    private static final int EXISTING_UTANG_LOADER = 0;

    private Uri mCurrentUtangUri;

    private EditText mTanggalPeminjamanEditText;
    private EditText mJumlahEditText;
    private EditText mNamaEditText;
    private EditText mJatuhTempoEditText;
    private EditText mKeteranganEditText;
    private EditText mPhoneEditText;
    private RadioButton mDipinjamRadioButton;
    private RadioButton mMeminjamRadioButton;
    private RadioGroup mKategoriRadioGroup;
    private Button mTambahButton;
    private Button mPerbaruhiButton;
    private Button mHapusButton;
    private TextView mHapusJatuhTempoTextView;
    private TextView mJudulPhoneTextView;
    private ImageView mPickerPhoneImageView;

    private boolean mUtangHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mUtangHasChanged = true;
            return false;
        }
    };

    MethodeFunction methodeFunction = new MethodeFunction();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        mTanggalPeminjamanEditText = findViewById(R.id.edittext_form_tanggal_peminjaman);
        mNamaEditText = findViewById(R.id.edittext_form_nama);
        mJumlahEditText = findViewById(R.id.edittext_form_jumlah);
        mJatuhTempoEditText = findViewById(R.id.edittext_form_jatuh_tempo);
        mKeteranganEditText = findViewById(R.id.edittext_form_keterangan);
        mDipinjamRadioButton = findViewById(R.id.radiobutton_form_dipinjam);
        mMeminjamRadioButton = findViewById(R.id.radioButton_form_meminjam);
        mKategoriRadioGroup = findViewById(R.id.radiogroup_form_utang);
        mTambahButton = findViewById(R.id.button_form_tambah);
        mPerbaruhiButton = findViewById(R.id.button_form_perbaruhi);
        mHapusButton = findViewById(R.id.button_form_hapus);
        mHapusJatuhTempoTextView = findViewById(R.id.textview_form_hapus_jatuh_tempo);
        mPhoneEditText = findViewById(R.id.editText_form_phone);
        mPickerPhoneImageView = findViewById(R.id.imageView_form_phone);
        mJudulPhoneTextView = findViewById(R.id.textview_form_phone);

        mNamaEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        mGetRadioGroupData();

        mDatePickerTanggalPeminjaman();
        mDatePickerJatuhTempo();

        //untuk hapus data jatuh tempo
        mClearDataJatuhTempo();

        //untuk check kondisi uri database
        mGetDataUtang();

        mButtonData();

        mGetContactData();

        //untuk get tanggal hari ini ototmatis pada edittext
        mTanggalPeminjamanEditText.setText(getCurrentDate());

        //sistem currency pada jumlah edittext
        mJumlahEditText.addTextChangedListener(methodeFunction.jumlahChangeListener(mJumlahEditText));
    }

    private void mGetContactData() {
        mPickerPhoneImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPhoneEditText.setText("");
                mCheckPermission();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            Uri contactUri = data.getData();
            String[] projection = new String[] {ContactsContract.CommonDataKinds.Phone.NUMBER};
            Cursor cursor = getContentResolver().query(contactUri, projection, null, null ,null);

            try {
                if (cursor != null && cursor.moveToFirst()) {
                    String nomorponsel = "";
                    String numberContact = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    nomorponsel = numberContact;
                    mPhoneEditText.setText(nomorponsel);

                    LoaderManager.getInstance(this).destroyLoader(EXISTING_UTANG_LOADER);
                }
                cursor.close();
            } catch (Exception e) {

            }
        }
    }

    private void mCheckPermission() {
        if (ContextCompat.checkSelfPermission(FormActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(FormActivity.this, Manifest.permission.READ_CONTACTS)) {
                methodeFunction.readContactPermission(this);
                methodeFunction.readPhoneNumberPermission(this);
            } else {
                methodeFunction.readContactPermission(this);
                methodeFunction.readPhoneNumberPermission(this);
            }
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startActivityForResult(intent, 1);
        }
    }

    private void mKategoriCheckStatus() {
        if (mKategoriRadioGroup.getCheckedRadioButtonId() == R.id.radiobutton_form_dipinjam) {
            mPhoneEditText.setVisibility(View.VISIBLE);
            mJudulPhoneTextView.setVisibility(View.VISIBLE);
            mPickerPhoneImageView.setVisibility(View.VISIBLE);
        } else if (mKategoriRadioGroup.getCheckedRadioButtonId() == R.id.radioButton_form_meminjam) {
            mPhoneEditText.setVisibility(View.GONE);
            mJudulPhoneTextView.setVisibility(View.GONE);
            mPickerPhoneImageView.setVisibility(View.GONE);
        }

        mDipinjamRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPhoneEditText.setVisibility(View.VISIBLE);
                mJudulPhoneTextView.setVisibility(View.VISIBLE);
                mPickerPhoneImageView.setVisibility(View.VISIBLE);
            }
        });

        mMeminjamRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPhoneEditText.setVisibility(View.GONE);
                mJudulPhoneTextView.setVisibility(View.GONE);
                mPickerPhoneImageView.setVisibility(View.GONE);
            }
        });
    }

    private void mGetDataUtang() {
        Intent intent = getIntent();
        mCurrentUtangUri = intent.getData();

        mKategoriCheckStatus();

        if (mCurrentUtangUri == null) {
            setTitle(getString(R.string.form_activity_title_new_data));
            mHapusButton.setVisibility(View.GONE);
            mPerbaruhiButton.setVisibility(View.GONE);
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.form_activity_title_edit_data));
            mTambahButton.setVisibility(View.GONE);
            LoaderManager.getInstance(this).initLoader(EXISTING_UTANG_LOADER, null, this);
        }

        mTanggalPeminjamanEditText.setOnTouchListener(mTouchListener);
        mNamaEditText.setOnTouchListener(mTouchListener);
        mJumlahEditText.setOnTouchListener(mTouchListener);
        mPhoneEditText.setOnTouchListener(mTouchListener);
        mJatuhTempoEditText.setOnTouchListener(mTouchListener);
        mKeteranganEditText.setOnTouchListener(mTouchListener);
        mKategoriRadioGroup.setOnTouchListener(mTouchListener);
    }

    private void mGetRadioGroupData() {
        //pengambilan dataview paget untuk check kategori pada radio button
        Intent i = getIntent();
        int getKategoriViewPager = i.getIntExtra("dataRadio", 0);

        if (getKategoriViewPager == 1) {
            mKategoriRadioGroup.check(R.id.radiobutton_form_dipinjam);
        } else if (getKategoriViewPager == 2) {
            mKategoriRadioGroup.check(R.id.radioButton_form_meminjam);
        }
    }

    private void mClearDataJatuhTempo() {
        mHapusJatuhTempoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mJatuhTempoEditText.setText("");
            }
        });
    }

    private void mDatePickerTanggalPeminjaman() {
        //untuk date picker otomatis dalam edit text
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, month);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                mUpdateLabelTanggalPeminjaman();
            }
        };

        mTanggalPeminjamanEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(FormActivity.this, date, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void mDatePickerJatuhTempo() {
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, month);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                mUpdateLabelJatuhTempo();
            }
        };

        DatePicker datePicker = new DatePicker(this);
        datePicker.setMinDate(System.currentTimeMillis() - 1000);

        mJatuhTempoEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(FormActivity.this, date, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
    }

    private void mUpdateLabelTanggalPeminjaman() {
        //format date untuk sql
        String myFormat = "yyyy/MM/dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.US);

        mTanggalPeminjamanEditText.setText(simpleDateFormat.format(mCalendar.getTime()));
    }

    private void mUpdateLabelJatuhTempo() {
        //format date untuk sql
        String myFormat = "yyyy/MM/dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.US);

        mJatuhTempoEditText.setText(simpleDateFormat.format(mCalendar.getTime()));
    }

    private String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String formattedDate = simpleDateFormat.format(c.getTime());

        return formattedDate;
    }

    private void mButtonData() {
        mTambahButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSaveUtang();
                finish();
            }
        });

        mPerbaruhiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSaveUtang();
                finish();
            }
        });

        mHapusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowDeleteConfirmationBox();
            }
        });
    }

    private void mSaveUtang() {
        String tanggalPeminjamanString = mTanggalPeminjamanEditText.getText().toString().trim().replace("/","");
        String jatuhTempoString = mJatuhTempoEditText.getText().toString().trim();
        String namaString = mNamaEditText.getText().toString().trim();
        String jumlahString = mJumlahEditText.getText().toString().trim();
        String keteranganString = mKeteranganEditText.getText().toString().trim();
        String phoneString = mPhoneEditText.getText().toString().trim();

        if (TextUtils.isEmpty(jumlahString) || TextUtils.isEmpty(namaString)) {
            Toast.makeText(getApplicationContext(), getString(R.string.form_data_empty_warning), Toast.LENGTH_SHORT).show();
        } else {
            String jumlahReplace = jumlahString.replace(",","");
            int jumlahTotal = 0;

            if (!TextUtils.isEmpty(jumlahString)){
                jumlahTotal = Integer.parseInt(jumlahReplace);
            }

            if (jumlahTotal <= 999) {
                Toast.makeText(getApplicationContext(), getString(R.string.form_data_empty_total), Toast.LENGTH_SHORT).show();
            } else {
                ContentValues values = new ContentValues();
                values.put(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_CREATE, tanggalPeminjamanString);
                values.put(Contract.UtangEntry.COLUMN_UTANG_NAMA, namaString);
                values.put(Contract.UtangEntry.COLUMN_UTANG_JUMLAH, jumlahTotal);
                values.put(Contract.UtangEntry.COLUMN_UTANG_STATUS, 1);
                values.put(Contract.UtangEntry.COLUMN_UTANG_PHONE, phoneString);

                int selectKategori = mKategoriRadioGroup.getCheckedRadioButtonId();
                RadioButton radioKategori = findViewById(selectKategori);
                String textKategori = radioKategori.getText().toString();

                if (textKategori == getString(R.string.dipinjam)) {
                    values.put(Contract.UtangEntry.COLUMN_UTANG_KATEGORI, 1);
                } else if (textKategori == getString(R.string.meminjam)) {
                    values.put(Contract.UtangEntry.COLUMN_UTANG_KATEGORI, 2);
                }

                if (!TextUtils.isEmpty(jatuhTempoString)) {
                    values.put(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_DEADLINE, jatuhTempoString.replace("/",""));
                } else {
                    values.put(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_DEADLINE, "");
                }

                if (!TextUtils.isEmpty(keteranganString)) {
                    values.put(Contract.UtangEntry.COLUMN_UTANG_KETERANGAN, keteranganString);
                } else {
                    values.put(Contract.UtangEntry.COLUMN_UTANG_KETERANGAN, "");
                }

                if (mCurrentUtangUri == null) {
                    Uri newUri = getContentResolver().insert(Contract.UtangEntry.CONTENT_URI, values);

                    if (newUri == null) {
                        Toast.makeText(this, getString(R.string.form_activity_insert_failed), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, getString(R.string.form_activity_insert_success), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    int rowAffected = getContentResolver().update(mCurrentUtangUri, values, null, null);

                    if (rowAffected == 0) {
                        Toast.makeText(this, getString(R.string.form_activity_update_failed), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, getString(R.string.form_activity_update_success), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                Contract.UtangEntry._ID,
                Contract.UtangEntry.COLUMN_UTANG_TANGGAL_CREATE,
                Contract.UtangEntry.COLUMN_UTANG_TANGGAL_DEADLINE,
                Contract.UtangEntry.COLUMN_UTANG_NAMA,
                Contract.UtangEntry.COLUMN_UTANG_PHONE,
                Contract.UtangEntry.COLUMN_UTANG_KETERANGAN,
                Contract.UtangEntry.COLUMN_UTANG_KATEGORI,
                Contract.UtangEntry.COLUMN_UTANG_STATUS,
                Contract.UtangEntry.COLUMN_UTANG_JUMLAH

        };

        return new CursorLoader(this,
                mCurrentUtangUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1){
            return;
        }

        if (cursor.moveToFirst()) {
            int tanggalPeminjamanIndex = cursor.getColumnIndex(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_CREATE);
            int jatuhTempoIndex = cursor.getColumnIndex(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_DEADLINE);
            int namaIndex = cursor.getColumnIndex(Contract.UtangEntry.COLUMN_UTANG_NAMA);
            int phoneIndex = cursor.getColumnIndex(Contract.UtangEntry.COLUMN_UTANG_PHONE);
            int jumlahIndex = cursor.getColumnIndex(Contract.UtangEntry.COLUMN_UTANG_JUMLAH);
            int keteranganIndex = cursor.getColumnIndex(Contract.UtangEntry.COLUMN_UTANG_KETERANGAN);
            int statusIndex = cursor.getColumnIndex(Contract.UtangEntry.COLUMN_UTANG_STATUS);
            int kategoriIndex = cursor.getColumnIndex(Contract.UtangEntry.COLUMN_UTANG_KATEGORI);

            String stringTanggalPeminjaman = cursor.getString(tanggalPeminjamanIndex);
            String stringJatuhTempo = cursor.getString(jatuhTempoIndex);
            String stringNama = cursor.getString(namaIndex);
            String stringKeterangan = cursor.getString(keteranganIndex);
            String stringPhone = cursor.getString(phoneIndex);
            int intJumlah = cursor.getInt(jumlahIndex);
            int intKategori = cursor.getInt(kategoriIndex);
            int intStatus = cursor.getInt(statusIndex);

            mTanggalPeminjamanEditText.setText(methodeFunction.subStringTanggal(stringTanggalPeminjaman));

            if (!TextUtils.isEmpty(stringJatuhTempo)) {
                mJatuhTempoEditText.setText(methodeFunction.subStringTanggal(stringJatuhTempo));
            } else {
                mJatuhTempoEditText.setText(stringJatuhTempo);
            }

            mNamaEditText.setText(stringNama);
            mPhoneEditText.setText(stringPhone);
            mKeteranganEditText.setText(stringKeterangan);
            mJumlahEditText.setText(Integer.toString(intJumlah));


            switch (intKategori) {
                case Contract.UtangEntry.UTANG_KATEGORI_DIPINJAM:
                    mKategoriRadioGroup.check(R.id.radiobutton_form_dipinjam);
                    break;
                case Contract.UtangEntry.UTANG_KATEGORI_MEMINJAM:
                    mKategoriRadioGroup.check(R.id.radioButton_form_meminjam);
                    break;
                default:
                    mKategoriRadioGroup.check(0);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    public void onBackPressed() {
        if (!mUtangHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        };

        mShowUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!mUtangHasChanged) {
                    NavUtils.navigateUpFromSameTask(FormActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NavUtils.navigateUpFromSameTask(FormActivity.this);
                    }
                };

                mShowUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void mShowUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.form_activity_unsaved_dialog_message);
        builder.setPositiveButton(R.string.yes, discardButtonClickListener);
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (dialogInterface != null) {
                    dialogInterface.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void mDeteleUtang() {
        if (mCurrentUtangUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentUtangUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(getApplicationContext(), getString(R.string.form_activity_delete_failed), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.form_activity_delete_success), Toast.LENGTH_SHORT).show();
            }
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }

    private void mShowDeleteConfirmationBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.form_activity_delete_dialog_message);

        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (dialogInterface != null) {
                    dialogInterface.dismiss();
                }
            }
        });

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDeteleUtang();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
