package com.young.newsgathering.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.young.newsgathering.R;
import com.young.newsgathering.ToastUtils;

public class ReviewDialog extends BaseDialog {
    private ConfirmClickListener listener;

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
        holder.setOnClickListener(R.id.submit, v -> {
            String reason = ((EditText) holder.getView(R.id.return_reason_text)).getText().toString().trim();
            if (TextUtils.isEmpty(reason)) {
                ToastUtils.showShort("请输入退回原因");
            } else {
                listener.onConfirm(reason);
                dismiss();
            }
        });
    }

    public ReviewDialog setConfirmClickListener(ConfirmClickListener listener) {
        this.listener = listener;
        return this;
    }

    public interface ConfirmClickListener {
        public void onConfirm(String reason);
    }
}
