package com.tonyofrancis.jiffygifs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tonyofrancis.jiffygifs.R;
import com.tonyofrancis.jiffygifs.adapter.GifListAdapter;
import com.tonyofrancis.jiffygifs.api.GifService;
import com.tonyofrancis.jiffygifs.helpers.Searchable;
import com.tonyofrancis.jiffygifs.helpers.SpacesItemDecoration;
import com.tonyofrancis.jiffygifs.model.GifItem;

import java.util.List;

/**
 * Created by tonyofrancis on 5/20/16.
 *
 * SearchGifListFragment implements the search functionality of the application.
 * This class uses the GifSerivce API to query the web service for Gifs related to
 * the specific search term and the results are displayed in a recyclerView.
 *
 */

public class SearchGifListFragment extends Fragment implements Searchable, GifService.Callback.OnListDataLoadedListener {

    private static final String QUERY = "com.tonyofrancis.search.query";

    private String mQuery;
    private boolean isNewSearch;
    private GifListAdapter mGifListAdapter;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;


    /**Static method used to get a properly formatted SearchGifListFragment
     * @return - properly formatted SearchGifListFragment
     * */
    public static SearchGifListFragment newInstance() {

        return new SearchGifListFragment();
    }

    /**Method that can be called by the hosting activity or fragment to perform
     * a search query in the fragment
     * @param query - Query String
     * */
    @Override
    public boolean performSearch(String query) {

        if(query == null) {
            return false;
        }

        mQuery = query;
        isNewSearch = true;

        GifService.queryDatabaseAsync(this, mQuery);

        return true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null) {
            mQuery = getString(R.string.default_query);
            isNewSearch = true;
        } else {
            mQuery = savedInstanceState.getString(QUERY);
            isNewSearch = false;
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(QUERY,mQuery);

    }

    @Override
    public void onStart() {
        super.onStart();

        GifService.queryDatabaseAsync(this,mQuery);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            super.onCreateView(inflater,container,savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_gif_list,container,false);

        //Setup RecyclerView that will contain the GIF Items
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.gif_recycler_view);

        final int span_size = getResources().getInteger(R.integer.col_span_size);
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(span_size,StaggeredGridLayoutManager.VERTICAL);

        mStaggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(mStaggeredGridLayoutManager); //Set LayoutManager

        final int decorSize = getResources().getInteger(R.integer.space_decor_size);
        recyclerView.addItemDecoration(new SpacesItemDecoration(decorSize));


        mGifListAdapter = new GifListAdapter(getActivity());
        recyclerView.setAdapter(mGifListAdapter);
        recyclerView.addOnScrollListener(mOnScrollListener);

        return view;
    }

    /**DataSet returned by the GifService to update the search recyclerView
     * @param dataSet - new dataSet
     * */
    @Override
    public void onDataLoaded(List<GifItem> dataSet) {

        if(isNewSearch) {
            mStaggeredGridLayoutManager.scrollToPositionWithOffset(0,0);
            mGifListAdapter.swapDataSet(dataSet);
        } else {
            mGifListAdapter.mergeDataSet(dataSet);
        }

        isNewSearch = false;
    }

    /**The mOnScrollListener object is attached to the recyclerView to implement
     * infinite scrolling **/
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {

        private ArrayMap<Integer,Boolean> mVisited = new ArrayMap<>();

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int[] lastVisibleItemsPosition = ((StaggeredGridLayoutManager) recyclerView.getLayoutManager())
                                              .findLastVisibleItemPositions(null);

                int max = 0;

                for (int x = 0; x < lastVisibleItemsPosition.length;x++) {
                    if (lastVisibleItemsPosition[x] > max) {
                        max = lastVisibleItemsPosition[x];
                    }
                }

            //If we are in the last row and we have not loaded this information previously fetch
            // more items for the gif service
                if(max + lastVisibleItemsPosition.length >= mGifListAdapter.getItemCount() &&
                        !mVisited.containsKey(mGifListAdapter.getItemCount())) {

                    mVisited.put(mGifListAdapter.getItemCount(),true);
                    GifService.queryDatabaseAsync(SearchGifListFragment.this,mQuery,mGifListAdapter.getItemCount() + 1);
                }
        }
    };
}
