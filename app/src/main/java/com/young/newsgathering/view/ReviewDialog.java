package com.young.newsgathering.view;

import android.os.Bundle;

import com.young.newsgathering.R;

public class ReviewDialog extends BaseDialog {
    public static ReviewDialog newInstance() {
        return new ReviewDialog();
    }
    @Override
    public int setUpLayoutId() {
        return R.layout.dialog_review;
    }

    @Override
    public void convertView(DialogViewHolder holder, BaseDialog dialog) {
        setShowBottom(true);
    }
}
