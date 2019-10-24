package org.d3ifcool.utang;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

public class PickerActivity extends AppCompatActivity {

    private ListView mFilePickerListview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);

        mFilePickerListview = findViewById(R.id.listview_picker_file);
    }

    private void mShowFileList() {

    }
}
