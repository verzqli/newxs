package com.young.newsgathering;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.functions.Consumer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.young.newsgathering.entity.User;

public class SplashActivity extends BaseActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {

    }

    @SuppressLint("CheckResult")
    @Override
    protected void initEvent() {
        //如果android版本>6.0 则进行权限判断，有权限才能进入App,低于6.0无需权限判断
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            new RxPermissions(this)
                    .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(aBoolean -> {
                        if (aBoolean) {
                            jumpApp();
                        } else {
                            ToastUtils.showShort("请先打开存储权限");
                            finish();
                        }
                    });
        } else {
            jumpApp();
        }

    }

    private void jumpApp() {

        User user = UserUtil.getInstance().getUser();
        //判断本地有没有用户信息，就是以前有没有登陆过，登陆过的话直接跳转HomeActivity，没登录就去登录
        if (user == null) {
            baseStartActivity(MainActivity.class);
        } else {
            baseStartActivity(HomeActivity.class);
        }
        finish();
    }
}
