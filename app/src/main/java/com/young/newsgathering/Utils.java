package com.young.newsgathering;

import android.annotation.SuppressLint;
import android.app.Application;
import android.widget.Toast;

public class Utils {
    @SuppressLint("StaticFieldLeak")
    private static Application sApplication;

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void init(final Application app) {
        sApplication = app;
    }

    public static Application getApp() {
        return sApplication;
    }
}
