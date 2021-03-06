package com.tonyofrancis.jiffygifs.api;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.LruCache;

import com.tonyofrancis.jiffygifs.model.GifItem;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
import retrofit2.http.QueryMap;

/**
 * Created by tonyofrancis on 5/21/16.
 *
 * The GifService is a singleton class that is responsible
 * for fetching and querying gif information from the Gliphy API
 * or local cache. All fetching tasks are performed asynchronously
 * and the results are returned to the callback object.
 *
 */

public final class GifService {

    private static GifService sGifService;

    private GifServiceAPI mGifServiceAPI;
    private LruCache<String,GifItem> mCache; //local memory cache for gifs
    private Context mContext;

    private GifService(Context packageContext) {
        mContext = packageContext;
        mCache = new LruCache<>(GifServiceAPI.API_LIMIT);
        mGifServiceAPI = getDefaultRetrofitConfiguration()
                .create(GifServiceAPI.class);

    }

    /**Use this method to create an instance of the GifService class
     * @param packageContext - Application instance is encouraged because it is the longest
     *                       lasting context
     * */
    public static void initialize(Application packageContext) {

        if(sGifService == null) {
            sGifService = new GifService(packageContext);
        }
    }

    public static void flushCache() {
        sGifService.mCache.evictAll();
    }

    /**Callback interfaces that must be implemented by objects wanting to retrieve data (GIFItems)
     * from the GifService class*/
    public static final class Callback {

        public interface OnListDataLoadedListener {
            void onDataLoaded(List<GifItem> dataSet);
        }
        public interface OnItemDataLoadedListener {
            void onDataLoaded(GifItem gifItem);
        }
    }


    /**Method used to query the GifService for specific GIFS
     * @param callback - Callback that dataSet will be passed to after fetching
     *@param query  - Query String
     * */
    public static void queryDatabaseAsync(Callback.OnListDataLoadedListener callback,String query) {
        sGifService.queryDatabaseAsync(callback,query,GifServiceAPI.DEFAULT_OFFSET);
    }

    /**Method used to query the GifService for specific GIFS
     * @param callback - Callback that dataSet will be passed to after fetching
     * @param query - Query String
     * @param offset - Next Position to fetch data
     * */
    public static void queryDatabaseAsync(final Callback.OnListDataLoadedListener callback,String query,int offset) {

        if(callback != null) {

            Map<String,String> map = new ArrayMap<>();
            map.put("q",query);
            map.put("offset",String.valueOf(offset));

            sGifService.mGifServiceAPI.query(map)
                    .enqueue(new retrofit2.Callback<GifItemResults>() {
                        @Override
                        public void onResponse(Call<GifItemResults> call, Response<GifItemResults> response) {

                            if(response.isSuccessful()) {
                                sGifService.updateCache(response.body().getData());
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
    public static void fetchTrendingFromDatabaseAsync(Callback.OnListDataLoadedListener callback) {
        sGifService.fetchTrendingFromDatabaseAsync(callback,GifServiceAPI.DEFAULT_OFFSET);
    }

    /**Method used to query the GifService for Trending GIFS
     * @param callback - Callback that dataSet will be passed to after fetching
     * @param offset - Next Position to fetch data*/
    public static void fetchTrendingFromDatabaseAsync(final Callback.OnListDataLoadedListener callback,int offset) {

        if(callback != null) {
            sGifService.mGifServiceAPI.queryTrending(offset)
                    .enqueue(new retrofit2.Callback<GifItemResults>() {
                        @Override
                        public void onResponse(Call<GifItemResults> call, Response<GifItemResults> response) {

                            if (response.isSuccessful()) {
                                sGifService.updateCache(response.body().getData());
                                callback.onDataLoaded(response.body().getData());
                            }
                        }

                        @Override
                        public void onFailure(Call<GifItemResults> call, Throwable t) {
                        }
                    });
        }

    }

    /**This method updates the local LRUCache with
     * the last fetched dataSet
     * @param dataSet - recently fetched dataSet
     * */
    private void updateCache(List<GifItem> dataSet) {

        if(dataSet == null) {
            return;
        }

        sGifService.mCache.evictAll();

        for (GifItem item: dataSet) {
            sGifService.mCache.put(item.getId(),item);
        }
    }


    /**Method used to fetch a single Gif Item. The local cache is checked
     * first to find the gif. If the gif is not found a network call is placed
     * @param id - Gif ID
     * @param callback - Callback that dataSet will be passed to after fetching
     * */
    public static void fetchGifWithIdAsync(String id, final Callback.OnItemDataLoadedListener callback) {

        if(callback != null) {

            //If item is in local cache fetch it otherwise get it from the network
            if(sGifService.mCache != null) {
                GifItem item = sGifService.mCache.get(id);

                if(item != null) {
                    callback.onDataLoaded(item);
                    return;
                }
            }

            sGifService.mGifServiceAPI.queryId(id)
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

    /**Get a configured OkHttpClient instance that supports local
     * caching of network requests.*/
    private OkHttpClient getOkHttpCacheClient() {
        return new OkHttpClient.Builder()
                .cache(new Cache(mContext.getCacheDir(),GifServiceAPI.API_CACHE_SIZE))
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();

                        if(isNetworkAvailable()) {

                            //If the network is available fetch new data every 5 mins from the network
                            // otherwise use the local cache to fetch the last request.
                            request = request.newBuilder()
                                    .header("Cache-Control","public, max-age="+300)
                                    .build();
                        } else {

                            //If the network is not available used the last cached request up
                            // to 7 days ago
                            request = request.newBuilder()
                                    .header("Cache-Control","public, only-if-cached, max-stale="+ 60*60*24*7)
                                    .build();
                        }

                        return chain.proceed(request);
                    }
                })
                .build();
    }

    /**Method used to determine if the device is connected to a network*/
    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    /**The GifServiceAPI in instantiated by the Retrofit framework and is
     * used to communicate with Web Endpoints*/
    private interface GifServiceAPI {
        String BASE_API = "http://api.giphy.com";
        String API_KEY = "dc6zaTOxFJmzC";
        int DEFAULT_OFFSET = 0;
        int API_LIMIT = 50;
        int API_CACHE_SIZE = 10 * 1024 * 1024;

        @GET("/v1/gifs/search?&api_key="+API_KEY+"&limit="+API_LIMIT)
        Call<GifItemResults> query(@QueryMap Map<String,String> options);

        @GET("/v1/gifs/trending?&api_key="+API_KEY+"&limit="+API_LIMIT)
        Call<GifItemResults> queryTrending(@Query("offset")int offset);

        @GET("/v1/gifs/{id}?&api_key="+API_KEY)
        Call<GifItemResult> queryId(@Path("id")String id);
    }

    /**Helper class used to Map GifItems from thr GifServiceAPI*/
    public static class GifItemResults {
        private List<GifItem> data;

        public List<GifItem> getData() {
            return data;
        }

        public void setData(List<GifItem> data) {
            this.data = data;
        }
    }

    /**Helper class used to Map GifItems from thr GifServiceAPI*/
    public static class GifItemResult {
        private GifItem data;

        public GifItem getData() {
            return data;
        }

        public void setData(GifItem data) {
            this.data = data;
        }
    }

}
