package com.young.newsgathering.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.young.newsgathering.PreviewImageActivity;
import com.young.newsgathering.PreviewVideoActivity;
import com.young.newsgathering.ToastUtils;
import com.zhihu.matisse.internal.ui.AlbumPreviewActivity;

public class PreviewImageView extends androidx.appcompat.widget.AppCompatImageView {
    public PreviewImageView(Context context) {
        super(context);
    }

    public PreviewImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setUrl(String url, boolean isImage) {
        Glide.with(this).load(url).into(this);
        setOnClickListener(v -> {
            if (isImage) {
                Intent intent = new Intent(getContext(), PreviewImageActivity.class);
                intent.putExtra("url", url);
                getContext().startActivity(intent);
            } else {
                Intent intent = new Intent(getContext(), PreviewVideoActivity.class);
                intent.putExtra("url", url);
                getContext().startActivity(intent);
            }
        });
    }
}
