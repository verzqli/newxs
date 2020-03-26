package com.young.newsgathering;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

public class PreviewVideoActivity extends BaseActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_preview_video;
    }

    @Override
    protected void initView() {
        String url = getIntent().getStringExtra("url");
        VideoView videoView = findViewById(R.id.video_view);
        videoView.setVideoPath(url);
        videoView.setMediaController(new MediaController(this));
        /**
         * 视频播放完成时回调
         */
        videoView.setOnCompletionListener(mp -> {
            //播放完成时，再次循环播放
            videoView.start();
        });
        videoView.start();
    }

    @Override
    protected void initEvent() {

    }
}
