package org.d3ifcool.utang;


import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.d3ifcool.utang.base.MethodeFunction;
import org.d3ifcool.utang.database.Contract;
import org.d3ifcool.utang.database.Helper;

import java.text.NumberFormat;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeminjamMainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    public MeminjamMainFragment() {
        // Required empty public constructor
    }

    private static final int UTANG_MEMINJAM_LOADER = 1;

    CursorMainUtangAdapter mCursorAdapter;

    MethodeFunction methodeFunction = new MethodeFunction();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_meminjam, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDisplayMeminjamListview();

        LoaderManager.getInstance(getActivity()).initLoader(UTANG_MEMINJAM_LOADER, null, MeminjamMainFragment.this);
    }


    private void mDisplayMeminjamListview() {
        final ListView listView = getView().findViewById(R.id.listview_meminjam);
        View emptyView = getView().findViewById(R.id.emptyview_meminjam);

        mCursorAdapter = new CursorMainUtangAdapter(getActivity(), null);

        listView.setEmptyView(emptyView);
        listView.setAdapter(mCursorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, final long id) {
                final Cursor cursor = (Cursor) listView.getItemAtPosition(i);

                detailUtang(getContext(), cursor, id);
            }
        });
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

        //pengkondisian status dan kategori menggunakan clausa where
        String clausaWhere = Contract.UtangEntry.COLUMN_UTANG_STATUS+"=?"+" AND "+ Contract.UtangEntry.COLUMN_UTANG_KATEGORI+"=?";
        String statusWhere = "1";
        String kategoriWhere = "2";
        String[] whereArgs = {
                statusWhere.toString(),
                kategoriWhere.toString()
        };

        return new CursorLoader(getActivity(),
                Contract.UtangEntry.CONTENT_URI,
                projection,
                clausaWhere,
                whereArgs,
                Contract.UtangEntry.COLUMN_UTANG_TANGGAL_DEADLINE + " ASC, " + Contract.UtangEntry.COLUMN_UTANG_TANGGAL_CREATE + " DESC");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    private void detailUtang(final Context context, Cursor cursor, final long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View detailView = getActivity().getLayoutInflater().inflate(R.layout.dialog_detail_main, null);

        builder.setView(detailView);
        final AlertDialog alertDialog = builder.create();

        TextView tanggalPeminjamanDetailTextView = detailView.findViewById(R.id.textView_detail_tanggal_peminjaman);
        TextView namaDetailTextView = detailView.findViewById(R.id.textView_detail_nama);
        TextView jumlahDetailTextView = detailView.findViewById(R.id.textView_detail_jumlah);
        TextView keteranganDetailTextView = detailView.findViewById(R.id.textView_detail_keterangan);
        TextView jatuhTempoDetailTextView = detailView.findViewById(R.id.textView_detail_jatuh_tempo);
        TextView phoneDetailTextView = detailView.findViewById(R.id.textView_detail_phone);

        final EditText jumlahCicilEditText = detailView.findViewById(R.id.editText_detail_cicilan);
        jumlahCicilEditText.addTextChangedListener(methodeFunction.jumlahChangeListener(jumlahCicilEditText));

        Button markAsDoneDetailButton = detailView.findViewById(R.id.button_detail_mark_as_done);
        Button cicilDetailButton = detailView.findViewById(R.id.button_detail_cicil);
        Button editDetailButton = detailView.findViewById(R.id.button_detail_edit);
        Button yesCicilDetailButton = detailView.findViewById(R.id.button_detail_yes);
        Button cancleCicilDetailButton = detailView.findViewById(R.id.button_detail_cancle);
        Button messageDetailButton = detailView.findViewById(R.id.button_detail_message);
        Button callDetailButton = detailView.findViewById(R.id.button_detail_call);

        final ConstraintLayout detailUtangView = detailView.findViewById(R.id.constraitLayout_detail);
        final ConstraintLayout cicilUtangView = detailView.findViewById(R.id.constraitLayout_detail_payment_cicil);

        final String tanggalPeminjamanString = cursor.getString(cursor.getColumnIndexOrThrow(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_CREATE));
        final String namaString = cursor.getString(cursor.getColumnIndexOrThrow(Contract.UtangEntry.COLUMN_UTANG_NAMA));
        final String jumlahString = cursor.getString(cursor.getColumnIndexOrThrow(Contract.UtangEntry.COLUMN_UTANG_JUMLAH));
        final String jatuhTempoString = cursor.getString(cursor.getColumnIndexOrThrow(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_DEADLINE));
        final String keteranganString = cursor.getString(cursor.getColumnIndexOrThrow(Contract.UtangEntry.COLUMN_UTANG_KETERANGAN));
        final String phoneString = cursor.getString(cursor.getColumnIndex(Contract.UtangEntry.COLUMN_UTANG_PHONE));
        final String kategoriString = cursor.getString(cursor.getColumnIndex(Contract.UtangEntry.COLUMN_UTANG_KATEGORI));

        tanggalPeminjamanDetailTextView.setText("Tanggal Peminjaman : " + methodeFunction.subStringTanggal(tanggalPeminjamanString));
        namaDetailTextView.setText("Nama : " + namaString);
        jumlahDetailTextView.setText("Jumlah " + methodeFunction.currencyIndonesian(Integer.parseInt(jumlahString)));

        if (!TextUtils.isEmpty(jatuhTempoString)) {
            jatuhTempoDetailTextView.setText("Jatuh Tempo : " + methodeFunction.subStringTanggal(jatuhTempoString));
        } else  {
            jatuhTempoDetailTextView.setText("Jatuh Tempo : -");
        }

        if (!TextUtils.isEmpty(keteranganString)) {
            keteranganDetailTextView.setText("Keterangan : " + keteranganString);
        } else  {
            keteranganDetailTextView.setText("Keterangan : -");
        }

        if (!TextUtils.isEmpty(phoneString)) {
            phoneDetailTextView.setText("Nomor Ponsel : " + phoneString);
        } else  {
            phoneDetailTextView.setText("Nomor Ponsel : -");
        }

        //untuk view gone
        cicilUtangView.setVisibility(View.GONE);
        if (kategoriString == "1") {
            if (!TextUtils.isEmpty(phoneString)) {
                callDetailButton.setVisibility(View.VISIBLE);
                messageDetailButton.setVisibility(View.VISIBLE);
            } else {
                callDetailButton.setVisibility(View.GONE);
                messageDetailButton.setVisibility(View.GONE);
            }
        } else {
            callDetailButton.setVisibility(View.GONE);
            messageDetailButton.setVisibility(View.GONE);
        }
        //end view gone

        final Uri currentUtangUri = ContentUris.withAppendedId(Contract.UtangEntry.CONTENT_URI, id);
        final ContentValues values = new ContentValues();
        final ContentValues cicilValues = new ContentValues();

        markAsDoneDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                values.put(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_CREATE, tanggalPeminjamanString);
                values.put(Contract.UtangEntry.COLUMN_UTANG_NAMA, namaString);
                values.put(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_PEMBAYARAN, Integer.parseInt(methodeFunction.getCurrentDate()));
                values.put(Contract.UtangEntry.COLUMN_UTANG_JUMLAH, Integer.parseInt(jumlahString));
                values.put(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_DEADLINE, jatuhTempoString);
                values.put(Contract.UtangEntry.COLUMN_UTANG_KETERANGAN, keteranganString);
                values.put(Contract.UtangEntry.COLUMN_UTANG_STATUS, 2);
                values.put(Contract.UtangEntry.COLUMN_UTANG_KATEGORI, 2);

                int rowAffected = getActivity().getContentResolver().update(currentUtangUri, values, null, null);

                if (rowAffected == 0) {
                    Toast.makeText(getActivity(), getString(R.string.main_activity_update_failed), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.main_activity_update_success), Toast.LENGTH_SHORT).show();
                }
                getActivity().recreate();
            }
        });

        cicilDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detailUtangView.setVisibility(View.GONE);
                cicilUtangView.setVisibility(View.VISIBLE);
            }
        });

        editDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FormActivity.class);
                final Uri currentMeminjamUri = ContentUris.withAppendedId(Contract.UtangEntry.CONTENT_URI, id);
                intent.setData(currentMeminjamUri);
                startActivity(intent);

                alertDialog.cancel();
            }
        });

        yesCicilDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cicilTotalRaw = jumlahCicilEditText.getText().toString();

                String cicilTotalString;
                int cicilTotal = 0;

                if (cicilTotalRaw.contains(",")) {
                    cicilTotalString = cicilTotalRaw.replace("," ,"");
                    cicilTotal = Integer.parseInt(cicilTotalString);
                }

                if (cicilTotal == 0 || cicilTotal >= Integer.parseInt(jumlahString)) {
                    Toast.makeText(getActivity(), "Cicilan Tidak Boleh kurang dari 1000 atau lebih besar dari pada utang", Toast.LENGTH_SHORT).show();
                    return;
                }

                int substractionTotal = Integer.parseInt(jumlahString) - cicilTotal;

                values.put(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_CREATE, tanggalPeminjamanString);
                values.put(Contract.UtangEntry.COLUMN_UTANG_NAMA, namaString);
                values.put(Contract.UtangEntry.COLUMN_UTANG_PHONE, phoneString);
                values.put(Contract.UtangEntry.COLUMN_UTANG_JUMLAH, substractionTotal);
                values.put(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_DEADLINE, jatuhTempoString);
                values.put(Contract.UtangEntry.COLUMN_UTANG_KETERANGAN, keteranganString);
                values.put(Contract.UtangEntry.COLUMN_UTANG_STATUS, 1);
                values.put(Contract.UtangEntry.COLUMN_UTANG_KATEGORI, 2);

                int rowAffected = getActivity().getContentResolver().update(currentUtangUri, values, null, null);

                if (rowAffected == 0) {
                    Toast.makeText(getActivity(), getString(R.string.main_activity_update_failed), Toast.LENGTH_SHORT).show();
                } else {

                    cicilValues.put(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_CREATE, tanggalPeminjamanString);
                    cicilValues.put(Contract.UtangEntry.COLUMN_UTANG_NAMA, namaString);
                    cicilValues.put(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_PEMBAYARAN, Integer.parseInt(methodeFunction.getCurrentDate()));
                    cicilValues.put(Contract.UtangEntry.COLUMN_UTANG_JUMLAH, cicilTotal);
                    cicilValues.put(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_DEADLINE, jatuhTempoString);
                    cicilValues.put(Contract.UtangEntry.COLUMN_UTANG_KETERANGAN, keteranganString);
                    cicilValues.put(Contract.UtangEntry.COLUMN_UTANG_STATUS, 2);
                    cicilValues.put(Contract.UtangEntry.COLUMN_UTANG_KATEGORI, 2);

                    Uri newUri = getActivity().getContentResolver().insert(Contract.UtangEntry.CONTENT_URI, cicilValues);

                    if (newUri == null) {
                        Toast.makeText(getActivity(), getString(R.string.form_activity_insert_failed), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.form_activity_insert_success), Toast.LENGTH_SHORT).show();
                    }
                    getActivity().recreate();
                }
            }
        });

        cancleCicilDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detailUtangView.setVisibility(View.VISIBLE);
                cicilUtangView.setVisibility(View.GONE);
            }
        });

        alertDialog.show();
    }
}
