package com.young.newsgathering;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.young.newsgathering.entity.User;

import static android.content.Context.MODE_PRIVATE;

public class UserUtil {
    private User user;
    private static UserUtil userUtil = new UserUtil();

    public static UserUtil getInstance() {
        return userUtil;
    }

    public void save(User user) {
        this.user = user;
        SharedPreferences spf = Utils.getApp().getSharedPreferences("news", 0);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("user", new Gson().toJson(user));
        editor.apply();
    }

    public User getUser() {
        if (user != null) {
            return user;
        }
        SharedPreferences sp = Utils.getApp().getSharedPreferences("news", 0);
        String userJson = sp.getString("user", "");
        Log.i("aaa", "userJosn" + userJson);
        if (TextUtils.isEmpty(userJson)) {
            return null;
        } else {
            user = new Gson().fromJson(userJson, User.class);
            return user;
        }
    }

    public void updateAvatar(String url) {
        this.user.setAvatar(url);
        save(this.user);
    }

    public void logout() {
        this.user = null;
        SharedPreferences spf = Utils.getApp().getSharedPreferences("news", 0);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("user", "");
        editor.apply();
    }

    public boolean isAdmin() {
        return getUser().getType().equals("总编");
    }
}
