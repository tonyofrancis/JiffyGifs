package com.tonyofrancis.jiffygifs.activity;

import android.support.v4.app.Fragment;

import com.tonyofrancis.jiffygifs.fragment.GifListFragment;

public class GifListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return GifListFragment.newInstance();
    }
}
