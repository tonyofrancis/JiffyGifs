package com.tonyofrancis.jiffygifs.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tonyofrancis.jiffygifs.R;
import com.tonyofrancis.jiffygifs.activity.DetailGifActivity;
import com.tonyofrancis.jiffygifs.model.GifItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tonyofrancis on 5/20/16.
 *
 * GifListAdapter is a RecyclerView adapter that can be used to display
 * GifItems. This adapter is smart enough to swap out an existing
 * dataSet or merge new items with the current dataSet
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
        return new GifViewHolder(LayoutInflater.from(mContext).inflate(viewType,parent,false));
    }

    @Override
    public int getItemViewType(int position) {

        //Get the proper view type for a gif item based on its position
        if(position % 2 == 0) {
            return R.layout.gif_item_one;
        }

        return R.layout.gif_item_two;
    }

    @Override
    public void onBindViewHolder(GifViewHolder holder, int position) {
        holder.bind(mDataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    /**Method used to replace the entire dataSet held by
     * the adapter
     * @param newDataSet - new dataSet
     * */
    public void swapDataSet(List<GifItem> newDataSet) {

        if(newDataSet == null) {
            mDataSet = new ArrayList<>();
        } else {
            mDataSet = newDataSet;
        }

        notifyDataSetChanged();
    }

    /**Method used to merge new dataSet with the existing
     * dataSet held by the adapter
     * @param newDataSet - new dataSet
     * */
    public void mergeDataSet(List<GifItem> newDataSet) {

        if(newDataSet == null) {
            return;
        }

        int currentSize = getItemCount();
        mDataSet.addAll(newDataSet);
        notifyItemRangeInserted(currentSize+1,newDataSet.size());
    }

    public static class GifViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mImageView;
        private String id;

        public GifViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mImageView = (ImageView) itemView.findViewById(R.id.gif_image_view);
        }

        public void bind(GifItem gifItem) {
            this.id = gifItem.getId();

            //Display GIF Still image into the ImageView
            Picasso.with(mImageView.getContext())
                    .load(gifItem.getImages().getOriginal_still().getUrl())
                    .into(mImageView);

        }

        @Override
        public void onClick(View v) {
            //Start the detailed activity when an item is selected in the list
            v.getContext().startActivity(DetailGifActivity.newIntent(v.getContext(),id));
        }
    }
}
