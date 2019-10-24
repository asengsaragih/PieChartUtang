package org.d3ifcool.utang;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class KategoriHistoryUtangAdapter extends FragmentPagerAdapter {
    private Context mContext;

    public KategoriHistoryUtangAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new DipinjamHistoryFragment();
        } else {
            return new MeminjamHistoryFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.dipinjam);
        } else {
            return mContext.getString(R.string.meminjam);
        }
    }
}
