package com.tonyofrancis.jiffygifs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tonyofrancis.jiffygifs.R;
import com.tonyofrancis.jiffygifs.adapter.GifListAdapter;
import com.tonyofrancis.jiffygifs.api.GifService;
import com.tonyofrancis.jiffygifs.model.GifItem;

import java.util.List;

/**
 * Created by tonyofrancis on 5/20/16.
 */

public class GifListFragment extends Fragment implements GifService.Callback {


    private GifListAdapter mGifListAdapter;

    public static GifListFragment newInstance() {
        return new GifListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gif_list,container,false);

        //Setup RecyclerView that will contain the GIF Items
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.gif_recycler_view);

        final int spanCount = getResources().getInteger(R.integer.gif_grid_span_count);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(spanCount,StaggeredGridLayoutManager.VERTICAL));

        mGifListAdapter = new GifListAdapter(getActivity());
        recyclerView.setAdapter(mGifListAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                int totalItemCount = recyclerView.getLayoutManager().getItemCount();

                int[] firstVisibleItemsPosition = ((StaggeredGridLayoutManager) recyclerView.getLayoutManager())
                        .findFirstVisibleItemPositions(null);

                if (visibleItemCount + firstVisibleItemsPosition[0] >= totalItemCount &&
                        firstVisibleItemsPosition[0] >= 0 && totalItemCount >= mGifListAdapter.getItemCount()) {
//
//                    MovieService.getInstance(getActivity().getApplicationContext())
//                            .fetchMoreFromPopularMovieListAsync(PopularMovieListFragment.this);
                }
            }
        });
        
        
        //Setup SearchView
        SearchView searchView = (SearchView) view.findViewById(R.id.gif_search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                //Query Database Asynchronously and return results to this fragment
                GifService.getInstance(getActivity())
                        .queryDatabaseAsync(query,GifListFragment.this);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


        //When Fragment become visible, fetch trending GIFS from database
        GifService.getInstance(getActivity())
                .fetchTrendingFromDatabaseAsync(this);
    }

    /** Callback method used by this fragment to receive the GIF dataSet
     * that will be displayed in the RecyclerView.
     * @param dataSet - List of GIF items that will be displayed in a RecyclerView
     * */
    @Override
    public void onDataLoaded(List<GifItem> dataSet) {
        mGifListAdapter.swapDataSet(dataSet);
    }

    @Override
    public void onDataLoaded(GifItem gifItem) {

    }
}
