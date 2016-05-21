package com.tonyofrancis.jiffygifs.api;

import com.tonyofrancis.jiffygifs.model.GifItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by tonyofrancis on 5/21/16.
 */

public final class GifService {

    private static GifService sGifService;
    private GifServiceAPI mGifServiceAPI;

    private GifService() {
        mGifServiceAPI = getDefaultRetrofitConfiguration()
                .create(GifServiceAPI.class);
    }

    /**Use this method to get an instance of the GifService class*/
    public static GifService getInstance() {

        if(sGifService == null) {
            sGifService = new GifService();
        }

        return sGifService;
    }

    /**Callback interface must be implemented by objects wanting to retrieve data (GIFItems)
     * from the GifService class*/
    public interface Callback {

        void onDataLoaded(List<GifItem> dataSet);
    }

    /**Method used to query the GifService for specific GIFS
     * @param callback - Callback that dataSet will be passed to after fetching
     * */
    public void queryDatabaseAsync(String query, final Callback callback) {

        if(callback != null) {

            mGifServiceAPI.query(query)
                    .enqueue(new retrofit2.Callback<GifItemResults>() {
                        @Override
                        public void onResponse(Call<GifItemResults> call, Response<GifItemResults> response) {

                            if(response.isSuccessful()) {
                                callback.onDataLoaded(response.body().getData());
                            }
                        }

                        @Override
                        public void onFailure(Call<GifItemResults> call, Throwable t) {

                        }
                    });
        }
    }

    /**Method used to query the GifService for Trending GIFS
     * @param callback - Callback that dataSet will be passed to after fetching*/
    public void fetchTrendingFromDatabaseAsync(final Callback callback) {

        if(callback != null) {
            mGifServiceAPI.queryTrending()
                    .enqueue(new retrofit2.Callback<GifItemResults>() {
                        @Override
                        public void onResponse(Call<GifItemResults> call, Response<GifItemResults> response) {

                            if (response.isSuccessful()) {
                                callback.onDataLoaded(response.body().getData());
                            }

                        }

                        @Override
                        public void onFailure(Call<GifItemResults> call, Throwable t) {

                        }
                    });
        }

    }

    /**Get a configured retrofit instance that will be
    * used to communicate with the GIF Service*/
    private Retrofit getDefaultRetrofitConfiguration() {
        return  new Retrofit.Builder()
                .baseUrl(GifServiceAPI.BASE_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**The GifServiceAPI in instantiated by the Retrofit framework and is
     * used to communicate with Web Endpoints*/
    private interface GifServiceAPI {
        String BASE_API = "http://api.giphy.com";
        String API_KEY = "dc6zaTOxFJmzC";
        int API_LIMIT = 50;

        @GET("/v1/gifs/search?&api_key="+API_KEY+"&limit="+API_LIMIT)
        Call<GifItemResults> query(@Query("q")String query);

        @GET("/v1/gifs/trending?&api_key="+API_KEY)
        Call<GifItemResults> queryTrending();

    }

    /**Helper class used to Map GifItems from thr GifServiceAPI*/
    private static class GifItemResults {
        private List<GifItem> data;

        public List<GifItem> getData() {
            return data;
        }

        public void setData(List<GifItem> data) {
            this.data = data;
        }
    }

}
