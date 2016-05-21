package com.tonyofrancis.jiffygifs.api;

import com.tonyofrancis.jiffygifs.model.GifItem;

import java.util.List;

/**
 * Created by tonyofrancis on 5/21/16.
 */

public final class GifService {

    private static GifService sGifService;

    private GifService() {

    }

    public static GifService getInstance() {

        if(sGifService == null) {
            sGifService = new GifService();
        }

        return sGifService;
    }

    public interface Callback {

        void onDataLoaded(List<GifItem> dataSet);
    }

    public void queryDatabaseAsync(String query,Callback callback) {

    }

    public void fetchFromDatabaseAsync(Callback callback) {

    }

}
