package com.tonyofrancis.jiffygifs.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.tonyofrancis.jiffygifs.R;

/**
 * Created by tonyofrancis on 5/21/16.
 *
 * Abstract class that can be implemented by an activity that
 * will host a single fragment. Subclasses must implement the
 * createFragment method and return an instance of android.support.v4.app.Fragment
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);

        /*If a fragment is not attached to the activity, attach
        * the main fragment*/
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.findFragmentById(R.id.fragment_container) == null) {

            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container,createFragment())
                    .commit();
        }
    }
}
