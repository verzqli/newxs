package com.young.newsgathering.view;

import com.young.newsgathering.R;

public class LoadDialog extends BaseDialog {
    private String content = "正在加载...";

    public static LoadDialog newInstance() {

        return new LoadDialog();
    }

    @Override
    public int setUpLayoutId() {
        return R.layout.load_dialog;
    }

    @Override
    public void convertView(DialogViewHolder holder, BaseDialog dialog) {
        setOutCancel(false);
        setDimAmout(0f);
        holder.setText(R.id.load_content, content);
    }

    public LoadDialog setContent(String content) {
        this.content = content;
        return this;
    }
}
