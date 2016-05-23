package com.tonyofrancis.jiffygifs.activity;

import android.support.v4.app.Fragment;

import com.tonyofrancis.jiffygifs.fragment.GifViewPagerFragment;

/**GifListActivity can be used to implement any fragment which
 * contains a ListView or a RecyclerView*/
public class GifListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return GifViewPagerFragment.newInstance();
    }
}
