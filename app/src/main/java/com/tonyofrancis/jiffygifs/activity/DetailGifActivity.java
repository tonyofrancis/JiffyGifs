package com.tonyofrancis.jiffygifs.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tonyofrancis.jiffygifs.fragment.DetailGifFragment;

/**
 * Created by tonyofrancis on 5/21/16.
 */

public class DetailGifActivity extends SingleFragmentActivity {

    private static final String GIF_ID = "com.tonyofrancis.activity.gif_id";

    @Override
    protected Fragment createFragment() {
        return DetailGifFragment.newInstance(getIntent().getStringExtra(GIF_ID));
    }

    public static Intent newIntent(Context packageContext,String id) {

        Intent intent = new Intent(packageContext,DetailGifActivity.class);
        intent.putExtra(GIF_ID,id);

        return intent;
    }
}
