package com.tonyofrancis.jiffygifs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tonyofrancis.jiffygifs.R;
import com.tonyofrancis.jiffygifs.api.GifService;
import com.tonyofrancis.jiffygifs.model.GifItem;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tonyofrancis on 5/21/16.
 *
 * DetailGifFragment contains the views and functionality
 * that will display the animated Gif. A gif id must be
 * passed into the setArguments method before the fragment
 * is committed and properly configured.
 *
 */

public class DetailGifFragment extends Fragment implements GifService.Callback.OnItemDataLoadedListener {

    private static final String GIF_ID = "com.tonyofrancis.fragments.gif_id";

    private String mGifId;

    @BindView(R.id.gif_video_view)
    protected SimpleDraweeView mSimpleDraweeView;

    /**Static method used to get a properly formatted DetailGifFragment
     * @param id - The id string of a GifItem
     * @return - properly formatted DetailGifFragment
     * */
    public static DetailGifFragment newInstance(String id) {

        Bundle args = new Bundle();
        args.putString(GIF_ID,id);

        DetailGifFragment fragment = new DetailGifFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Restore id of gif on fragment re-creation
        if(savedInstanceState != null) {
            mGifId = savedInstanceState.getString(GIF_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_gif_detail,container,false);
        ButterKnife.bind(this,view);

        GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(getResources());
        ProgressBarDrawable progressBarDrawable = new ProgressBarDrawable();
        progressBarDrawable.setColor(ResourcesCompat.getColor(getResources(),R.color.colorAccent,null));

        mSimpleDraweeView.setHierarchy(builder.setProgressBarImage(progressBarDrawable).build());

        return view;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        mGifId = args.getString(GIF_ID);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(GIF_ID,mGifId);
    }

    @Override
    public void onResume() {
        super.onResume();

        GifService.fetchGifWithIdAsync(mGifId,this);

    }

    /**The onDataLoaded(GifItem) method is called by the GifService class on the callback object
     * when the queried gifItem is returned.
     * @param gifItem - Queried Gif Item that is returned to the callback object*/
    @Override
    public void onDataLoaded(GifItem gifItem) {

        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setUri(gifItem.getImages().getOriginal().getWebp())
                .setAutoPlayAnimations(true)
                .build();

        mSimpleDraweeView.setController(draweeController);
    }
}
