package com.young.newsgathering;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.young.newsgathering.entity.User;

public class SplashActivity extends BaseActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initEvent() {
        User user = UserUtil.getInstance().getUser();
        if (user == null) {
            baseStartActivity(MainActivity.class);
        } else {
            baseStartActivity(HomeActivity.class);
        }
        finish();
    }
}
