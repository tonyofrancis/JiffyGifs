package com.tonyofrancis.jiffygifs.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tonyofrancis.jiffygifs.R;
import com.tonyofrancis.jiffygifs.model.GifItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tonyofrancis on 5/20/16.
 */

public class GifListAdapter extends RecyclerView.Adapter<GifListAdapter.GifViewHolder> {

    private List<GifItem> mDataSet;
    private Context mContext;


    public GifListAdapter(Context context) {
        super();
        mContext = context;
        mDataSet = new ArrayList<>();
    }

    @Override
    public GifViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.gif_item,parent,false);
        return new GifViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GifViewHolder holder, int position) {
        holder.bind(mDataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void swapDataSet(List<GifItem> newDataSet) {

        if(newDataSet == null) {
            mDataSet = new ArrayList<>();
        } else {
            mDataSet = newDataSet;
        }

        notifyDataSetChanged();
    }

    public static class GifViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private String Id;

        public GifViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(GifItem gifItem) {
            this.Id = gifItem.getId();
        }

        @Override
        public void onClick(View v) {

        }
    }
}
