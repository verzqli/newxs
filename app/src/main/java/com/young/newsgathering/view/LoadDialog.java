package com.young.newsgathering.view;

import com.young.newsgathering.R;

public class LoadDialog extends BaseDialog {
    private String content = "正在加载...";
    private DialogViewHolder holder;

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
        this.holder = holder;
    }

    public void setContent(String content) {
        this.holder.setText(R.id.load_content, content);
    }

}
