package com.young.newsgathering.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

public class MaterialItemImage extends PreviewImageView {
    public MaterialItemImage(Context context) {
        super(context);
    }

    public MaterialItemImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }


}
