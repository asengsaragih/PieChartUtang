package org.d3ifcool.utang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class HistoryActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //kodingan untuk two pane
        isTwoPane();
    }

    private void isTwoPane() {
        fragmentManager = this.getSupportFragmentManager();

        if (findViewById(R.id.dipinjam_history_container) != null) {
            fragmentManager.beginTransaction()
                    .add(R.id.meminjam_history_container, new MeminjamHistoryFragment())
                    .commit();

            fragmentManager.beginTransaction()
                    .add(R.id.dipinjam_history_container, new DipinjamHistoryFragment())
                    .commit();
        } else {
            //kodingan untuk viewpager dan tablayout
            mHistoryViewPager();
        }
    }

    private void mHistoryViewPager() {
        //kodingan untuk view pager
        mViewPager = findViewById(R.id.viewpager_history_utang);
        KategoriHistoryUtangAdapter kategoriHistoryUtangAdapter = new KategoriHistoryUtangAdapter(HistoryActivity.this, getSupportFragmentManager());
        mViewPager.setAdapter(kategoriHistoryUtangAdapter);

        //kodingan untuk tab layout
        mTabLayout = findViewById(R.id.tablayout_history_utang);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
