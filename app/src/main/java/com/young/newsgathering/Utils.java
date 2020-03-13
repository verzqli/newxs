package com.young.newsgathering;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.res.Resources;
import android.widget.Toast;

import com.young.newsgathering.entity.Article;

public class Utils {
    @SuppressLint("StaticFieldLeak")
    private static Application sApplication;
    public static Article article;
    private static float density = Resources.getSystem().getDisplayMetrics().density;

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void init(final Application app) {
        sApplication = app;
    }

    public static Application getApp() {
        return sApplication;
    }

    public static int dp2px(float dpValue) {
        return (int) (0.5f + dpValue * density);
    }
}
