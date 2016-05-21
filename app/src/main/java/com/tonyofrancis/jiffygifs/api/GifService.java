package com.tonyofrancis.jiffygifs.api;

import android.content.Context;
import android.net.ConnectivityManager;

import com.tonyofrancis.jiffygifs.model.GifItem;

import java.io.IOException;
import java.util.List;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by tonyofrancis on 5/21/16.
 */

public final class GifService {

    private static GifService sGifService;
    private GifServiceAPI mGifServiceAPI;

    private Context mContext;

    private GifService(Context packageContext) {
        mContext = packageContext;

        mGifServiceAPI = getDefaultRetrofitConfiguration()
                .create(GifServiceAPI.class);
    }

    /**Use this method to get an instance of the GifService class*/
    public static GifService getInstance(Context packageContext) {

        if(sGifService == null) {
            sGifService = new GifService(packageContext);
        }

        return sGifService;
    }

    /**Callback interface must be implemented by objects wanting to retrieve data (GIFItems)
     * from the GifService class*/
    public interface Callback {

        void onDataLoaded(List<GifItem> dataSet);
        void onDataLoaded(GifItem gifItem);
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

    public void fetchGifWithIdAsync(String id, final Callback callback) {

        if(callback != null) {

            mGifServiceAPI.queryId(id)
                    .enqueue(new retrofit2.Callback<GifItemResult>() {
                        @Override
                        public void onResponse(Call<GifItemResult> call, Response<GifItemResult> response) {

                            if(response.isSuccessful()) {

                                callback.onDataLoaded(response.body().getData());
                            }
                        }

                        @Override
                        public void onFailure(Call<GifItemResult> call, Throwable t) {

                        }
                    });
        }
    }

    /**Get a configured retrofit instance that will be
    * used to communicate with the GIF Service*/
    private Retrofit getDefaultRetrofitConfiguration() {
        return  new Retrofit.Builder()
                .client(getOkHttpCacheClient())
                .baseUrl(GifServiceAPI.BASE_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    private OkHttpClient getOkHttpCacheClient() {
        return new OkHttpClient.Builder()
                .cache(new Cache(mContext.getCacheDir(),GifServiceAPI.API_CACHE_SIZE))
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();

                        if(isNetworkAvailable()) {
                            request = request.newBuilder()
                                    .header("Cache-Control","public, max-age="+300)
                                    .build();
                            ;
                        } else {
                            request = request.newBuilder()
                                    .header("Cache-Control","public, only-if-cached, max-stale="+ 60*60*24*7)
                                    .build();
                        }

                        return chain.proceed(request);
                    }
                })
                .build();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    /**The GifServiceAPI in instantiated by the Retrofit framework and is
     * used to communicate with Web Endpoints*/
    private interface GifServiceAPI {
        String BASE_API = "http://api.giphy.com";
        String API_KEY = "dc6zaTOxFJmzC";
        int API_LIMIT = 50;
        int API_CACHE_SIZE = 10 * 1024 * 1024;

        @GET("/v1/gifs/search?&api_key="+API_KEY+"&limit="+API_LIMIT)
        Call<GifItemResults> query(@Query("q")String query);

        @GET("/v1/gifs/trending?&api_key="+API_KEY)
        Call<GifItemResults> queryTrending();

        @GET("/v1/gifs/{id}?&api_key="+API_KEY)
        Call<GifItemResult> queryId(@Path("id")String id);
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

    /**Helper class used to Map GifItems from thr GifServiceAPI*/
    private static class GifItemResult {
        private GifItem data;

        public GifItem getData() {
            return data;
        }

        public void setData(GifItem data) {
            this.data = data;
        }
    }

}
