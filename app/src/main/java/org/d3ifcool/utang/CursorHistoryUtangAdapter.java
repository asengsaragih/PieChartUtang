package org.d3ifcool.utang;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import org.d3ifcool.utang.base.MethodeFunction;
import org.d3ifcool.utang.database.Contract;

import java.text.NumberFormat;
import java.util.Locale;

public class CursorHistoryUtangAdapter extends CursorAdapter {

    public CursorHistoryUtangAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_history_utang, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        MethodeFunction methodeFunction = new MethodeFunction();

        TextView namaTextView = view.findViewById(R.id.textView_list_history_nama);
        TextView tanggalPeminjamanTextView = view.findViewById(R.id.textView_list_history_tanggal_peminjaman);
        TextView jumlahTextView = view.findViewById(R.id.textView_list_history_jumlah);

        String jumlahString = cursor.getString(cursor.getColumnIndex(Contract.UtangEntry.COLUMN_UTANG_JUMLAH));

        namaTextView.setText(cursor.getString(cursor.getColumnIndex(Contract.UtangEntry.COLUMN_UTANG_NAMA)));
        tanggalPeminjamanTextView.setText("Tanggal Pembayaran : " + methodeFunction.subStringTanggal(cursor.getString(cursor.getColumnIndex(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_PEMBAYARAN))));
        jumlahTextView.setText(methodeFunction.currencyIndonesian(Integer.parseInt(jumlahString)));
    }
}
