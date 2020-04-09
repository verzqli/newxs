package com.young.newsgathering.fragment;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gyf.immersionbar.ImmersionBar;
import com.young.newsgathering.R;
import com.young.newsgathering.util.LollipopFixedWebView;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewFragment extends Fragment {
    private LollipopFixedWebView mWebview;

    public NewFragment() {
        // Required empty public constructor
    }

    public static NewFragment newInstance() {
        return new NewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        initView(view);
        return view;
    }

    private void initView(View view) {

        mWebview = view.findViewById(R.id.webView);
        //下面都是webview的基础设置
        WebSettings webSettings = mWebview.getSettings();
        // 让WebView能够执行javaScript
        webSettings.setJavaScriptEnabled(true);
        // 让JavaScript可以自动打开windows
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 设置缓存
        webSettings.setAppCacheEnabled(true);
        // 设置缓存模式,一共有四种模式
        webSettings.setCacheMode(webSettings.LOAD_CACHE_ELSE_NETWORK);
        // 设置缓存路径
//        webSettings.setAppCachePath("");
        // 支持缩放(适配到当前屏幕)
        webSettings.setSupportZoom(true);
        // 将图片调整到合适的大小
        webSettings.setUseWideViewPort(true);
        // 支持内容重新布局,一共有四种方式
        // 默认的是NARROW_COLUMNS
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // 设置可以被显示的屏幕控制
        webSettings.setDisplayZoomControls(true);
        // 设置默认字体大小
        webSettings.setDefaultFontSize(12);

        // 设置WebView属性，能够执行Javascript脚本
        mWebview.getSettings().setJavaScriptEnabled(true);
        //3、 加载需要显示的网页
        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("webview", "url: " + url);
                view.loadUrl(url);
                return true;
            }
        });
        mWebview.loadUrl("https://news.qq.com/");
    }

    public boolean onBackPressed() {
        //这是homeactivity传过来的返回键事件，这里看英文名也可以理解，如果这个网页可以返回上一步
        //就返回，不然就退出，这里把一个boolean传回去，如果为false，那边就直接调用super.onBackPressed()就直接推出了
        if (mWebview.canGoBack()) {
            mWebview.goBack();
            return true;
        } //后退
        return false;
    }
}
