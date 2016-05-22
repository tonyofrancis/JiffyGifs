package com.tonyofrancis.jiffygifs.activity;

import android.support.v4.app.Fragment;

import com.tonyofrancis.jiffygifs.fragment.GifViewPagerFragment;

public class GifListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return GifViewPagerFragment.newInstance();
    }
}
