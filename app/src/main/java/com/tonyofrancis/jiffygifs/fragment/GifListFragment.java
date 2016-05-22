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
import com.tonyofrancis.jiffygifs.helpers.SpacesItemDecoration;
import com.tonyofrancis.jiffygifs.model.GifItem;

import java.util.List;

/**
 * Created by tonyofrancis on 5/20/16.
 */

public class GifListFragment extends Fragment implements GifService.Callback{

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

        final int span_size = getResources().getInteger(R.integer.col_span_size);
        StaggeredGridLayoutManager layoutManager
                = new StaggeredGridLayoutManager(span_size,StaggeredGridLayoutManager.VERTICAL);

        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(layoutManager); //Set LayoutManager

        final int decorSize = getResources().getInteger(R.integer.space_decor_size);
        recyclerView.addItemDecoration(new SpacesItemDecoration(decorSize));


        mGifListAdapter = new GifListAdapter(getActivity());
        recyclerView.setAdapter(mGifListAdapter);
        recyclerView.addOnScrollListener(mOnScrollListener);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        //When Fragment become visible, fetch trending GIFS from database
        GifService.getInstance(getActivity().getApplication())
                .fetchTrendingFromDatabaseAsync(this);
    }

    /** Callback method used by this fragment to receive the GIF dataSet
     * that will be displayed in the RecyclerView.
     * @param dataSet - List of GIF items that will be displayed in a RecyclerView
     * */
    @Override
    public void onDataLoaded(List<GifItem> dataSet) {
        mGifListAdapter.mergeDataSet(dataSet);
    }

    @Override
    public void onDataLoaded(GifItem gifItem) {

    }

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

                if(max + lastVisibleItemsPosition.length >= mGifListAdapter.getItemCount() &&
                        !mVisited.containsKey(mGifListAdapter.getItemCount())) {

                    mVisited.put(mGifListAdapter.getItemCount(),true);
                    GifService.getInstance(getActivity().getApplication())
                            .fetchTrendingFromDatabaseAsync(GifListFragment.this,mGifListAdapter.getItemCount()+1);
                }
        }
    };

}
