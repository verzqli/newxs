package com.young.newsgathering;

import android.app.Application;

import cn.bmob.v3.Bmob;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        Bmob.initialize(this, "166a5983cc42785a7133b1af01d12e7c");
    }
}
