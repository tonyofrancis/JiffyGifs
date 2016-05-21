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
        return new GifViewHolder(LayoutInflater.from(mContext).inflate(R.layout.gif_item,parent,false));
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

    public class GifViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
            Picasso.with(mContext)
                    .load(gifItem.getImages().getOriginal_still().getUrl())
                    .into(mImageView);

        }

        @Override
        public void onClick(View v) {
            mContext.startActivity(DetailGifActivity.newIntent(mContext,id));
        }
    }
}
