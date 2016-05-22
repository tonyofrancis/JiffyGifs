package com.tonyofrancis.jiffygifs.fragment;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.tonyofrancis.jiffygifs.R;
import com.tonyofrancis.jiffygifs.api.GifService;
import com.tonyofrancis.jiffygifs.model.GifItem;

import java.util.List;

/**
 * Created by tonyofrancis on 5/21/16.
 */

public class DetailGifFragment extends Fragment implements GifService.Callback {

    private static final String GIF_ID = "com.tonyofrancis.fragments.gif_id";

    private String mGifId;
    private VideoView mVideoView;

    public static DetailGifFragment newInstance(String id) {

        Bundle args = new Bundle();
        args.putString(GIF_ID,id);

        DetailGifFragment fragment = new DetailGifFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gif_detail,container,false);

        mVideoView = (VideoView) view.findViewById(R.id.gif_video_view);
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

        return view;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        mGifId = args.getString(GIF_ID);
    }


    @Override
    public void onResume() {
        super.onResume();

        GifService.getInstance(getActivity().getApplication())
                .fetchGifWithIdAsync(mGifId,this);

    }

    @Override
    public void onPause() {
        super.onPause();

        if(mVideoView.isPlaying()) {
            mVideoView.stopPlayback();
        }
    }

    @Override
    public void onDataLoaded(List<GifItem> dataSet) {

    }

    @Override
    public void onDataLoaded(GifItem gifItem) {

        Uri uri = Uri.parse(gifItem.getImages().getOriginal().getMp4());
        mVideoView.setVideoURI(uri);
        mVideoView.requestFocus();
        mVideoView.start();
    }
}
