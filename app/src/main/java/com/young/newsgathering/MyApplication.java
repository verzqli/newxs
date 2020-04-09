package com.young.newsgathering;

import android.app.Application;
import android.os.Environment;

import java.io.File;

import cn.bmob.v3.Bmob;

public class MyApplication extends Application {
    private String outPath = Environment.getExternalStorageDirectory() + "/pic";
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        Bmob.initialize(this, "166a5983cc42785a7133b1af01d12e7c");
        new File(outPath).mkdir();
    }
}
