package com.tonyofrancis.jiffygifs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.gif_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mGifListAdapter = new GifListAdapter(getActivity());
        recyclerView.setAdapter(mGifListAdapter);
        
        
        //Setup SearchView

        SearchView searchView = (SearchView) view.findViewById(R.id.gif_search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                GifService.getInstance()
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
    public void onResume() {
        super.onResume();

        GifService.getInstance()
                .fetchFromDatabaseAsync(this);
    }

    @Override
    public void onDataLoaded(List<GifItem> dataSet) {
        mGifListAdapter.swapDataSet(dataSet);
    }
}
