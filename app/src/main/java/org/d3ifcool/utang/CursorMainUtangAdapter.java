package org.d3ifcool.utang;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import org.d3ifcool.utang.base.MethodeFunction;
import org.d3ifcool.utang.database.Contract;

import java.text.NumberFormat;
import java.util.Locale;

public class CursorMainUtangAdapter extends CursorAdapter {

    public CursorMainUtangAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    MethodeFunction methodeFunction = new MethodeFunction();

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_main_utang, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ConstraintLayout mainConstraint = view.findViewById(R.id.constraint_list_main);
        TextView namaTextView = view.findViewById(R.id.textView_list_nama);
        TextView tanggalPeminjamanTextView = view.findViewById(R.id.textView_list_tanggal_peminjaman);
        TextView jatuhTempoTextView = view.findViewById(R.id.textView_list_jatuh_tempo);
        TextView jumlahTextView = view.findViewById(R.id.textView_list_jumlah);

        String jumlahString = cursor.getString(cursor.getColumnIndex(Contract.UtangEntry.COLUMN_UTANG_JUMLAH));
        String tanggalPeminjamanString = cursor.getString(cursor.getColumnIndex(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_CREATE));

        String jatuhTempoString = cursor.getString(cursor.getColumnIndex(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_DEADLINE));

        String currentDate = methodeFunction.getCurrentDate();

        if (!TextUtils.isEmpty(jatuhTempoString)) {
            if (Integer.parseInt(currentDate) >= Integer.parseInt(jatuhTempoString)) {
                mainConstraint.setBackgroundResource(R.color.colorRedTransparent);

                namaTextView.setTextColor(Color.parseColor("#ffffff"));
                tanggalPeminjamanTextView.setTextColor(Color.parseColor("#ffffff"));
                jatuhTempoTextView.setTextColor(Color.parseColor("#ffffff"));
                jumlahTextView.setTextColor(Color.parseColor("#ffffff"));
            }
        }



        if (TextUtils.isEmpty(jatuhTempoString)) {
            jatuhTempoTextView.setText("Jatuh Tempo : - ");
        } else {
            jatuhTempoTextView.setText("Jatuh Tempo : " + methodeFunction.subStringTanggal(jatuhTempoString));
        }

        namaTextView.setText(cursor.getString(cursor.getColumnIndex(Contract.UtangEntry.COLUMN_UTANG_NAMA)));
        tanggalPeminjamanTextView.setText("Tanggal Peminjaman : " + methodeFunction.subStringTanggal(tanggalPeminjamanString));

        jumlahTextView.setText(methodeFunction.currencyIndonesian(Integer.parseInt(jumlahString)));

    }
}
