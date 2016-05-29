package com.tonyofrancis.jiffygifs;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.tonyofrancis.jiffygifs.api.GifService;

/**
 * Created by tonyofrancis on 5/28/16.
 */

public class JiffyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        /*Initialize app services here*/
        Fresco.initialize(this);
        GifService.initialize(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        GifService.flushCache();
    }

}
