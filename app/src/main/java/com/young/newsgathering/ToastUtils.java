package com.young.newsgathering;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;
import androidx.core.app.NotificationManagerCompat;

import java.lang.reflect.Field;


/**
 * toast简单工具类
 */
public final class ToastUtils {


    private ToastUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }



    public static void showShort(String message) {
        Toast.makeText(Utils.getApp(), message, Toast.LENGTH_SHORT).show();
    }
}