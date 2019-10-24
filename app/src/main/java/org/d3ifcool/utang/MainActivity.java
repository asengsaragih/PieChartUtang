package org.d3ifcool.utang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton mFloatingButtonForm;
    private BottomAppBar mBottomBarApp;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //kodingan untuk cek two pane layout
        isTwoPane();
    }

    private void isTwoPane() {
        fragmentManager = this.getSupportFragmentManager();

        if (findViewById(R.id.dipinjam_container) != null) {
            fragmentManager.beginTransaction()
                    .add(R.id.dipinjam_container, new DipinjamMainFragment())
                    .commit();

            fragmentManager.beginTransaction()
                    .add(R.id.meminjam_container, new MeminjamMainFragment())
                    .commit();

            mMainFloatingButtonFormTabletMode();

            mMainBottomBar();
        } else {
            //koidngan untuk view pager pada android biasa
            mMainViewPager();
        }
    }

    private void mMainViewPager(){
        //kodingan untuk view pager
        mViewPager = findViewById(R.id.viewpager_main_utang);
        KategoriMainUtangAdapter utangAdapter = new KategoriMainUtangAdapter(MainActivity.this, getSupportFragmentManager());
        mViewPager.setAdapter(utangAdapter);

        //kodingan untuk view pager
        mTabLayout = findViewById(R.id.tablayout_main_utang);
        mTabLayout.setupWithViewPager(mViewPager);

        //kodingan untuk floating button
        mMainFloatingButtonForm();

        //kodingan untuk bottom app bar
        mMainBottomBar();
    }

    private void mMainFloatingButtonForm(){
        // kodingan untuk floating action button
        mFloatingButtonForm = findViewById(R.id.floatingbutton_main_utang);
        mFloatingButtonForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FormActivity.class);
                int getDataViewPager = 0;

                if (mViewPager.getCurrentItem() == 0) {
                    getDataViewPager = 1;
                } else {
                    getDataViewPager = 2;
                }

                intent.putExtra("dataRadio", getDataViewPager);
                startActivity(intent);
            }
        });
    }

    private void mMainFloatingButtonFormTabletMode() {
        mFloatingButtonForm = findViewById(R.id.floatingbutton_main_utang);
        mFloatingButtonForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FormActivity.class);
                int setDataKategori = 1;
                intent.putExtra("dataRadio", setDataKategori);
                startActivity(intent);
            }
        });
    }

    private void mMainBottomBar(){
        // kodingan untuk bottom app bar
        mBottomBarApp = findViewById(R.id.bottom_app_bar);
        mBottomBarApp.replaceMenu(R.menu.bottom_menu);

        // kodingan click untuk icon setting
        mBottomBarApp.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_setting:
                        Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });

        // kodingan click untuk icon home
        mBottomBarApp.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ChartActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_history){
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
