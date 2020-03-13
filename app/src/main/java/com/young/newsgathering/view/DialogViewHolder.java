package com.young.newsgathering.view;

import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.StringRes;

/**
 * <pre>
 *     author: Windows XP
 *     time  : 2019/4/10
 *     desc  :
 * </pre>
 */
public class DialogViewHolder {
    private SparseArray<View> views;
    private View convertView;

    private DialogViewHolder(View view) {
        convertView = view;
        views = new SparseArray<>();
    }

    public static DialogViewHolder create(View view) {
        return new DialogViewHolder(view);
    }

    /**
     * 获取View
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(@IdRes int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 设置文本
     *
     * @param viewId
     * @param text
     */
    public void setText(int viewId, @StringRes int text) {
        TextView textView = getView(viewId);
        textView.setText(text);
    }

    /**
     * 设置文本
     *
     * @param viewId
     * @param text
     */
    public void setText(int viewId, String text) {
        TextView textView = getView(viewId);
        textView.setText(text);
    }

    /**
     * 设置字体颜色
     *
     * @param viewId
     * @param colorId
     */
    public void setTextColor(int viewId, int colorId) {
        TextView textView = getView(viewId);
        textView.setTextColor(colorId);
    }

    /**
     * 设置背景图片
     *
     * @param viewId
     * @param resId
     */
    public void setBackgroundResource(int viewId, int resId) {
        View view = getView(viewId);
        view.setBackgroundResource(resId);
    }

    /**
     * 设置背景颜色
     *
     * @param viewId
     * @param colorId
     */
    public void setBackgroundColor(int viewId, int colorId) {
        View view = getView(viewId);
        view.setBackgroundColor(colorId);
    }

    /**
     * 设置点击事件
     *
     * @param viewId
     * @param listener
     */
    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
    }
}
